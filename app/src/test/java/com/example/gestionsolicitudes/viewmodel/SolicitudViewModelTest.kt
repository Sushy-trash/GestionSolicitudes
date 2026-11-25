
package com.example.gestionsolicitudes.viewmodel

import com.example.gestionsolicitudes.model.SolicitudEntity
import com.example.gestionsolicitudes.model.SolicitudRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class SolicitudViewModelTest : FunSpec({

    fun crearViewModel(repository: SolicitudRepository): SolicitudViewModel {
        every { repository.obtenerSolicitudes() } returns flowOf(emptyList())

        return SolicitudViewModel(repository)
    }

    test("Debe actualizar los campos del formulario") {
        val repository = mockk<SolicitudRepository>(relaxed = true)

        val viewModel = crearViewModel(repository)

        viewModel.actualizarTitulo("Problema técnico")
        viewModel.actualizarDescripcion("No funciona el sistema")
        viewModel.actualizarCategoria("Informática")
        viewModel.actualizarServicio("Soporte")

        viewModel.uiState.value.titulo shouldBe "Problema técnico"
        viewModel.uiState.value.descripcion shouldBe "No funciona el sistema"
        viewModel.uiState.value.categoria shouldBe "Informática"
        viewModel.uiState.value.servicio shouldBe "Soporte"
    }

    test("Debe limpiar el formulario al preparar una nueva solicitud") {
        val repository = mockk<SolicitudRepository>(relaxed = true)
        val viewModel = crearViewModel(repository)

        viewModel.actualizarTitulo("Solicitud anterior")
        viewModel.actualizarDescripcion("Descripción anterior")
        viewModel.actualizarCategoria("Informática")

        viewModel.prepararNuevaSolicitud()

        viewModel.uiState.value.titulo shouldBe ""
        viewModel.uiState.value.descripcion shouldBe ""
        viewModel.uiState.value.categoria shouldBe ""
        viewModel.uiState.value.solicitudEditandoId shouldBe null
        viewModel.uiState.value.guardadoExitosamente shouldBe false
    }

    test("Debe validar los campos obligatorios") {
        val repository = mockk<SolicitudRepository>(relaxed = true)
        val viewModel = crearViewModel(repository)

        viewModel.guardarSolicitud()

        viewModel.uiState.value.tituloError shouldBe
                "El título es obligatorio."

        viewModel.uiState.value.descripcionError shouldBe
                "La descripción es obligatoria."

        viewModel.uiState.value.categoriaError shouldBe
                "Selecciona una categoría."
    }

    test("Debe guardar una solicitud nueva correctamente") {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<SolicitudRepository>(relaxed = true)
                val viewModel = crearViewModel(repository)

                coEvery {
                    repository.insertarSolicitud(any())
                } returns 1L

                viewModel.actualizarTitulo("Problema técnico")
                viewModel.actualizarDescripcion("Error en el sistema")
                viewModel.actualizarCategoria("Informática")
                viewModel.actualizarServicio("Soporte")

                viewModel.guardarSolicitud()

                advanceUntilIdle()

                viewModel.uiState.value.guardadoExitosamente shouldBe true
                viewModel.uiState.value.guardando shouldBe false

                coVerify(exactly = 1) {
                    repository.insertarSolicitud(any())
                }
            } finally {
                Dispatchers.resetMain()
            }
        }
    }

    test("Debe mostrar error cuando falla el guardado") {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<SolicitudRepository>(relaxed = true)
                val viewModel = crearViewModel(repository)

                coEvery {
                    repository.insertarSolicitud(any())
                } throws Exception("Error al guardar")

                viewModel.actualizarTitulo("Problema técnico")
                viewModel.actualizarDescripcion("Error en el sistema")
                viewModel.actualizarCategoria("Informática")

                viewModel.guardarSolicitud()

                advanceUntilIdle()

                viewModel.uiState.value.guardadoExitosamente shouldBe false
                viewModel.uiState.value.mensajeError shouldBe "Error al guardar"
                viewModel.uiState.value.guardando shouldBe false
            } finally {
                Dispatchers.resetMain()
            }
        }
    }

    test("Debe cargar una solicitud existente") {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<SolicitudRepository>(relaxed = true)
                val solicitud = SolicitudEntity(
                    id = 5L,
                    titulo = "Solicitud de prueba",
                    descripcion = "Descripción de prueba",
                    categoria = "Informática",
                    servicioRelacionado = "Soporte"
                )

                coEvery {
                    repository.obtenerSolicitudPorId(5L)
                } returns solicitud

                val viewModel = crearViewModel(repository)

                viewModel.cargarSolicitud(5L)

                advanceUntilIdle()

                viewModel.uiState.value.titulo shouldBe solicitud.titulo
                viewModel.uiState.value.descripcion shouldBe solicitud.descripcion
                viewModel.uiState.value.categoria shouldBe solicitud.categoria
                viewModel.uiState.value.solicitudEditandoId shouldBe 5L
                viewModel.uiState.value.guardando shouldBe false
            } finally {
                Dispatchers.resetMain()
            }
        }
    }

    test("Debe eliminar una solicitud correctamente") {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<SolicitudRepository>(relaxed = true)
                val viewModel = crearViewModel(repository)

                viewModel.eliminarSolicitud(5L)

                advanceUntilIdle()

                viewModel.uiState.value.guardando shouldBe false

                coVerify(exactly = 1) {
                    repository.eliminarSolicitud(5L)
                }
            } finally {
                Dispatchers.resetMain()
            }
        }
    }

    test("Debe mostrar error cuando falla la eliminación") {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<SolicitudRepository>(relaxed = true)

                coEvery {
                    repository.eliminarSolicitud(5L)
                } throws Exception("Error al eliminar")

                val viewModel = crearViewModel(repository)

                viewModel.eliminarSolicitud(5L)

                advanceUntilIdle()

                viewModel.uiState.value.mensajeError shouldBe "Error al eliminar"
                viewModel.uiState.value.guardando shouldBe false
            } finally {
                Dispatchers.resetMain()
            }
        }
    }

    test("Debe limpiar los mensajes del estado") {
        val repository = mockk<SolicitudRepository>(relaxed = true)
        val viewModel = crearViewModel(repository)

        viewModel.actualizarTitulo("Prueba")
        viewModel.guardarSolicitud()

        viewModel.limpiarMensaje()

        viewModel.uiState.value.guardadoExitosamente shouldBe false
        viewModel.uiState.value.mensajeError shouldBe null
    }
})
