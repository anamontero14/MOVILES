package com.example.elhostal.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.elhostal.domain.entities.Habitacion
import com.example.elhostal.ui.viewmodels.VMHabitacion

@Composable
fun VAñadirHabitacion(
    navController: NavHostController,
    viewmodelH: VMHabitacion
) {
    var tipoCama by remember { mutableStateOf("") }
    var bañoPrivado by remember { mutableStateOf(false) }
    var wifi by remember { mutableStateOf(false) }
    var ac by remember { mutableStateOf(false) }
    var serviciosExtra by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nueva Habitación",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo: Tipo de cama
        OutlinedTextField(
            value = tipoCama,
            onValueChange = { tipoCama = it },
            label = { Text("Tipo de Cama") },
            placeholder = { Text("Ej: Individual, Doble, Queen, King") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Switch: Baño Privado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Baño Privado",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "La habitación tiene baño propio",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = bañoPrivado,
                    onCheckedChange = { bañoPrivado = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Switch: WiFi
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WiFi",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Conexión a internet inalámbrica",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = wifi,
                    onCheckedChange = { wifi = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Switch: Aire Acondicionado
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Aire Acondicionado",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Sistema de climatización",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = ac,
                    onCheckedChange = { ac = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Switch: Servicios Extra
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Servicios Extra",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "TV, minibar, caja fuerte, etc.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = serviciosExtra,
                    onCheckedChange = { serviciosExtra = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Guardar
        Button(
            onClick = {
                if (tipoCama.isNotEmpty()) {
                    val nuevaHabitacion = Habitacion(
                        tipoCama = tipoCama,
                        bañoPrivado = bañoPrivado,
                        wifi = wifi,
                        ac = ac,
                        serviciosExtra = serviciosExtra
                    )
                    viewmodelH.addHabitacion(nuevaHabitacion)
                    mostrarDialogo = true
                } else {
                    mensaje = "El tipo de cama es obligatorio"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Guardar Habitación")
        }

        Button(
            onClick = {
                navController.navigate("V1ListaHabitaciones")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Lista de habitaciones")
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    // Diálogo de confirmación
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("¡Habitación Creada!") },
            text = { Text("La habitación se ha agregado exitosamente al sistema") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                    navController.popBackStack()
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
}