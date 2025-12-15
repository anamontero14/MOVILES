package com.example.listacontactos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.listacontactos.ui.presentation.ContactsScreen
import com.example.listacontactos.ui.presentation.NuevoContacto
import com.example.listacontactos.ui.theme.ListaContactosTheme
import com.example.prueba.data.database.ContactosDatabase

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var database: ContactosDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
            applicationContext,			// Contexto de la aplicación
            ContactosDatabase::class.java,		// Clase de la base de datos
            "contactos-db"				// Nombre de la base de datos
        ).build()
        enableEdgeToEdge()
        setContent {
            ListaContactosTheme {
                val modifier: Modifier = Modifier.padding(top = 24.dp)
                // El NavController y NavHost deben estar dentro del tema
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "VListaContactos"
                ) {
                    composable("VListaContactos") {
                        ContactsScreen(
                            modifier,
                            navController,
                            database)
                    }
                    composable("VNuevoContacto") {
                        NuevoContacto(
                            navController
                        )
                    }
                }
            }
        }
    }
}