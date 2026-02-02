package com.example.elhostal.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.elhostal.Routes
import com.example.elhostal.ui.viewmodels.CancelarReservaState
import com.example.elhostal.ui.viewmodels.VMReserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun V5CancelarReserva(
    navController: NavHostController,
    vmReserva: VMReserva,
    idReserva: Int
) {
    val todasLasReservas by vmReserva.listaReservas.collectAsState()
    val reserva = todasLasReservas.find { it.id == idReserva }
    val cancelarState by vmReserva.cancelarReservaState.collectAsState()

    // Navegar de vuelta cuando se cancele correctamente
    LaunchedEffect(cancelarState) {
        if (cancelarState is CancelarReservaState.Success) {
            navController.navigate(Routes.ListaReservas.route) {
                popUpTo(Routes.ListaReservas.route) { inclusive = true }
            }
            vmReserva.resetCancelarReservaState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cancelar Reserva") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (reserva == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Reserva no encontrada",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
        } else if (reserva.isCancelada) {
            // Reserva ya cancelada
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color(0xFFF44336)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Reserva Ya Cancelada",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Esta reserva ya fue cancelada previamente y no puede ser modificada.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else if (reserva.isLibre) {
            // Reserva ya finalizada
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Reserva Finalizada",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Esta reserva ya fue finalizada y no puede ser cancelada.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Reserva activa - mostrar confirmación de cancelación
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = Color(0xFFFF9800)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¿Cancelar Reserva?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Detalles de la reserva:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        DetailRow(
                            icon = Icons.Default.Tag,
                            label = "ID Reserva",
                            value = "#${reserva.id}"
                        )

                        DetailRow(
                            icon = Icons.Default.Home,
                            label = "Habitación",
                            value = "#${reserva.idHabitacionReservada}"
                        )

                        DetailRow(
                            icon = Icons.Default.CalendarMonth,
                            label = "Fecha",
                            value = reserva.fecha
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFFFF9800)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Esta acción no se puede deshacer. La reserva será cancelada permanentemente.",
                            fontSize = 14.sp,
                            color = Color(0xFFE65100)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Mostrar error si existe
                when (cancelarState) {
                    is CancelarReservaState.Error -> {
                        Text(
                            text = (cancelarState as CancelarReservaState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    else -> {}
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("No, Volver")
                    }

                    Button(
                        onClick = { vmReserva.cancelarReserva(reserva) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF44336)
                        )
                    ) {
                        Icon(Icons.Default.Cancel, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sí, Cancelar")
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}