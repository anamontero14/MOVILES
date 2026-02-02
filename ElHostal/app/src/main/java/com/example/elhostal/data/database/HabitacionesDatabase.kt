package com.example.elhostal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.elhostal.domain.dao.HabitacionDAO
import com.example.elhostal.domain.entities.Habitacion

@Database(entities = [Habitacion::class], version = 1)
abstract class HabitacionesDatabase : RoomDatabase() {
    abstract fun habitacionDAO(): HabitacionDAO
}