package com.example.elhostal.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.elhostal.Routes
import com.example.elhostal.domain.roles.UserRole
import com.example.elhostal.ui.viewmodels.AddHabitacionState
import com.example.elhostal.ui.viewmodels.VMAuth
import com.example.elhostal.ui.viewmodels.VMHabitacion
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VAñadirHabitacion(
    navController: NavHostController,
    vmHabitacion: VMHabitacion,
    vmAuth: VMAuth
) {
    val currentRole = vmAuth.getCurrentUserRole()
    val addHabitacionState by vmHabitacion.addHabitacionState.collectAsState()

    // Verificar que el usuario sea admin
    if (currentRole != UserRole.ADMIN) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    var tipoCama by remember { mutableStateOf("") }
    var bañoPrivado by remember { mutableStateOf(false) }
    var wifi by remember { mutableStateOf(false) }
    var ac by remember { mutableStateOf(false) }
    var serviciosExtra by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Navegar de vuelta cuando se añada correctamente
    LaunchedEffect(addHabitacionState) {
        if (addHabitacionState is AddHabitacionState.Success) {
            scope.launch {
                snackbarHostState.showSnackbar("Habitación añadida correctamente")
            }
            navController.navigate(Routes.ListaHabitaciones.route) {
                popUpTo(Routes.ListaHabitaciones.route) { inclusive = true }
            }
            vmHabitacion.resetAddHabitacionState()
        } else if (addHabitacionState is AddHabitacionState.Error) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    (addHabitacionState as AddHabitacionState.Error).message
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Habitación") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Modo Administrador",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Añade nuevas habitaciones al hostal",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Información de la habitación",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de tipo de cama
            OutlinedTextField(
                value = tipoCama,
                onValueChange = { tipoCama = it },
                label = { Text("Tipo de cama") },
                placeholder = { Text("Ej: Individual, Doble, Queen, King...") },
                leadingIcon = {
                    Icon(Icons.Default.Bed, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Servicios y comodidades",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Switches para los servicios
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ServiceSwitch(
                        icon = Icons.Default.Bathroom,
                        label = "Baño privado",
                        checked = bañoPrivado,
                        onCheckedChange = { bañoPrivado = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    ServiceSwitch(
                        icon = Icons.Default.Wifi,
                        label = "WiFi",
                        checked = wifi,
                        onCheckedChange = { wifi = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    ServiceSwitch(
                        icon = Icons.Default.AcUnit,
                        label = "Aire acondicionado",
                        checked = ac,
                        onCheckedChange = { ac = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    ServiceSwitch(
                        icon = Icons.Default.RoomService,
                        label = "Servicios extra",
                        checked = serviciosExtra,
                        onCheckedChange = { serviciosExtra = it }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón de añadir
            Button(
                onClick = {
                    vmHabitacion.addHabitacion(
                        tipoCama = tipoCama,
                        bañoPrivado = bañoPrivado,
                        wifi = wifi,
                        ac = ac,
                        serviciosExtra = serviciosExtra
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = tipoCama.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Añadir Habitación", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun ServiceSwitch(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (checked) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                color = if (checked) Color.Black else Color.Gray
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}