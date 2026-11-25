package com.example.gestionsolicitudes.viewmodel

import com.example.gestionsolicitudes.data.model.Post
import com.example.gestionsolicitudes.repository.PostRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest : FunSpec({

    test("Debe cargar correctamente las publicaciones") {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<PostRepository>()

                val publicaciones = listOf(
                    Post(
                        userId = 1,
                        id = 1,
                        title = "Primera publicación",
                        body = "Contenido de prueba"
                    )
                )

                coEvery {
                    repository.getPosts()
                } returns publicaciones

                val viewModel = PostViewModel(repository)

                advanceUntilIdle()

                viewModel.uiState.value.posts shouldBe publicaciones
                viewModel.uiState.value.cargando shouldBe false
                viewModel.uiState.value.mensajeError shouldBe null

            } finally {
                Dispatchers.resetMain()
            }
        }
    }


    test("Debe mostrar el estado de carga") {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<PostRepository>()
                val respuestaPendiente = CompletableDeferred<List<Post>>()

                coEvery {
                    repository.getPosts()
                } coAnswers {
                    respuestaPendiente.await()
                }

                val viewModel = PostViewModel(repository)

                runCurrent()

                // La petición sigue pendiente.
                viewModel.uiState.value.cargando shouldBe true

                // Simulamos que la API responde.
                respuestaPendiente.complete(emptyList())

                advanceUntilIdle()

                // La carga debe haber terminado.
                viewModel.uiState.value.cargando shouldBe false

            } finally {
                Dispatchers.resetMain()
            }
        }
    }


    test("Debe mostrar un mensaje cuando ocurre un error") {
        runTest {
            Dispatchers.setMain(StandardTestDispatcher(testScheduler))

            try {
                val repository = mockk<PostRepository>()

                coEvery {
                    repository.getPosts()
                } throws Exception("Error de conexión")

                val viewModel = PostViewModel(repository)

                advanceUntilIdle()

                viewModel.uiState.value.cargando shouldBe false
                viewModel.uiState.value.mensajeError shouldBe "Error de conexión"

            } finally {
                Dispatchers.resetMain()
            }
        }
    }
})
