package com.example.gestionsolicitudes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionsolicitudes.data.remote.RetrofitInstance
import com.example.gestionsolicitudes.repository.PostRepository

class PostViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {

            val repository = PostRepository(
                RetrofitInstance.api
            )

            @Suppress("UNCHECKED_CAST")
            return PostViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}

