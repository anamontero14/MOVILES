package com.example.piedrapapeltijera.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.piedrapapeltijera.domain.entities.Jugada

@Dao
interface JugadaDAO {
    @Query("SELECT * FROM jugada_entity")
    suspend fun getAllJugadas(): MutableList<Jugada>

    @Insert
    suspend fun addJugada(jugada: Jugada): Long

    @Query("SELECT * FROM jugada_entity WHERE id LIKE :id")
    suspend fun getJugadaByID(id: Long): Jugada

    @Update
    suspend fun updateJugada(jugada: Jugada): Int

    @Delete
    suspend fun deleteJugada(jugada: Jugada): Int
}