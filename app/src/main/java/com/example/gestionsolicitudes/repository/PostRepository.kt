package com.example.gestionsolicitudes.repository

import com.example.gestionsolicitudes.data.model.Post
import com.example.gestionsolicitudes.data.remote.ApiService

class PostRepository(
    private val apiService: ApiService
) {

    suspend fun getPosts(): List<Post> {
        return apiService.getPosts()
    }
}
