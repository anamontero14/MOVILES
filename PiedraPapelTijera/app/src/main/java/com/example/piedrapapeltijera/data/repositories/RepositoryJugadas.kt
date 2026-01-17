package com.example.piedrapapeltijera.data.repositories

import com.example.piedrapapeltijera.domain.dao.JugadaDAO
import com.example.piedrapapeltijera.domain.entities.Jugada
import com.example.piedrapapeltijera.data.database.JugadasDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RepositoryJugadas {

    private lateinit var jugadaDao: JugadaDAO

    // Método para inicializar el DAO
    fun initialize(database: JugadasDatabase) {
        jugadaDao = database.jugadaDao()
    }

    // Obtener todas las jugadas
    suspend fun getAllJugadas(): List<Jugada> {
        return withContext(Dispatchers.IO) {
            jugadaDao.getAllJugadas()
        }
    }

    // Agregar nueva jugada
    suspend fun addJugada(jugada: Jugada) {
        withContext(Dispatchers.IO) {
            jugadaDao.addJugada(jugada)
        }
    }

    // Limpiar todas las jugadas
    suspend fun limpiarJugadas() {
        withContext(Dispatchers.IO) {
            val jugadas = jugadaDao.getAllJugadas()
            jugadas.forEach { jugadaDao.deleteJugada(it) }
        }
    }
}