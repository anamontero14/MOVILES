package com.example.elhostal.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun V1ListaHabitaciones(
    navController: NavHostController,
    vmHabitacion: VMHabitacion,
    vmAuth: VMAuth
) {
    val habitaciones by vmHabitacion.listaHabitaciones.collectAsState()
    val habitacionesOcupadas by vmHabitacion.habitacionesOcupadas.collectAsState()
    val currentUser by vmAuth.currentUser.collectAsState()
    val currentRole = vmAuth.getCurrentUserRole()

    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "JetPack Stay Rooms",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                actions = {
                    // Botón añadir habitación (solo ADMIN)
                    if (currentRole == UserRole.ADMIN) {
                        IconButton(onClick = {
                            navController.navigate(Routes.AñadirHabitacion.route)
                        }) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Añadir habitación",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Header con información del usuario
            UserHeaderCard(
                currentUser = currentUser,
                currentRole = currentRole,
                onLoginClick = { navController.navigate(Routes.Login.route) },
                onRegisterClick = { navController.navigate(Routes.Register.route) },
                onReservasClick = { navController.navigate(Routes.ListaReservas.route) },
                onLogoutClick = { showLogoutDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas (solo para ADMIN)
            if (currentRole == UserRole.ADMIN) {
                AdminStatsCard(
                    totalHabitaciones = habitaciones.size,
                    habitacionesDisponibles = vmHabitacion.getNumeroHabitacionesDisponibles(),
                    habitacionesOcupadas = vmHabitacion.getNumeroHabitacionesOcupadas()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Título de la sección
            Text(
                text = if (habitaciones.isEmpty()) "Habitaciones" else "Habitaciones Disponibles",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de habitaciones o mensaje "NO VACANCY"
            if (habitaciones.isEmpty()) {
                NoVacancyMessage()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(habitaciones) { habitacion ->
                        HabitacionCard(
                            habitacion = habitacion,
                            estaOcupada = habitacionesOcupadas[habitacion.id] ?: false,
                            onClick = {
                                navController.navigate(Routes.Habitacion.createRoute(habitacion.id))
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogo de confirmación de logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vmAuth.logout()
                        showLogoutDialog = false
                        navController.navigate(Routes.ListaHabitaciones.route) {
                            popUpTo(Routes.ListaHabitaciones.route) { inclusive = true }
                        }
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
private fun UserHeaderCard(
    currentUser: com.example.elhostal.domain.entities.UsuarioLoggeado?,
    currentRole: UserRole,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onReservasClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (currentUser != null) {
                // Usuario logueado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Bienvenido, ${currentUser.nombre}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Rol: ${currentRole.name}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Icon(
                        imageVector = if (currentRole == UserRole.ADMIN)
                            Icons.Default.AdminPanelSettings
                        else
                            Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onReservasClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reservas")
                    }

                    OutlinedButton(
                        onClick = onLogoutClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Salir")
                    }
                }
            } else {
                // Usuario no logueado
                Text(
                    text = "¿Ya tienes cuenta?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onLoginClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Login, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Login")
                    }

                    OutlinedButton(
                        onClick = onRegisterClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Registro")
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStatsCard(
    totalHabitaciones: Int,
    habitacionesDisponibles: Int,
    habitacionesOcupadas: Int
) {
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
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                icon = Icons.Default.Home,
                label = "Total",
                value = totalHabitaciones.toString()
            )
            StatItem(
                icon = Icons.Default.CheckCircle,
                label = "Disponibles",
                value = habitacionesDisponibles.toString(),
                color = Color(0xFF4CAF50)
            )
            StatItem(
                icon = Icons.Default.Cancel,
                label = "Ocupadas",
                value = habitacionesOcupadas.toString(),
                color = Color(0xFFF44336)
            )
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun NoVacancyMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.NoMeetingRoom,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "NO VACANCY",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun HabitacionCard(
    habitacion: Habitacion,
    estaOcupada: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (estaOcupada) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Habitación #${habitacion.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (estaOcupada) Color(0xFFF44336) else Color(0xFF4CAF50)
                    ) {
                        Text(
                            text = if (estaOcupada) "OCUPADA" else "DISPONIBLE",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Tipo de cama: ${habitacion.tipoCama}", fontSize = 14.sp)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    if (habitacion.bañoPrivado) Chip("Baño privado")
                    if (habitacion.wifi) Chip("WiFi")
                    if (habitacion.ac) Chip("A/C")
                    if (habitacion.serviciosExtra) Chip("Servicios extra")
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
private fun Chip(text: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontSize = 12.sp
        )
    }
}