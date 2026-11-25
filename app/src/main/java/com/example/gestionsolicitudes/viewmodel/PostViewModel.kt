package com.example.gestionsolicitudes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionsolicitudes.repository.PostRepository
import com.example.gestionsolicitudes.ui.screen.PostUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostUiState())
    val uiState: StateFlow<PostUiState> = _uiState.asStateFlow()

    init {
        cargarPosts()
    }

    fun cargarPosts() {
        viewModelScope.launch {
            _uiState.value = PostUiState(cargando = true)

            try {
                val publicaciones = repository.getPosts()

                _uiState.value = PostUiState(
                    posts = publicaciones,
                    cargando = false
                )

            } catch (e: Exception) {
                _uiState.value = PostUiState(
                    cargando = false,
                    mensajeError = e.message
                        ?: "No fue posible cargar las publicaciones."
                )
            }
        }
    }
}
