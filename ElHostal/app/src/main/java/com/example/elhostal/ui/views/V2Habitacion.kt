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
import com.example.elhostal.domain.entities.Habitacion
import com.example.elhostal.domain.roles.UserRole
import com.example.elhostal.ui.viewmodels.VMAuth
import com.example.elhostal.ui.viewmodels.VMHabitacion
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun V2Habitacion(
    navController: NavHostController,
    vmHabitacion: VMHabitacion,
    vmAuth: VMAuth,
    idHabitacion: Int
) {
    val habitaciones by vmHabitacion.listaHabitaciones.collectAsState()
    val habitacion = habitaciones.find { it.id == idHabitacion }
    val habitacionesOcupadas by vmHabitacion.habitacionesOcupadas.collectAsState()
    val estaOcupada = habitacionesOcupadas[idHabitacion] ?: false

    val currentUser by vmAuth.currentUser.collectAsState()
    val currentRole = vmAuth.getCurrentUserRole()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles de Habitación") },
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
        if (habitacion == null) {
            // Habitación no encontrada
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
                        text = "Habitación no encontrada",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Card con información de la habitación
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Habitación #${habitacion.id}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = if (estaOcupada) Color(0xFFF44336) else Color(0xFF4CAF50)
                            ) {
                                Text(
                                    text = if (estaOcupada) "OCUPADA" else "DISPONIBLE",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Tipo de cama
                        DetailRow(
                            icon = Icons.Default.Bed,
                            label = "Tipo de cama",
                            value = habitacion.tipoCama
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Servicios incluidos:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Servicios
                        ServiceItem(
                            icon = Icons.Default.Bathroom,
                            text = "Baño privado",
                            available = habitacion.bañoPrivado
                        )

                        ServiceItem(
                            icon = Icons.Default.Wifi,
                            text = "WiFi",
                            available = habitacion.wifi
                        )

                        ServiceItem(
                            icon = Icons.Default.AcUnit,
                            text = "Aire acondicionado",
                            available = habitacion.ac
                        )

                        ServiceItem(
                            icon = Icons.Default.RoomService,
                            text = "Servicios extra",
                            available = habitacion.serviciosExtra
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botón de reservar (solo si está disponible)
                if (!estaOcupada) {
                    Button(
                        onClick = {
                            if (currentUser != null) {
                                // Usuario logueado: ir a pantalla de reserva
                                navController.navigate(Routes.Reserva.createRoute(idHabitacion))
                            } else {
                                // Usuario no logueado: mostrar mensaje y navegar a login
                                scope.launch {
                                    snackbarHostState.showSnackbar("Debes iniciar sesión para reservar")
                                }
                                navController.navigate(Routes.Login.route)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reservar Habitación", fontSize = 16.sp)
                    }
                } else {
                    // Habitación ocupada
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE)
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
                                tint = Color(0xFFF44336)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Esta habitación está actualmente ocupada",
                                color = Color(0xFFC62828)
                            )
                        }
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
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

@Composable
private fun ServiceItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    available: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (available) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (available) Color(0xFF4CAF50) else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (available) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (available) Color.Black else Color.Gray
        )
    }
}