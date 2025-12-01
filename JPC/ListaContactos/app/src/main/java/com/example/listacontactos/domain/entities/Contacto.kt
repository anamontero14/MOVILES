package com.example.listacontactos.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacto_entity")
data class Contacto(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val genero: String
)
