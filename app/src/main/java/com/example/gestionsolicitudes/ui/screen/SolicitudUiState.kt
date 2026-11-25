package com.example.gestionsolicitudes.ui.screen

import com.example.gestionsolicitudes.model.SolicitudEntity

data class SolicitudUiState(
    // Datos del formulario
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val servicio: String = "",
    val evidenciaUri: String? = null,
    val estado: String = "Recibida",

    // Validaciones
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val categoriaError: String? = null,

    // Estado de las operaciones
    val guardando: Boolean = false,
    val guardadoExitosamente: Boolean = false,
    val mensajeError: String? = null,

    // Datos de las solicitudes
    val solicitudes: List<SolicitudEntity> = emptyList(),
    val solicitudEditandoId: Long? = null
)