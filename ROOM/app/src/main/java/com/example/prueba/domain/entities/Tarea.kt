package com.example.prueba.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_entity")
data class Tarea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreTarea: String = "",
    var estadoTarea: Boolean = false
)
