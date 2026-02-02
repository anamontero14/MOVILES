package com.example.elhostal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.elhostal.domain.dao.UsuarioLoggeadoDAO
import com.example.elhostal.domain.entities.UsuarioLoggeado

@Database(entities = [UsuarioLoggeado::class], version = 1)
abstract class UsuarioLoggeadoDatabase : RoomDatabase() {
    abstract fun usuarioLoggeadoDAO(): UsuarioLoggeadoDAO
}