package com.example.piedrapapeltijera.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.piedrapapeltijera.ui.viewmodels.VMJugadas
import kotlin.random.Random

@Composable
fun Juego(
    nombreJugador: String,
    navController: NavHostController,
    viewmodel: VMJugadas
) {
    var turnoActual by remember { mutableStateOf(1) }
    var jugadaJugador by remember { mutableStateOf<Int?>(null) }
    var jugadaIA by remember { mutableStateOf<Int?>(null) }
    var resultadoTurno by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF11998E),
                        Color(0xFF38EF7D)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Parte Superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🤖 IA: ESPAGUETI67",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Turno: $turnoActual/3",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Parte Media
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ParteMedio(turnoActual, jugadaJugador, jugadaIA, resultadoTurno)
            }

            // Parte Inferior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp, start = 24.dp, end = 24.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "👤 Jugador: $nombreJugador",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Botón Piedra
                    Button(
                        onClick = {
                            val jugada = 0
                            val jugadaIAGenerada = viewmodel.generarJugadaIA()
                            val resultado = viewmodel.calcularResultado(jugada, jugadaIAGenerada)

                            jugadaJugador = jugada
                            jugadaIA = jugadaIAGenerada
                            resultadoTurno = resultado

                            viewmodel.realizarJugada(turnoActual, jugada, jugadaIAGenerada)

                            if (turnoActual == 3) {
                                navController.navigate("V3Resultado")
                            }

                            turnoActual++
                        },
                        modifier = Modifier
                            .size(80.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("✊", fontSize = 40.sp)
                    }

                    // Botón Papel
                    Button(
                        onClick = {
                            val jugada = 1
                            val jugadaIAGenerada = viewmodel.generarJugadaIA()
                            val resultado = viewmodel.calcularResultado(jugada, jugadaIAGenerada)

                            jugadaJugador = jugada
                            jugadaIA = jugadaIAGenerada
                            resultadoTurno = resultado

                            viewmodel.realizarJugada(turnoActual, jugada, jugadaIAGenerada)

                            if (turnoActual == 3) {
                                navController.navigate("V3Resultado")
                            }

                            turnoActual++
                        },
                        modifier = Modifier
                            .size(80.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("✋", fontSize = 40.sp)
                    }

                    // Botón Tijera
                    Button(
                        onClick = {
                            val jugada = 2
                            val jugadaIAGenerada = viewmodel.generarJugadaIA()
                            val resultado = viewmodel.calcularResultado(jugada, jugadaIAGenerada)

                            jugadaJugador = jugada
                            jugadaIA = jugadaIAGenerada
                            resultadoTurno = resultado

                            viewmodel.realizarJugada(turnoActual, jugada, jugadaIAGenerada)

                            if (turnoActual == 3) {
                                navController.navigate("V3Resultado")
                            }

                            turnoActual++
                        },
                        modifier = Modifier
                            .size(80.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("✌️", fontSize = 40.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón Aleatorio
                Button(
                    onClick = {
                        val jugada = Random.nextInt(0, 3)
                        val jugadaIAGenerada = viewmodel.generarJugadaIA()
                        val resultado = viewmodel.calcularResultado(jugada, jugadaIAGenerada)

                        jugadaJugador = jugada
                        jugadaIA = jugadaIAGenerada
                        resultadoTurno = resultado

                        viewmodel.realizarJugada(turnoActual, jugada, jugadaIAGenerada)

                        if (turnoActual == 3) {
                            navController.navigate("V3Resultado")
                        }

                        turnoActual++
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "🎲 ALEATORIO",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ParteMedio(turnoActual: Int, jugadaJugador: Int?, jugadaIA: Int?, resultadoTurno: Int?) {
    val textoResultado = when (resultadoTurno) {
        0 -> "😐 ¡Empate!"
        1 -> "🎉 ¡Ganaste esta ronda!"
        2 -> "😔 Perdiste esta ronda"
        else -> ""
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (resultadoTurno != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = when (resultadoTurno) {
                        0 -> Color(0xFFFFA500).copy(alpha = 0.3f)
                        1 -> Color(0xFF4CAF50).copy(alpha = 0.3f)
                        else -> Color(0xFFF44336).copy(alpha = 0.3f)
                    }
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = textoResultado,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Jugada del Jugador
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "TÚ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF11998E)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val textoJugadaJugador = when (jugadaJugador) {
                        0 -> "✊"
                        1 -> "✋"
                        2 -> "✌️"
                        else -> "❓"
                    }
                    Text(
                        text = textoJugadaJugador,
                        fontSize = 60.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                text = "VS",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            // Jugada de la IA
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "IA",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF11998E)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val textoJugadaIA = when (jugadaIA) {
                        0 -> "✊"
                        1 -> "✋"
                        2 -> "✌️"
                        else -> "❓"
                    }
                    Text(
                        text = textoJugadaIA,
                        fontSize = 60.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}