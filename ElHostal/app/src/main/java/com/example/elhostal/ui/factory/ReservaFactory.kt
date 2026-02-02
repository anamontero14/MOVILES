package com.example.elhostal.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elhostal.data.repositories.RepositoryReservas
import com.example.elhostal.data.repositories.RepositoryUsuarioLoggeado
import com.example.elhostal.ui.viewmodels.VMAuth
import com.example.elhostal.ui.viewmodels.VMReserva

// El Factory "enseña" a crear el ViewModel con su repositorio
class ReservaFactory(private val repository: RepositoryReservas) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VMReserva(repository) as T // <--- Aquí ocurre la magia
    }
}