package com.example.prueba

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
import androidx.room.Database
import androidx.room.Room
import com.example.prueba.data.database.TareasDatabase
import com.example.prueba.ui.theme.PruebaTheme
import com.example.prueba.ui.viewmodels.VMTareas
import com.example.prueba.ui.views.ListaTareas

class MainActivity : ComponentActivity() {
    private val viewmodel: VMTareas by viewModels()

    companion object {
        lateinit var database: TareasDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
            applicationContext,			// Contexto de la aplicación
            TareasDatabase::class.java,		// Clase de la base de datos
            "tareas-db"				// Nombre de la base de datos
        ).build()					// Se construye
        enableEdgeToEdge()
        setContent {
            PruebaTheme {
                // El NavController y NavHost deben estar dentro del tema
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "V1Inicio"
                ) {
                    composable("V1Inicio") {
                        ListaTareas(
                            navController,
                            viewmodel
                        )
                    }
                    /*composable("VDetailScreen/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")
                        DestinationDetails(destinationId = id)
                    }*/
                }
            }
        }
    }
}