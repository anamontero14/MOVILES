package com.example.piedrapapeltijera.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.piedrapapeltijera.data.repositories.RepositoryJugadas

// El Factory "enseña" a crear el ViewModel con su repositorio
class JugadasFactory(private val repository: RepositoryJugadas) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VMJugadas(repository) as T // <--- Aquí ocurre la magia
    }
}