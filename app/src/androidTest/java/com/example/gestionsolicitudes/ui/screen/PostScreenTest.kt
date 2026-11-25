
package com.example.gestionsolicitudes.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gestionsolicitudes.data.model.Post
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue

@RunWith(AndroidJUnit4::class)
class PostScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun debeMostrarIndicadorDeCarga() {
        composeTestRule.setContent {
            PostScreen(
                uiState = PostUiState(cargando = true),
                onReintentar = {},
                onVolver = {}
            )
        }

        composeTestRule
            .onNodeWithText("Cargando publicaciones...")
            .assertIsDisplayed()
    }

    @Test
    fun debeMostrarMensajeDeError() {
        composeTestRule.setContent {
            PostScreen(
                uiState = PostUiState(
                    mensajeError = "Error de conexión"
                ),
                onReintentar = {},
                onVolver = {}
            )
        }

        composeTestRule
            .onNodeWithText("Error de conexión")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Reintentar")
            .assertIsDisplayed()
    }

    @Test
    fun debeEjecutarAccionAlReintentar() {
        var reintentoEjecutado = false

        composeTestRule.setContent {
            PostScreen(
                uiState = PostUiState(
                    mensajeError = "Error de conexión"
                ),
                onReintentar = {
                    reintentoEjecutado = true
                },
                onVolver = {}
            )
        }

        composeTestRule
            .onNodeWithText("Reintentar")
            .performClick()

        assertTrue(reintentoEjecutado)
    }

    @Test
    fun debeMostrarLasPublicaciones() {
        val publicaciones = listOf(
            Post(
                userId = 1,
                id = 1,
                title = "Primera publicación",
                body = "Contenido de prueba"
            ),
            Post(
                userId = 2,
                id = 2,
                title = "Segunda publicación",
                body = "Segundo contenido"
            )
        )

        composeTestRule.setContent {
            PostScreen(
                uiState = PostUiState(posts = publicaciones),
                onReintentar = {},
                onVolver = {}
            )
        }

        composeTestRule
            .onNodeWithText("Primera publicación")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Contenido de prueba")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Segunda publicación")
            .assertIsDisplayed()
    }

    @Test
    fun debeEjecutarAccionAlVolver() {
        var volverEjecutado = false

        composeTestRule.setContent {
            PostScreen(
                uiState = PostUiState(),
                onReintentar = {},
                onVolver = {
                    volverEjecutado = true
                }
            )
        }

        composeTestRule
            .onNodeWithText("Volver")
            .performClick()

        assertTrue(volverEjecutado)
    }
}
