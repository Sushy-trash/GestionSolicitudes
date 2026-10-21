package com.example.gestionsolicitudes.model

import kotlinx.coroutines.flow.Flow

class SolicitudRepository(
    private val solicitudDao: SolicitudDao
) {

    fun obtenerSolicitudes(): Flow<List<SolicitudEntity>> {
        return solicitudDao.obtenerSolicitudes()
    }

    suspend fun obtenerSolicitudPorId(id: Long): SolicitudEntity? {
        return solicitudDao.obtenerSolicitudPorId(id)
    }

    suspend fun insertarSolicitud(solicitud: SolicitudEntity): Long {
        return solicitudDao.insertarSolicitud(solicitud)
    }

    suspend fun actualizarSolicitud(solicitud: SolicitudEntity) {
        solicitudDao.actualizarSolicitud(solicitud)
    }

    suspend fun eliminarSolicitud(id: Long) {
        solicitudDao.eliminarSolicitud(id)
    }

    suspend fun obtenerSolicitudPorCodigo(
        codigo: String
    ): SolicitudEntity? = solicitudDao.obtenerSolicitudPorCodigo(codigo)
}