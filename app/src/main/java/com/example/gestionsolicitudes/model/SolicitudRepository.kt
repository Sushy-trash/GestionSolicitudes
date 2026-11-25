
package com.example.gestionsolicitudes.model

import com.example.gestionsolicitudes.data.remote.SolicitudApiService
import kotlinx.coroutines.flow.Flow

class SolicitudRepository(
    private val solicitudDao: SolicitudDao,
    private val api: SolicitudApiService
) {

    fun obtenerSolicitudes(): Flow<List<SolicitudEntity>> {
        return solicitudDao.obtenerSolicitudes()
    }

    suspend fun sincronizarSolicitudes() {
        val remotas = api.obtenerSolicitudes()

        remotas.forEach { remota ->
            val local = solicitudDao.obtenerSolicitudPorCodigo(remota.codigo)

            if (local == null) {
                solicitudDao.insertarSolicitud(remota.copy(id = 0))
            } else {
                solicitudDao.actualizarSolicitud(remota.copy(id = local.id))
            }
        }
    }

    suspend fun obtenerSolicitudPorId(id: Long): SolicitudEntity? {
        return solicitudDao.obtenerSolicitudPorId(id)
    }

    suspend fun insertarSolicitud(solicitud: SolicitudEntity): Long {
        val respuesta = api.crearSolicitud(solicitud.copy(id = 0))
        return solicitudDao.insertarSolicitud(respuesta.copy(id = 0))
    }

    suspend fun actualizarSolicitud(solicitud: SolicitudEntity) {
        api.actualizarSolicitud(solicitud.id, solicitud)
        solicitudDao.actualizarSolicitud(solicitud)
    }

    suspend fun eliminarSolicitud(id: Long) {
        val solicitud = solicitudDao.obtenerSolicitudPorId(id)
            ?: throw IllegalStateException("Solicitud no encontrada")

        api.eliminarSolicitud(solicitud.id)
        solicitudDao.eliminarSolicitud(id)
    }

    suspend fun obtenerSolicitudPorCodigo(
        codigo: String
    ): SolicitudEntity? {
        val local = solicitudDao.obtenerSolicitudPorCodigo(codigo)
        if (local != null) return local

        return api.obtenerSolicitudes().firstOrNull {
            it.codigo == codigo
        }
    }
}
