package com.example.prueba.domain.entities.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.prueba.domain.entities.Tarea

@Dao
interface TareasDao {
    @Query("SELECT * FROM task_entity")
    suspend fun getAllTareas(): MutableList<Tarea>

    @Insert
    suspend fun addTarea(tarea: Tarea): Long

    @Query("SELECT * FROM task_entity WHERE id LIKE :id")
    suspend fun getTareaByID(id: Long): Tarea

    @Update
    suspend fun updateTarea(tarea: Tarea): Int

    @Delete
    suspend fun deleteTarea(tarea: Tarea): Int
}