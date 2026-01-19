package com.example.piedrapapeltijera.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piedrapapeltijera.data.repositories.RepositoryJugadas
import com.example.piedrapapeltijera.domain.entities.Jugada
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class VMJugadas(private val repository: RepositoryJugadas) : ViewModel() {

    //constante privada que guarda el jugador final
    private val _ganadorFinal = MutableStateFlow("")

    //constante que almacena el ganador final para que sea accesible para otras clases
    val ganadorFinal: StateFlow<String> = _ganadorFinal

    // Convertimos el Flow a StateFlow
    val listaJugadas: StateFlow<List<Jugada>> = repository.getAllJugadas().stateIn(
        scope = viewModelScope,
        // Pausa si no hay nadie viendo la pantalla
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList() // Valor inicial obligatorio
    )

    //funcion para obtener todas las jugadas que hay en la BBDD
    fun getAllJugadas(): StateFlow<List<Jugada>> {
        return listaJugadas
    }

    //funcion para añadir una nueva jugada a la BBDD
    private fun addJugada(jugada: Jugada){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addJugada(jugada)
        }
    }

    //genera una jugada aleatoria de la ia
    fun generarJugadaIA(): Int {
        return Random.nextInt(0, 3) // 0=Piedra, 1=Papel, 2=Tijera
    }

    //hace una jugada y la guarda en la BBDD
    fun realizarJugada(numJugada: Int, jugadaJugador: Int, jugadaIA: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            //se guarda el resultado de la partida
            val resultado = calcularResultado(jugadaJugador, jugadaIA)
            //se crea una nueva jugada
            val nuevaJugada = Jugada(
                numJugada = numJugada,
                resultadoJugada = resultado
            )
            //se añade la nueva jugada a la BBDD
            addJugada(nuevaJugada)
        }
    }

    //calcula el resultado: 0=Empate, 1=Victoria Jugador, 2=Victoria IA
    fun calcularResultado(jugador: Int, ia: Int): Int {
        return when {
            jugador == ia -> 0
            (jugador == 0 && ia == 2) || (jugador == 1 && ia == 0) || (jugador == 2 && ia == 1) -> 1
            else -> 2
        }
    }

    //calcular ganador final y actualizar el StateFlow
    fun calcularGanadorFinal() {
        viewModelScope.launch {
            val jugadas = listaJugadas.value
            val victoriasJugador = jugadas.count { it.resultadoJugada == 1 }
            val victoriasIA = jugadas.count { it.resultadoJugada == 2 }

            _ganadorFinal.value = when {
                victoriasJugador > victoriasIA -> "¡Has ganado!"
                victoriasIA > victoriasJugador -> "Ha ganado la IA"
                else -> "¡Empate!"
            }
        }
    }

    //reinicia el juego
    fun reiniciarJuego() {
        viewModelScope.launch {
            _ganadorFinal.value = ""
        }
    }

}