package com.example.elhostal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitacion_entity")
data class Habitacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tipoCama: String,
    val bañoPrivado: Boolean,
    val wifi: Boolean,
    val ac: Boolean,
    val serviciosExtra: Boolean
)
