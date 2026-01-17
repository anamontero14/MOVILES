package com.example.piedrapapeltijera.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.piedrapapeltijera.domain.dao.JugadaDAO
import com.example.piedrapapeltijera.domain.entities.Jugada

@Database(entities = [Jugada::class], version = 1)
abstract class JugadasDatabase : RoomDatabase() {
    abstract fun jugadaDao(): JugadaDAO
}