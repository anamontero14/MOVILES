package com.example.prueba.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.prueba.domain.entities.Tarea
import com.example.prueba.domain.entities.dao.TareasDao

@Database(entities = [Tarea::class], version = 1)
abstract class TareasDatabase : RoomDatabase() {
    abstract fun taskDao(): TareasDao
}