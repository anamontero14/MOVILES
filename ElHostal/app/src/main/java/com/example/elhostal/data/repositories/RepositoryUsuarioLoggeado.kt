package com.example.elhostal.data.repositories

import com.example.elhostal.domain.dao.UsuarioLoggeadoDAO
import com.example.elhostal.domain.entities.Habitacion
import com.example.elhostal.domain.entities.UsuarioLoggeado
import kotlinx.coroutines.flow.Flow

class RepositoryUsuarioLoggeado(private val usuarioLoggeadoDAO: UsuarioLoggeadoDAO) {
    //funcion que llama a la clase dao para devolver la lista de todos los usuarios
    fun getAllUsuarioLoggeado(): Flow<List<UsuarioLoggeado>> {
        return usuarioLoggeadoDAO.getAllUsuariosLoggeados()
    }

    //funcion a la que le llega un nuevo usuario
    fun addUsuarioLoggeado(usuarioLoggeado: UsuarioLoggeado) {
        usuarioLoggeadoDAO.addUsuarioLoggeado(usuarioLoggeado)
    }
}
