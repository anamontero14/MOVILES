package com.example.elhostal.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.elhostal.domain.entities.Reserva
import com.example.elhostal.domain.roles.CurrentUser
import com.example.elhostal.ui.viewmodels.VMHabitacion
import com.example.elhostal.ui.viewmodels.VMReserva

@Composable
fun V2Habitacion(
    navController: NavHostController,
    viewmodelH: VMHabitacion,
    viewmodelR: VMReserva,
    habitacionId: Int
) {
    val habitaciones by viewmodelH.listaHabitaciones.collectAsState()
    val habitacion = habitaciones.find { it.id == habitacionId }

    val todasReservas by viewmodelR.listaReservas.collectAsState()
    val usuarioActual = CurrentUser.usuario
    val estaLogueado = usuarioActual != null

    var fecha by remember { mutableStateOf("") }
    var mostrarDialogoReserva by remember { mutableStateOf(false) }
    var mostrarDialogoLogin by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    // Verificar si ya existe una reserva activa para esta habitación
    val tieneReservaActiva = todasReservas.any { reserva ->
        reserva.idHabitacionReservada == habitacionId &&
                !reserva.isCancelada &&
                reserva.isLibre
    }

    if (habitacion == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Habitación no encontrada")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Espacio superior
        Spacer(modifier = Modifier.height(24.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Habitación ${habitacion.id}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // Características
        Text(
            text = "Características",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tipo de Cama")
                Text(habitacion.tipoCama, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CaracteristicaItem("Baño Privado", habitacion.bañoPrivado)
        CaracteristicaItem("WiFi", habitacion.wifi)
        CaracteristicaItem("Aire Acondicionado", habitacion.ac)
        CaracteristicaItem("Servicios Extra", habitacion.serviciosExtra)

        Spacer(modifier = Modifier.height(32.dp))

        // Sección de reserva
        Text(
            text = "Realizar Reserva",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Verificar si la habitación ya está reservada
        if (tieneReservaActiva) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Esta habitación ya tiene una reserva activa",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        } else {
            // Campo de fecha
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha de Reserva") },
                placeholder = { Text("Ej: 15 de marzo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (mensajeError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de reservar
            Button(
                onClick = {
                    mensajeError = ""

                    if (!estaLogueado) {
                        mostrarDialogoLogin = true
                        return@Button
                    }

                    if (fecha.isEmpty()) {
                        mensajeError = "Por favor, ingresa una fecha"
                        return@Button
                    }

                    // Crear la reserva
                    val nuevaReserva = Reserva(
                        idHabitacionReservada = habitacion.id,
                        idUsuario = usuarioActual!!.id,
                        fecha = fecha,
                        isLibre = true,
                        isCancelada = false
                    )

                    viewmodelR.addReserva(nuevaReserva)
                    mostrarDialogoReserva = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = if (estaLogueado) "Confirmar Reserva" else "Reservar (Login requerido)"
                )
            }
        }
    }

    // Diálogo de confirmación
    if (mostrarDialogoReserva) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = { Text("¡Reserva Exitosa!") },
            text = { Text("Tu reserva ha sido confirmada.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoReserva = false
                    navController.navigate("V4ListaReservas")
                }) {
                    Text("Ver Mis Reservas")
                }
            }
        )
    }

    // Diálogo de login
    if (mostrarDialogoLogin) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoLogin = false },
            icon = {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Login Requerido") },
            text = { Text("Debes iniciar sesión para reservar.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoLogin = false
                    navController.navigate("VLogin")
                }) {
                    Text("Ir a Login")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoLogin = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun CaracteristicaItem(nombre: String, disponible: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (disponible)
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(nombre)
            Icon(
                imageVector = if (disponible) Icons.Default.Check else Icons.Default.Close,
                contentDescription = null,
                tint = if (disponible)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}