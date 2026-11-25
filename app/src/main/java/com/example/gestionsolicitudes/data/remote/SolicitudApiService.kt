package com.example.gestionsolicitudes.data.remote

import com.example.gestionsolicitudes.model.SolicitudEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SolicitudApiService {

    @GET("api/solicitudes")
    suspend fun obtenerSolicitudes(): List<SolicitudEntity>

    @GET("api/solicitudes/{id}")
    suspend fun obtenerSolicitud(
        @Path("id") id: Long
    ): SolicitudEntity

    @POST("api/solicitudes")
    suspend fun crearSolicitud(
        @Body solicitud: SolicitudEntity
    ): SolicitudEntity

    @PUT("api/solicitudes/{id}")
    suspend fun actualizarSolicitud(
        @Path("id") id: Long,
        @Body solicitud: SolicitudEntity
    ): SolicitudEntity

    @DELETE("api/solicitudes/{id}")
    suspend fun eliminarSolicitud(
        @Path("id") id: Long
    )
}