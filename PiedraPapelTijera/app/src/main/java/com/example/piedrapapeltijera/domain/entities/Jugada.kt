package com.example.piedrapapeltijera.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jugada_entity")
data class Jugada(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val numJugada: Int,
    val resultadoJugada: Int
)