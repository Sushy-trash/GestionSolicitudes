package com.example.gestionsolicitudes.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gestionsolicitudes.model.SolicitudEntity

@Database(
    entities = [SolicitudEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun solicitudDao(): SolicitudDao
}