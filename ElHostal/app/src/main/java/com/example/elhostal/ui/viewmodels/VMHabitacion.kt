package com.example.elhostal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elhostal.data.repositories.RepositoryHabitaciones
import com.example.elhostal.data.repositories.RepositoryReservas
import com.example.elhostal.domain.entities.Habitacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VMHabitacion(
    private val habitacionesRepo: RepositoryHabitaciones,
    private val reservasRepo: RepositoryReservas
) : ViewModel() {

    //lista de habitaciones convertida a StateFlow
    val listaHabitaciones: StateFlow<List<Habitacion>> = habitacionesRepo.getAllHabitaciones().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    //obtiene todas las habitaciones
    fun getAllHabitaciones(): StateFlow<List<Habitacion>> {
        return listaHabitaciones
    }

    //añade una nueva habitacion a la base de datos
    fun addHabitacion(habitacion: Habitacion) {
        viewModelScope.launch(Dispatchers.IO) {
            habitacionesRepo.addHabitacion(habitacion)
        }
    }

    //obtiene una habitacion especifica por su id
    fun getHabitacionPorID(id: Long): Habitacion? {
        var habitacion: Habitacion? = null
        viewModelScope.launch(Dispatchers.IO) {
            habitacion = habitacionesRepo.getHabitacionPorID(id) as Habitacion?
        }
        return habitacion
    }

    //verifica si una habitacion esta ocupada consultando la ultima reserva activa
    fun isHabitacionOcupada(idHabitacion: Int): Boolean {
        val ultimaReserva = reservasRepo.getUltimaReservaDeHabitacion(idHabitacion)
        return ultimaReserva != null
    }
}