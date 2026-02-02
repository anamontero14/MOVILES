package com.example.elhostal.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.elhostal.domain.entities.Reserva
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDAO {

    @Query("SELECT * FROM reserva_entity")
    fun getAllReservas(): Flow<List<Reserva>>

    //add
    @Insert
    fun addReserva(reserva: Reserva): Long

    //cancel update
    @Update
    fun cancelReserva(reserva: Reserva): Int

    //liberar
    @Update
    fun liberarReserva(reserva: Reserva): Int

    // En ReservaDAO.kt
    @Query("SELECT * FROM reserva_entity WHERE idUsuario = :idUsuario")
    fun getReservasByUsuario(idUsuario: Int): Flow<List<Reserva>>

    //get ultima reserva por el id de la reserva
    @Query("""
        SELECT * FROM reserva_entity 
        WHERE idHabitacionReservada = :idHabitacion 
        AND isLibre = 0 
        AND isCancelada = 0
        ORDER BY id DESC 
        LIMIT 1
    """)
    fun getUltimaReservaOcupadaByIDHabitacion(idHabitacion: Int): Reserva?
}