package com.example.elhostal.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reserva_entity")
class Reserva (
    //id de la reserva
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    //almacena el id de la habitación que se ha reservado
    val idHabitacionReservada: Int,
    //id del usuario que ha reservado la habitacion
    val idUsuario: Int,
    val fecha: String,
    //variable que indicará si la reserva está activa o no, por defecto
    //estará a true y si el admin la vacía o el usuario la cancela se
    //pone a false
    var isLibre: Boolean = true,
    var isCancelada: Boolean = false
)