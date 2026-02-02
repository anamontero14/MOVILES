package com.example.elhostal.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun V4ListaReservas(
    navController: NavHostController,
    viewmodelH: VMHabitacion,
    viewmodelR: VMReserva
) {
    val usuarioActual = CurrentUser.usuario

    // Si no hay usuario logueado, redirigir al login
    if (usuarioActual == null) {
        navController.navigate("VLogin")
        return
    }

    // Obtener las reservas del usuario actual
    val reservasUsuario by viewmodelR.getReservasUsuario(usuarioActual.id).collectAsState(initial = emptyList())
    val habitaciones by viewmodelH.listaHabitaciones.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Header con botones de navegación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Botón volver
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }

            // Título
            Text(
                text = "Mis Reservas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            )

            // Botón ir a habitaciones
            IconButton(
                onClick = { navController.navigate("V1ListaHabitaciones") }
            ) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Ir a habitaciones",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Divider()

        // Información del usuario
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Usuario: ${usuarioActual.nombre}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total de reservas: ${reservasUsuario.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Lista de reservas
        if (reservasUsuario.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No tienes reservas",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("V1ListaHabitaciones") }
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Habitaciones")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reservasUsuario) { reserva ->
                    ReservaCard(
                        reserva = reserva,
                        habitaciones = habitaciones,
                        onClick = {
                            navController.navigate("V3Reserva/${reserva.idHabitacionReservada}/${usuarioActual.id}")
                        }
                    )
                }

                // Espaciado al final
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun ReservaCard(
    reserva: Reserva,
    habitaciones: List<com.example.elhostal.domain.entities.Habitacion>,
    onClick: () -> Unit
) {
    val habitacion = habitaciones.find { it.id == reserva.idHabitacionReservada }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                reserva.isCancelada -> MaterialTheme.colorScheme.errorContainer
                !reserva.isLibre -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header de la card con estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Habitación ${reserva.idHabitacionReservada}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                // Badge de estado
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = when {
                        reserva.isCancelada -> MaterialTheme.colorScheme.error
                        !reserva.isLibre -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.primary
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when {
                                reserva.isCancelada -> Icons.Default.Close
                                !reserva.isLibre -> Icons.Default.CheckCircle
                                else -> Icons.Default.CheckCircle
                            },
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when {
                                reserva.isCancelada -> "CANCELADA"
                                !reserva.isLibre -> "OCUPADA"
                                else -> "ACTIVA"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider()

            Spacer(modifier = Modifier.height(12.dp))

            // Información de la reserva
            InfoRow(label = "ID Reserva", value = "#${reserva.id}")
            InfoRow(label = "Fecha", value = reserva.fecha)

            if (habitacion != null) {
                InfoRow(label = "Tipo de Cama", value = habitacion.tipoCama)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para ver detalles
            TextButton(
                onClick = onClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Ver detalles")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}