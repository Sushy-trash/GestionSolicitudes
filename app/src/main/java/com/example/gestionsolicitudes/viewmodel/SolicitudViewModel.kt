package com.example.gestionsolicitudes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionsolicitudes.model.DatabaseProvider
import com.example.gestionsolicitudes.model.SolicitudEntity
import com.example.gestionsolicitudes.model.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SolicitudUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val servicio: String = "",
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val categoriaError: String? = null,
    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false,
    val mensajeError: String? = null,
    val solicitudes: List<SolicitudEntity> = emptyList()
)

class SolicitudViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dao = DatabaseProvider
        .getDatabase(application)
        .solicitudDao()

    private val repository = SolicitudRepository(dao)

    private val _uiState = MutableStateFlow(SolicitudUiState())
    val uiState: StateFlow<SolicitudUiState> = _uiState.asStateFlow()

    init {
        observarSolicitudes()
    }

    private fun observarSolicitudes() {
        viewModelScope.launch {
            repository.obtenerSolicitudes()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            mensajeError = error.message
                                ?: "No se pudieron cargar las solicitudes."
                        )
                    }
                }
                .collect { solicitudes ->
                    _uiState.update {
                        it.copy(solicitudes = solicitudes)
                    }
                }
        }
    }

    fun actualizarTitulo(valor: String) {
        _uiState.update {
            it.copy(titulo = valor, tituloError = null)
        }
    }

    fun actualizarDescripcion(valor: String) {
        _uiState.update {
            it.copy(descripcion = valor, descripcionError = null)
        }
    }

    fun actualizarCategoria(valor: String) {
        _uiState.update {
            it.copy(categoria = valor, categoriaError = null)
        }
    }

    fun actualizarServicio(valor: String) {
        _uiState.update {
            it.copy(servicio = valor)
        }
    }

    fun guardarSolicitud() {
        val actual = _uiState.value

        val tituloError = if (actual.titulo.isBlank()) {
            "El título es obligatorio."
        } else null

        val descripcionError = if (actual.descripcion.isBlank()) {
            "La descripción es obligatoria."
        } else null

        val categoriaError = if (actual.categoria.isBlank()) {
            "Selecciona una categoría."
        } else null

        if (
            tituloError != null ||
            descripcionError != null ||
            categoriaError != null
        ) {
            _uiState.update {
                it.copy(
                    tituloError = tituloError,
                    descripcionError = descripcionError,
                    categoriaError = categoriaError,
                    guardadoExitosamente = false
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    guardando = true,
                    guardadoExitosamente = false,
                    mensajeError = null
                )
            }

            try {
                val solicitud = SolicitudEntity(
                    titulo = actual.titulo.trim(),
                    descripcion = actual.descripcion.trim(),
                    categoria = actual.categoria.trim(),
                    servicioRelacionado = actual.servicio
                        .trim()
                        .ifBlank { null }
                )

                repository.insertarSolicitud(solicitud)

                _uiState.update {
                    it.copy(
                        titulo = "",
                        descripcion = "",
                        categoria = "",
                        servicio = "",
                        tituloError = null,
                        descripcionError = null,
                        categoriaError = null,
                        guardadoExitosamente = true
                    )
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        guardadoExitosamente = false,
                        mensajeError = error.message
                            ?: "No se pudo guardar la solicitud."
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(guardando = false)
                }
            }
        }
    }

    fun limpiarMensaje() {
        _uiState.update {
            it.copy(
                guardadoExitosamente = false,
                mensajeError = null
            )
        }
    }
}
