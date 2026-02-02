package com.example.elhostal.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elhostal.data.repositories.RepositoryUsuarioLoggeado
import com.example.elhostal.ui.viewmodels.VMAuth

// El Factory "enseña" a crear el ViewModel con su repositorio
class AuthFactory(private val repository: RepositoryUsuarioLoggeado) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VMAuth(repository) as T // <--- Aquí ocurre la magia
    }
}