package com.example.elhostal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.elhostal.domain.roles.UserRole

@Entity(tableName = "usuario_loggeado_entity")
data class UsuarioLoggeado(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val contraseña: String,
    val role: UserRole
)
