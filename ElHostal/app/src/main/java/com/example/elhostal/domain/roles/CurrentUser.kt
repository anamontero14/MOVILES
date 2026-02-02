package com.example.elhostal.domain.roles

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.elhostal.domain.entities.UsuarioLoggeado

object CurrentUser {
    // Expón el State, no solo el valor
    var usuario by mutableStateOf<UsuarioLoggeado?>(null)
        private set  // Solo modificable internamente

    fun login(user: UsuarioLoggeado) {
        usuario = user
    }

    fun setAsGuest() {
        usuario = null
    }

    fun getRole(): UserRole {
        return usuario?.role ?: UserRole.GUEST
    }

    fun hasPermission(permission: AppPermission): Boolean {
        return getRole().permissions.contains(permission)
    }

    fun logout() {
        usuario = null
    }
}
