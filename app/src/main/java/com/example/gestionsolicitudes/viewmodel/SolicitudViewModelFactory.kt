package com.example.gestionsolicitudes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionsolicitudes.model.DatabaseProvider
import com.example.gestionsolicitudes.model.SolicitudRepository

class SolicitudViewModelFactory(
    private val repository: SolicitudRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SolicitudViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SolicitudViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}