package com.example.prueba.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.prueba.data.repositories.RepositoryTareas
import com.example.prueba.domain.entities.Tarea

class VMTareas : ViewModel() {

    //repositorio
    private val _repositorioTareas = RepositoryTareas

    //almacena una lista de las tareas
    private val _tareas = mutableStateOf<List<Tarea>>(emptyList())

    //variable de tipo lista de tareas que almacena lo que haya en tareas
    val tareas : State<List<Tarea>> get() = _tareas

    //constructor
    init {
        loadTareas()
    }

    //funcion que se encarga de igualar el valor de los destinos a todos ellos
    private fun loadTareas() {
        _tareas.value = _repositorioTareas.getAllTareas()
    }

    fun getListadoTareas(): List<Tarea> {
        return _repositorioTareas.getAllTareas()
    }
}