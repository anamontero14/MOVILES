package com.example.elhostal.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.elhostal.data.repositories.RepositoryHabitaciones
import com.example.elhostal.data.repositories.RepositoryReservas
import com.example.elhostal.ui.viewmodels.VMHabitacion

// El Factory "enseña" a crear el ViewModel con su repositorio
class HabitacionFactory(private val repositoryH: RepositoryHabitaciones,
    private val repositoryR: RepositoryReservas) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VMHabitacion(repositoryH, repositoryR)
                as T // <--- Aquí ocurre la magia
    }
}