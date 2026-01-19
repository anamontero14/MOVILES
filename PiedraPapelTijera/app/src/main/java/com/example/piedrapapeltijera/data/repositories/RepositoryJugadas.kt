package com.example.piedrapapeltijera.data.repositories

import com.example.piedrapapeltijera.domain.dao.JugadaDAO
import com.example.piedrapapeltijera.domain.entities.Jugada
import com.example.piedrapapeltijera.data.database.JugadasDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RepositoryJugadas(private val jugadaDAO: JugadaDAO) {

    //funcion que llama a la clase dao para devolver la lista de todas las jugadas
    fun getAllJugadas(): Flow<List<Jugada>> {
        return jugadaDAO.getAllJugadas()
    }

    //funcion a la que le llega una jugada nueva y llama a la de la BBDD para agregar una jugada
    fun addJugada(jugada: Jugada) {
        jugadaDAO.addJugada(jugada)
    }
}