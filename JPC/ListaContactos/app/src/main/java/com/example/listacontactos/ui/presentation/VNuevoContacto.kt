package com.example.listacontactos.ui.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.listacontactos.MainActivity
import com.example.listacontactos.domain.entities.Contacto
import kotlinx.coroutines.launch

@Composable
fun NuevoContacto(
    navController: NavHostController
) {
    var nombre by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del contacto") },
            modifier = Modifier.padding(10.dp)
        )
        OutlinedTextField(
            value = numero,
            onValueChange = { numero = it },
            label = { Text("Número de teléfono") },
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = "Género",
            modifier = Modifier.padding(10.dp)
        )

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            RadioButton(
                selected = genero == "Masculino",
                onClick = { genero = "Masculino" }
            )
            Text("Masculino", modifier = Modifier.padding(start = 8.dp))
        }

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            RadioButton(
                selected = genero == "Femenino",
                onClick = { genero = "Femenino" }
            )
            Text("Femenino", modifier = Modifier.padding(start = 8.dp))
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    val nuevoContacto = Contacto(
                        id = 0,
                        name = nombre,
                        phoneNumber = numero,
                        genero = genero
                    )
                    MainActivity.database.contactosDao().addContacto(nuevoContacto)
                }
                navController.navigate("VListaContactos")
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Text("Guardar Contacto")
        }
    }
}