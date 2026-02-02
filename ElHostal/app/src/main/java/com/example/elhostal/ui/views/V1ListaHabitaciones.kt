package com.example.elhostal.ui.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.elhostal.domain.roles.AppPermission
import com.example.elhostal.domain.roles.CurrentUser
import com.example.elhostal.ui.viewmodels.VMHabitacion

@Composable
fun ListaHabitaciones(
    navController: NavHostController,
    viewmodelH: VMHabitacion
) {
    val habitaciones by viewmodelH.listaHabitaciones.collectAsState()
    val usuarioActual = CurrentUser.usuario
    val puedeCrearHabitacion = usuarioActual?.role?.permissions?.contains(AppPermission.CREATE_ROOM) ?: false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Espacio superior
        Spacer(modifier = Modifier.height(40.dp))

        // Botones superiores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Habitaciones",
                style = MaterialTheme.typography.headlineMedium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (usuarioActual == null) {
                    Button(onClick = { navController.navigate("VLogin") }) {
                        Text("Login")
                    }
                    Button(onClick = { navController.navigate("VRegister") }) {
                        Text("Register")
                    }
                } else {
                    // Botón de Mis Reservas
                    Button(onClick = { navController.navigate("V4ListaReservas") }) {
                        Text("Mis Reservas")
                    }
                    Button(
                        onClick = {
                            CurrentUser.logout()
                            navController.navigate("V1ListaHabitaciones") {
                                popUpTo("V1ListaHabitaciones") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Logout")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenido principal
        if (habitaciones.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay habitaciones disponibles")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(habitaciones) { habitacion ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("V2Habitacion/${habitacion.id}") }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Habitación ${habitacion.id}")
                            Text("Tipo: ${habitacion.tipoCama}")
                        }
                    }
                }
            }
        }
    }

    // Botón flotante solo para admin
    if (puedeCrearHabitacion) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            FloatingActionButton(
                onClick = { navController.navigate("VAñadirHabitacion") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir habitación")
            }
        }
    }
}