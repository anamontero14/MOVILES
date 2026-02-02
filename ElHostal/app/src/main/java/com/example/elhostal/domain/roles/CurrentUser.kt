package com.example.elhostal.domain.roles

import com.example.elhostal.domain.entities.UsuarioLoggeado

object CurrentUser {

    private var usuario: UsuarioLoggeado? = null

    // Cuando alguien hace login
    fun login(user: UsuarioLoggeado) {
        this.usuario = user
    }

    // Cuando entra como invitado
    fun setAsGuest() {
        this.usuario = null
    }

    // Obtener el rol actual
    fun getRole(): UserRole {
        return usuario?.role ?: UserRole.GUEST
    }

    // Verificar si tiene un permiso
    fun hasPermission(permission: AppPermission): Boolean {
        return getRole().permissions.contains(permission)
    }

    // Cerrar sesión
    fun logout() {
        this.usuario = null
    }
}