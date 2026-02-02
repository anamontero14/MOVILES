package com.example.elhostal.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elhostal.data.repositories.RepositoryReservas
import com.example.elhostal.domain.entities.Reserva
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VMReserva(
    private val reservasRepo: RepositoryReservas
) : ViewModel() {

    //lista de todas las reservas convertida a StateFlow
    val listaReservas: StateFlow<List<Reserva>> = reservasRepo.getAllReservas().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    //obtiene todas las reservas
    fun getAllReservas(): StateFlow<List<Reserva>> {
        return listaReservas
    }

    //añade una nueva reserva a la base de datos
    fun addReserva(reserva: Reserva) {
        viewModelScope.launch(Dispatchers.IO) {
            reservasRepo.addReserva(reserva)
        }
    }

    //obtiene todas las reservas de un usuario especifico
    fun getReservasUsuario(idUsuario: Int): Flow<List<Reserva>> {
        return reservasRepo.getReservasUsuario(idUsuario)
    }

    //cancela una reserva marcandola como cancelada
    fun cancelarReserva(reserva: Reserva?) {
        reserva?.isCancelada = true
        viewModelScope.launch(Dispatchers.IO) {
            reservasRepo.cancelarReserva(reserva)
        }
    }

    //libera una reserva marcandola como libre
    fun liberarReserva(reserva: Reserva?) {
        reserva?.isLibre = true
        viewModelScope.launch(Dispatchers.IO) {
            reservasRepo.liberarReserva(reserva)
        }
    }

    //obtiene la ultima reserva activa de una habitacion especifica
    fun getUltimaReservaDeHabitacion(idHabitacion: Int): Reserva? {
        return reservasRepo.getUltimaReservaDeHabitacion(idHabitacion)
    }
}