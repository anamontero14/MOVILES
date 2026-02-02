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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.elhostal.Routes
import com.example.elhostal.domain.roles.UserRole
import com.example.elhostal.ui.viewmodels.RegisterState
import com.example.elhostal.ui.viewmodels.VMAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VRegister(
    navController: NavHostController,
    vmAuth: VMAuth
) {
    var nombre by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var confirmarContraseña by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val registerState by vmAuth.registerState.collectAsState()
    val usuarios by vmAuth.listaUsuarios.collectAsState()

    // Navegar al login cuando el registro sea exitoso
    LaunchedEffect(registerState) {
        if (registerState is RegisterState.Success) {
            navController.navigate(Routes.Login.route) {
                popUpTo(Routes.Register.route) { inclusive = true }
            }
            vmAuth.resetRegisterState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono de registro
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crear Cuenta",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Regístrate para reservar habitaciones",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de nombre de usuario
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de usuario") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de contraseña
            OutlinedTextField(
                value = contraseña,
                onValueChange = { contraseña = it },
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de confirmar contraseña
            OutlinedTextField(
                value = confirmarContraseña,
                onValueChange = { confirmarContraseña = it },
                label = { Text("Confirmar contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = confirmarContraseña.isNotBlank() && contraseña != confirmarContraseña
            )

            if (confirmarContraseña.isNotBlank() && contraseña != confirmarContraseña) {
                Text(
                    text = "Las contraseñas no coinciden",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar mensajes de error del registro
            when (registerState) {
                is RegisterState.Error -> {
                    Text(
                        text = (registerState as RegisterState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de registro
            Button(
                onClick = {
                    if (contraseña == confirmarContraseña) {
                        vmAuth.register(nombre, contraseña, UserRole.USER)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = nombre.isNotBlank() &&
                        contraseña.isNotBlank() &&
                        confirmarContraseña.isNotBlank() &&
                        contraseña == confirmarContraseña
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrarse", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para ir a login
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Ya tienes cuenta?", fontSize = 14.sp)
                TextButton(
                    onClick = {
                        vmAuth.resetRegisterState()
                        navController.navigate(Routes.Login.route) {
                            popUpTo(Routes.Register.route) { inclusive = true }
                        }
                    }
                ) {
                    Text("Inicia sesión aquí", fontSize = 14.sp)
                }
            }
        }
    }
}