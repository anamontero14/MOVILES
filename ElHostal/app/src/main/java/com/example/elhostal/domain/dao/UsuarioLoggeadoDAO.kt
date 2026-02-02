package com.example.elhostal.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.elhostal.domain.entities.Habitacion
import com.example.elhostal.domain.entities.UsuarioLoggeado
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioLoggeadoDAO {
    @Query("SELECT * FROM usuario_loggeado_entity")
    fun getAllUsuariosLoggeados(): Flow<List<UsuarioLoggeado>>

    @Insert
    fun addUsuarioLoggeado(usuarioLoggeado: UsuarioLoggeado): Long

    @Query("SELECT * FROM usuario_loggeado_entity WHERE id LIKE :id")
    fun getUsuarioLoggeadoById(id: Long): UsuarioLoggeado

    @Update
    fun updateUsuarioLoggeado(usuarioLoggeado: UsuarioLoggeado): Int

    @Delete
    fun deleteUsuarioLoggeado(usuarioLoggeado: UsuarioLoggeado): Int
}