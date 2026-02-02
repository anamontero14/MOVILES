package com.example.elhostal.data.repositories

import com.example.elhostal.domain.dao.HabitacionDAO
import com.example.elhostal.domain.entities.Habitacion
import kotlinx.coroutines.flow.Flow

class RepositoryHabitaciones(private val habitacionDAO: HabitacionDAO) {

    //funcion que llama a la clase dao para devolver la lista de todas las habitaciones
    fun getAllHabitaciones(): Flow<List<Habitacion>> {
        return habitacionDAO.getAllHabitaciones()
    }

    //funcion a la que le llega una nueva habitación y la añade
    fun addHabitacion(habitacion: Habitacion) {
        habitacionDAO.addHabitacion(habitacion)
    }

    //funcion que obtiene una habitacion por el id
    fun getHabitacionPorID(id: Long) {
        habitacionDAO.getHabitacionByID(id)
    }

}