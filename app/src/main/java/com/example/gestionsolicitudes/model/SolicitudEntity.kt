package com.example.gestionsolicitudes.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "solicitudes",
    indices = [Index(value = ["codigo"], unique = true)]
)
data class SolicitudEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val codigo: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val servicioRelacionado: String? = null,
    val estado: String = "Recibida",
    val fechaCreacion: Long = System.currentTimeMillis()
)
