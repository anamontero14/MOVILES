package com.example.elhostal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.elhostal.domain.dao.ReservaDAO
import com.example.elhostal.domain.entities.Reserva

@Database(entities = [Reserva::class], version = 1)
abstract class ReservasDatabase : RoomDatabase() {
    abstract fun reservaDao(): ReservaDAO
}