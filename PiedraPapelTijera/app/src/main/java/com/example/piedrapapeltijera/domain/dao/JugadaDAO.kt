package com.example.piedrapapeltijera.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.piedrapapeltijera.domain.entities.Jugada
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadaDAO {
    @Query("SELECT * FROM jugada_entity")
    fun getAllJugadas(): Flow<List<Jugada>>

    @Insert
    fun addJugada(jugada: Jugada): Long

    @Query("SELECT * FROM jugada_entity WHERE id LIKE :id")
    fun getJugadaByID(id: Long): Jugada

    @Update
    fun updateJugada(jugada: Jugada): Int

    @Delete
    fun deleteJugada(jugada: Jugada): Int
}