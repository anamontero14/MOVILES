package com.example.elhostal.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.elhostal.domain.roles.CurrentUser
import com.example.elhostal.ui.viewmodels.VMHabitacion
import com.example.elhostal.ui.viewmodels.VMReserva

@Composable
fun V3Reserva(
    navController: NavHostController,
    viewmodelH: VMHabitacion,
    viewmodelR: VMReserva,
    habitacionId: Int,
    clienteId: Int
) {
    val habitaciones by viewmodelH.listaHabitaciones.collectAsState()
    val reservas by viewmodelR.listaReservas.collectAsState()

    val habitacion = habitaciones.find { it.id == habitacionId }
    val reserva = reservas.find { it.idHabitacionReservada == habitacionId && it.idUsuario == clienteId }

    val usuarioActual = CurrentUser.usuario
    val esAdmin = usuarioActual?.role?.name == "ADMIN"

    var mostrarDialogoCancelar by remember { mutableStateOf(false) }
    var mostrarDialogoLiberar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Espacio superior
        Spacer(modifier = Modifier.height(40.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("V4ListaReservas") }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Detalles de Reserva",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        if (habitacion == null || reserva == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Reserva no encontrada")
            }
            return
        }

        // Estado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    reserva.isCancelada -> MaterialTheme.colorScheme.errorContainer
                    !reserva.isLibre -> MaterialTheme.colorScheme.tertiaryContainer
                    else -> MaterialTheme.colorScheme.primaryContainer
                }
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Estado", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when {
                        reserva.isCancelada -> "CANCELADA"
                        !reserva.isLibre -> "OCUPADA"
                        else -> "ACTIVA"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Info Habitación
        Text(
            text = "Habitación",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoCard("Número", "Habitación ${habitacion.id}")
        InfoCard("Tipo de Cama", habitacion.tipoCama)

        Spacer(modifier = Modifier.height(24.dp))

        // Info Reserva
        Text(
            text = "Detalles de Reserva",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoCard("ID Reserva", "#${reserva.id}")
        InfoCard("Fecha", reserva.fecha)
        InfoCard("Cliente ID", "#${reserva.idUsuario}")

        Spacer(modifier = Modifier.height(32.dp))

        // Botones
        if (!reserva.isCancelada) {
            if (esAdmin && reserva.isLibre) {
                Button(
                    onClick = { mostrarDialogoLiberar = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Marcar como Ocupada")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (usuarioActual?.id == clienteId || esAdmin) {
                OutlinedButton(
                    onClick = { mostrarDialogoCancelar = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cancelar Reserva")
                }
            }
        } else {
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
                    Text("Esta reserva ha sido cancelada")
                }
            }
        }
    }

    // Diálogo cancelar
    if (mostrarDialogoCancelar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCancelar = false },
            title = { Text("¿Cancelar Reserva?") },
            text = { Text("Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewmodelR.cancelarReserva(reserva)
                        mostrarDialogoCancelar = false
                        navController.navigate("V4ListaReservas")
                    }
                ) {
                    Text("Cancelar Reserva")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCancelar = false }) {
                    Text("Volver")
                }
            }
        )
    }

    // Diálogo ocupar
    if (mostrarDialogoLiberar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoLiberar = false },
            title = { Text("¿Marcar como Ocupada?") },
            text = { Text("La habitación se marcará como ocupada.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        reserva?.isLibre = false
                        viewmodelR.liberarReserva(reserva)
                        mostrarDialogoLiberar = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoLiberar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun InfoCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label)
            Text(value, fontWeight = FontWeight.SemiBold)
        }
    }
}