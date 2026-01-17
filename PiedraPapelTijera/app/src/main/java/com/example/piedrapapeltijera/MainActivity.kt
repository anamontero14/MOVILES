package com.example.piedrapapeltijera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.piedrapapeltijera.data.database.JugadasDatabase
import com.example.piedrapapeltijera.ui.theme.PiedraPapelTijeraTheme
import com.example.piedrapapeltijera.ui.viewmodels.VMJugadas
import com.example.piedrapapeltijera.ui.views.Bienvenida
import com.example.piedrapapeltijera.ui.views.Juego
import com.example.piedrapapeltijera.ui.views.Resultados
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val viewmodel: VMJugadas by viewModels()

    companion object {
        lateinit var database: JugadasDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
        applicationContext,			// Contexto de la aplicación
        JugadasDatabase::class.java,		// Clase de la base de datos
        "tareas-db"				// Nombre de la base de datos
        ).build()					// Se construye



        enableEdgeToEdge()
        setContent {
            PiedraPapelTijeraTheme() {
                // El NavController y NavHost deben estar dentro del tema
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "V1Bienvenida"
                ) {
                    composable("V1Bienvenida") {
                        Bienvenida(
                            navController,
                            viewmodel
                        )
                    }
                    composable("V2Juego/{nombreJugador}") { backStackEntry ->
                        val nombreJugador = backStackEntry.arguments?.getString("nombreJugador")

                        if (nombreJugador != null) {
                            Juego(
                                nombreJugador,
                                navController,
                                viewmodel
                            )
                        }
                    }
                    composable("V3Resultado") {
                        Resultados(
                            viewmodel,
                            navController
                        )
                    }
                }
            }
        }
    }
}