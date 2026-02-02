package com.example.elhostal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elhostal.data.repositories.RepositoryUsuarioLoggeado
import com.example.elhostal.domain.entities.UsuarioLoggeado
import com.example.elhostal.domain.roles.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VMAuth(
    private val usuariosRepo: RepositoryUsuarioLoggeado
) : ViewModel() {

    //lista de usuarios convertida a StateFlow
    val listaUsuarios: StateFlow<List<UsuarioLoggeado>> = usuariosRepo.getAllUsuarioLoggeado().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    //registra un nuevo usuario en la base de datos
    fun register(usuario: UsuarioLoggeado) {
        viewModelScope.launch(Dispatchers.IO) {
            usuariosRepo.addUsuarioLoggeado(usuario)
        }
    }

    //verifica las credenciales del usuario y realiza el login
    fun login(nombre: String, contraseña: String): UsuarioLoggeado? {
        val usuarios = listaUsuarios.value
        val usuarioEncontrado = usuarios.find {
            it.nombre == nombre && it.contraseña == contraseña
        }

        if (usuarioEncontrado != null) {
            CurrentUser.login(usuarioEncontrado)
        }

        return usuarioEncontrado
    }

    //cierra la sesion del usuario actual
    fun logout() {
        CurrentUser.logout()
    }

    //permite entrar como invitado sin autenticacion
    fun entrarComoInvitado() {
        CurrentUser.setAsGuest()
    }
}