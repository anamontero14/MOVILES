package com.example.elhostal.ui.views

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.elhostal.ui.viewmodels.VMAuth

@Composable
fun VLogin(
    navController: NavHostController,
    viewmodelAuth: VMAuth
) {
    var nombre by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // Observar el resultado del login
    val loginResult by viewmodelAuth.loginResult.collectAsState()

    // Efecto que se ejecuta cuando cambia loginResult
    LaunchedEffect(loginResult) {
        if (loginResult != null) {
            navController.navigate("V1ListaHabitaciones")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contraseña,
            onValueChange = { contraseña = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nombre.isNotEmpty() && contraseña.isNotEmpty()) {
                    viewmodelAuth.login(nombre, contraseña)
                    Log.d("ETIQUETA","Es admin y se mete")
                    // Esperar a que loginResult cambie
                    mensaje = "" // Limpiar mensaje anterior
                } else {
                    mensaje = "Completa todos los campos"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? Regístrate")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            viewmodelAuth.entrarComoInvitado()
            navController.navigate("V1ListaHabitaciones")
        }) {
            Text("Continuar como invitado")
        }

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = mensaje, color = MaterialTheme.colorScheme.error)
        }
    }
}