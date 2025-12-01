package com.example.prueba.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.listacontactos.domain.dao.ContactoDao
import com.example.listacontactos.domain.entities.Contacto

@Database(entities = [Contacto::class], version = 1)
abstract class ContactosDatabase : RoomDatabase() {
    abstract fun contactosDao(): ContactoDao
}