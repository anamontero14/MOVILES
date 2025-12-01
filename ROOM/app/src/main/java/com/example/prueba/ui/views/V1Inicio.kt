package com.example.prueba.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.prueba.MainActivity
import com.example.prueba.MainActivity.Companion.database
import com.example.prueba.domain.entities.Tarea
import com.example.prueba.ui.viewmodels.VMTareas
import kotlinx.coroutines.launch

@Composable
fun ListaTareas(
    navController: NavHostController,
    viewmodel: VMTareas
) {
    var texto by remember { mutableStateOf("") }
    var listaTareas = remember { mutableStateListOf<Tarea>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        listaTareas.clear()
        listaTareas.addAll(
            MainActivity.database.taskDao().getAllTareas()
        )
    }

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            NuevaTarea(
                texto = texto,
                onTextoChange = { texto = it }
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        val nuevaTarea = Tarea(nombreTarea = "${texto}")
                        MainActivity.database.taskDao().addTarea(nuevaTarea)
                        //Log.d(":::tag", nuevaTarea.id.toString())
                    }
                    navController.navigate("V1Inicio")
                }
            ) {
                Text("+")
            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            items(listaTareas) { item ->
                Row(modifier = Modifier.padding(10.dp)) {
                    CheckTareas(item)
                }
            }
        }
    }
}

@Composable
fun NuevaTarea(texto: String, onTextoChange: (String) -> Unit) {
    OutlinedTextField(
        value = texto,
        onValueChange = onTextoChange,
        label = { Text("Introduce una nueva tarea") }
    )
}

@Composable
fun CheckTareas(item: Tarea) {
    var checked by remember { mutableStateOf(item.estadoTarea) }

    val background = if (checked) {
        androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
    } else {
        androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
    }

    val alpha = if (checked) 0.6f else 1f

    androidx.compose.material3.Surface(
        tonalElevation = 4.dp,
        shadowElevation = 6.dp,
        shape = androidx.compose.material3.MaterialTheme.shapes.medium,
        color = background,
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    item.estadoTarea = it
                }
            )

            Text(
                text = item.nombreTarea,
                modifier = Modifier.padding(start = 10.dp),
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)
            )
        }
    }
}