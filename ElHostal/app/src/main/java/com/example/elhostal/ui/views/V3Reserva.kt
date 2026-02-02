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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.elhostal.Routes
import com.example.elhostal.ui.viewmodels.AddReservaState
import com.example.elhostal.ui.viewmodels.VMAuth
import com.example.elhostal.ui.viewmodels.VMHabitacion
import com.example.elhostal.ui.viewmodels.VMReserva
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun V3Reserva(
    navController: NavHostController,
    vmReserva: VMReserva,
    vmAuth: VMAuth,
    vmHabitacion: VMHabitacion,
    idHabitacion: Int
) {
    val habitaciones by vmHabitacion.listaHabitaciones.collectAsState()
    val habitacion = habitaciones.find { it.id == idHabitacion }

    val currentUser by vmAuth.currentUser.collectAsState()
    val addReservaState by vmReserva.addReservaState.collectAsState()

    var fecha by remember { mutableStateOf(getCurrentDate()) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Navegar a lista de reservas cuando la reserva sea exitosa
    LaunchedEffect(addReservaState) {
        if (addReservaState is AddReservaState.Success) {
            navController.navigate(Routes.ListaReservas.route) {
                popUpTo(Routes.ListaHabitaciones.route)
            }
            vmReserva.resetAddReservaState()
        } else if (addReservaState is AddReservaState.Error) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    (addReservaState as AddReservaState.Error).message
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Realizar Reserva") },
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (habitacion == null || currentUser == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Error al cargar la información")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Card de resumen de habitación
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Resumen de Reserva",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Habitación #${habitacion.id}")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Bed,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tipo: ${habitacion.tipoCama}")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Usuario: ${currentUser.nombre}")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Card de servicios incluidos
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Servicios incluidos:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (habitacion.bañoPrivado) ServiceChip("Baño privado")
                        if (habitacion.wifi) ServiceChip("WiFi")
                        if (habitacion.ac) ServiceChip("Aire acondicionado")
                        if (habitacion.serviciosExtra) ServiceChip("Servicios extra")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Información de fecha
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Fecha de reserva:",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = fecha,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botón de confirmar reserva
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirmar Reserva", fontSize = 16.sp)
                }
            }
        }
    }

    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = { Text("Confirmar Reserva") },
            text = {
                Column {
                    Text("¿Deseas confirmar la reserva de la Habitación #$idHabitacion?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Fecha: $fecha",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        currentUser?.let {
                            vmReserva.addReserva(
                                idHabitacion = idHabitacion,
                                idUsuario = it.id,
                                fecha = fecha
                            )
                        }
                        showConfirmDialog = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ServiceChip(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp)
    }
}

private fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date())
}