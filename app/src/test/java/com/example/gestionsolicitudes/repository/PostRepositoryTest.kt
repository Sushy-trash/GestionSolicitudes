
package com.example.gestionsolicitudes.repository

import com.example.gestionsolicitudes.data.model.Post
import com.example.gestionsolicitudes.data.remote.ApiService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class PostRepositoryTest : FunSpec({

    val apiService = mockk<ApiService>()
    val repository = PostRepository(apiService)

    test("Debe obtener correctamente las publicaciones") {
        val publicaciones = listOf(
            Post(
                userId = 1,
                id = 1,
                title = "Primera publicación",
                body = "Contenido de prueba"
            )
        )

        coEvery {
            apiService.getPosts()
        } returns publicaciones

        val resultado = repository.getPosts()

        resultado shouldBe publicaciones

        coVerify(exactly = 1) {
            apiService.getPosts()
        }
    }

    test("Debe devolver una lista vacía cuando no hay publicaciones") {
        coEvery {
            apiService.getPosts()
        } returns emptyList()

        val resultado = repository.getPosts()

        resultado shouldBe emptyList()

        coVerify(exactly = 1) {
            apiService.getPosts()
        }
    }
})
