package com.example.elhostal.data.repositories

import com.example.elhostal.domain.dao.ReservaDAO
import com.example.elhostal.domain.entities.Reserva
import kotlinx.coroutines.flow.Flow

data class RepositoryReservas(private val reservaDAO: ReservaDAO) {

    //obtiene todas las reservas
    fun getAllReservas(): Flow<List<Reserva>> {
        return reservaDAO.getAllReservas()
    }

    //añadir una reserva
    fun addReserva(reserva: Reserva): Long {
        return reservaDAO.addReserva(reserva)
    }

    //cancelar una reserva - maneja el nullable aquí
    fun cancelarReserva(reserva: Reserva?): Int {
        return if (reserva != null) {
            reservaDAO.cancelReserva(reserva)
        } else {
            0
        }
    }

    //liberar una reserva - maneja el nullable aquí
    fun liberarReserva(reserva: Reserva?): Int {
        return if (reserva != null) {
            reservaDAO.liberarReserva(reserva)
        } else {
            0
        }
    }

    //obtener todas las reservas de un usuario en concreto
    fun getReservasUsuario(id: Int): Flow<List<Reserva>> {
        return reservaDAO.getReservasByUsuario(id)
    }

    //obtener la ULTIMA reserva hecha de una habitacion específica
    fun getUltimaReservaDeHabitacion(id: Int): Reserva? {
        return reservaDAO.getUltimaReservaOcupadaByIDHabitacion(id)
    }

}