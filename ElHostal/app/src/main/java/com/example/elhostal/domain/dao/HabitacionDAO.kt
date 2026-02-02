package com.example.elhostal.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.elhostal.domain.entities.Habitacion
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitacionDAO {
    @Query("SELECT * FROM habitacion_entity")
    fun getAllHabitaciones(): Flow<List<Habitacion>>

    @Insert
    fun addHabitacion(habitacion: Habitacion): Long

    @Query("SELECT * FROM habitacion_entity WHERE id LIKE :id")
    fun getHabitacionByID(id: Long): Habitacion
}