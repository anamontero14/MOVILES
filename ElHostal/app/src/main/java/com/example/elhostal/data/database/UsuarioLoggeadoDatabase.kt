package com.example.elhostal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.elhostal.data.converter.UserRoleConverter
import com.example.elhostal.domain.dao.UsuarioLoggeadoDAO
import com.example.elhostal.domain.entities.UsuarioLoggeado

@Database(entities = [UsuarioLoggeado::class], version = 2)
@TypeConverters(UserRoleConverter::class)
abstract class UsuarioLoggeadoDatabase : RoomDatabase() {
    abstract fun usuarioLoggeadoDAO(): UsuarioLoggeadoDAO
}