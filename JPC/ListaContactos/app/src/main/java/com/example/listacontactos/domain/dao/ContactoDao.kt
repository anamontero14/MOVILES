package com.example.listacontactos.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.listacontactos.domain.entities.Contacto

@Dao
interface ContactoDao {
    @Query("SELECT * FROM contacto_entity")
    suspend fun getAllContactos(): MutableList<Contacto>

    @Insert
    suspend fun addContacto(contacto: Contacto): Long

    @Query("SELECT * FROM contacto_entity WHERE id LIKE :id")
    suspend fun getContactoById(id: Long): Contacto

    @Update
    suspend fun updateContacto(tarea: Contacto): Int

    @Delete
    suspend fun deleteContacto(tarea: Contacto): Int
}