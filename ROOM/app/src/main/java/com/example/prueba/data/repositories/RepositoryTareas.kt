package com.example.prueba.data.repositories

import com.example.prueba.domain.entities.Tarea

object RepositoryTareas {

    //lista de todas las tareas
    private val _tareasList = mutableListOf<Tarea>();

    //devuelve todas las tareas
    fun getAllTareas(): List<Tarea> {
        return _tareasList;
    }
}