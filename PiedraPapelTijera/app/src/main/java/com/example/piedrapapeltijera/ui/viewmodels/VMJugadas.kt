package com.example.piedrapapeltijera.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piedrapapeltijera.MainActivity
import com.example.piedrapapeltijera.data.repositories.RepositoryJugadas
import com.example.piedrapapeltijera.domain.entities.Jugada
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class VMJugadas : ViewModel() {

    private val _ganadorFinal = MutableStateFlow("")
    val ganadorFinal: StateFlow<String> = _ganadorFinal

    init {
        RepositoryJugadas.initialize(MainActivity.database)
    }

    // Generar jugada aleatoria de la IA
    fun generarJugadaIA(): Int {
        return Random.nextInt(0, 3) // 0=Piedra, 1=Papel, 2=Tijera
    }

    // Realizar una jugada y guardarla en la BD
    fun realizarJugada(numJugada: Int, jugadaJugador: Int, jugadaIA: Int) {
        viewModelScope.launch {
            val resultado = calcularResultado(jugadaJugador, jugadaIA)

            val nuevaJugada = Jugada(
                numJugada = numJugada,
                resultadoJugada = resultado
            )

            RepositoryJugadas.addJugada(nuevaJugada)
        }
    }

    // Calcula el resultado: 0=Empate, 1=Victoria Jugador, 2=Victoria IA
    fun calcularResultado(jugador: Int, ia: Int): Int {
        return when {
            jugador == ia -> 0
            (jugador == 0 && ia == 2) || (jugador == 1 && ia == 0) || (jugador == 2 && ia == 1) -> 1
            else -> 2
        }
    }

    // Calcular ganador final y actualizar el StateFlow
    fun calcularGanadorFinal() {
        viewModelScope.launch {
            val jugadas = RepositoryJugadas.getAllJugadas()
            val victoriasJugador = jugadas.count { it.resultadoJugada == 1 }
            val victoriasIA = jugadas.count { it.resultadoJugada == 2 }

            _ganadorFinal.value = when {
                victoriasJugador > victoriasIA -> "¡Has ganado!"
                victoriasIA > victoriasJugador -> "Ha ganado la IA"
                else -> "¡Empate!"
            }
        }
    }

    // Reiniciar juego (limpiar BD)
    fun reiniciarJuego() {
        viewModelScope.launch {
            RepositoryJugadas.limpiarJugadas()
            _ganadorFinal.value = ""
        }
    }
}