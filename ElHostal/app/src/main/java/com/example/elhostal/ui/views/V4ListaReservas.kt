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
import com.example.elhostal.domain.entities.Reserva
import com.example.elhostal.domain.roles.UserRole
import com.example.elhostal.ui.viewmodels.VMAuth
import com.example.elhostal.ui.viewmodels.VMHabitacion
import com.example.elhostal.ui.viewmodels.VMReserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun V4ListaReservas(
    navController: NavHostController,
    vmReserva: VMReserva,
    vmAuth: VMAuth,
    vmHabitacion: VMHabitacion
) {
    val currentUser by vmAuth.currentUser.collectAsState()
    val currentRole = vmAuth.getCurrentUserRole()
    val todasLasReservas by vmReserva.listaReservas.collectAsState()

    // Filtrar reservas según el rol
    val reservasMostradas = if (currentRole == UserRole.ADMIN) {
        todasLasReservas
    } else {
        todasLasReservas.filter { it.idUsuario == (currentUser?.id ?: -1) }
    }

    val habitaciones by vmHabitacion.listaHabitaciones.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = if (currentRole == UserRole.ADMIN) {
        listOf("Todas", "Activas", "Canceladas", "Finalizadas")
    } else {
        listOf("Activas", "Canceladas", "Finalizadas")
    }

    // Filtrar reservas según la pestaña seleccionada
    val reservasFiltradas = when (tabs[selectedTab]) {
        "Todas" -> reservasMostradas
        "Activas" -> reservasMostradas.filter { !it.isCancelada && !it.isLibre }
        "Canceladas" -> reservasMostradas.filter { it.isCancelada }
        "Finalizadas" -> reservasMostradas.filter { it.isLibre && !it.isCancelada }
        else -> reservasMostradas
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (currentRole == UserRole.ADMIN)
                            "Todas las Reservas"
                        else
                            "Mis Reservas"
                    )
                },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs para filtrar
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Contenido
            if (reservasFiltradas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.EventBusy,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay reservas",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(reservasFiltradas) { reserva ->
                        val habitacion = habitaciones.find { it.id == reserva.idHabitacionReservada }
                        ReservaCard(
                            reserva = reserva,
                            habitacion = habitacion,
                            currentRole = currentRole,
                            onCancelarClick = {
                                if (!reserva.isCancelada && !reserva.isLibre) {
                                    navController.navigate(Routes.CancelarReserva.createRoute(reserva.id))
                                }
                            },
                            onLiberarClick = {
                                if (currentRole == UserRole.ADMIN && !reserva.isLibre) {
                                    vmReserva.liberarReserva(reserva)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReservaCard(
    reserva: Reserva,
    habitacion: com.example.elhostal.domain.entities.Habitacion?,
    currentRole: UserRole,
    onCancelarClick: () -> Unit,
    onLiberarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                reserva.isCancelada -> Color(0xFFFFEBEE)
                reserva.isLibre -> Color(0xFFF5F5F5)
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Reserva #${reserva.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Habitación #${reserva.idHabitacionReservada}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = when {
                        reserva.isCancelada -> Color(0xFFF44336)
                        reserva.isLibre -> Color.Gray
                        else -> Color(0xFF4CAF50)
                    }
                ) {
                    Text(
                        text = when {
                            reserva.isCancelada -> "CANCELADA"
                            reserva.isLibre -> "FINALIZADA"
                            else -> "ACTIVA"
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (habitacion != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Bed,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tipo: ${habitacion.tipoCama}", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fecha: ${reserva.fecha}", fontSize = 14.sp)
            }

            // Botones de acción
            if (!reserva.isCancelada && !reserva.isLibre) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Botón cancelar (para usuario y admin)
                    OutlinedButton(
                        onClick = onCancelarClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFF44336)
                        )
                    ) {
                        Icon(Icons.Default.Cancel, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cancelar")
                    }

                    // Botón liberar (solo admin)
                    if (currentRole == UserRole.ADMIN) {
                        Button(
                            onClick = onLiberarClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Liberar")
                        }
                    }
                }
            }
        }
    }
}