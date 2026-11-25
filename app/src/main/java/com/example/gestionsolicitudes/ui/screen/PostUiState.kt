package com.example.gestionsolicitudes.ui.screen

import com.example.gestionsolicitudes.data.model.Post

data class PostUiState(
    val posts: List<Post> = emptyList(),
    val cargando: Boolean = false,
    val mensajeError: String? = null
)
