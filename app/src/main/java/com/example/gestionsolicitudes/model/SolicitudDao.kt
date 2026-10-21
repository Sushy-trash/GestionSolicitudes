package com.example.gestionsolicitudes.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gestionsolicitudes.model.SolicitudEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {

    @Query("SELECT * FROM solicitudes ORDER BY fechaCreacion DESC")
    fun obtenerSolicitudes(): Flow<List<SolicitudEntity>>

    @Query("SELECT * FROM solicitudes WHERE id = :id LIMIT 1")
    suspend fun obtenerSolicitudPorId(id: Long): SolicitudEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarSolicitud(solicitud: SolicitudEntity): Long

    @Update
    suspend fun actualizarSolicitud(solicitud: SolicitudEntity)

    @Query("DELETE FROM solicitudes WHERE id = :id")
    suspend fun eliminarSolicitud(id: Long)

    @Query("SELECT * FROM solicitudes WHERE codigo = :codigo LIMIT 1")
    suspend fun obtenerSolicitudPorCodigo(codigo: String): SolicitudEntity?
}