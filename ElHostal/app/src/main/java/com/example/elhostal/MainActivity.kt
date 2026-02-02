package com.example.elhostal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.elhostal.data.database.HabitacionesDatabase
import com.example.elhostal.data.database.ReservasDatabase
import com.example.elhostal.data.database.UsuarioLoggeadoDatabase
import com.example.elhostal.data.repositories.RepositoryHabitaciones
import com.example.elhostal.data.repositories.RepositoryReservas
import com.example.elhostal.data.repositories.RepositoryUsuarioLoggeado
import com.example.elhostal.ui.factory.AuthFactory
import com.example.elhostal.ui.factory.HabitacionFactory
import com.example.elhostal.ui.factory.ReservaFactory
import com.example.elhostal.ui.theme.ElHostalTheme
import com.example.elhostal.ui.viewmodels.VMAuth
import com.example.elhostal.ui.viewmodels.VMHabitacion
import com.example.elhostal.ui.viewmodels.VMReserva
import com.example.elhostal.ui.views.*

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var databaseH: HabitacionesDatabase
        lateinit var databaseR: ReservasDatabase
        lateinit var databaseU: UsuarioLoggeadoDatabase
    }

    private val repositoryHabitaciones by lazy {
        RepositoryHabitaciones(databaseH.habitacionDAO())
    }
    private val repositoryReservas by lazy {
        RepositoryReservas(databaseR.reservaDao())
    }
    private val repositoryUsuarioLoggeado by lazy {
        RepositoryUsuarioLoggeado(databaseU.usuarioLoggeadoDAO())
    }

    private val vmHabitacion: VMHabitacion by viewModels {
        HabitacionFactory(repositoryHabitaciones, repositoryReservas)
    }

    private val vmReserva: VMReserva by viewModels {
        ReservaFactory(repositoryReservas)
    }

    private val vmAuth: VMAuth by viewModels {
        AuthFactory(repositoryUsuarioLoggeado)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseH = Room.databaseBuilder(
            applicationContext,
            HabitacionesDatabase::class.java,
            "HabitacionesDatabase"
        ).build()

        databaseR = Room.databaseBuilder(
            applicationContext,
            ReservasDatabase::class.java,
            "ReservasDatabase"
        ).build()

        databaseU = Room.databaseBuilder(
            applicationContext,
            UsuarioLoggeadoDatabase::class.java,
            "UsuarioLoggeadoDatabase"
        ).build()

        enableEdgeToEdge()
        setContent {
            ElHostalTheme {
                AppNavigation(
                    vmHabitacion = vmHabitacion,
                    vmReserva = vmReserva,
                    vmAuth = vmAuth
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    vmHabitacion: VMHabitacion,
    vmReserva: VMReserva,
    vmAuth: VMAuth
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ListaHabitaciones.route
    ) {
        composable(Routes.ListaHabitaciones.route) {
            V1ListaHabitaciones(
                navController = navController,
                vmHabitacion = vmHabitacion,
                vmAuth = vmAuth
            )
        }

        composable(Routes.Login.route) {
            VLogin(
                navController = navController,
                vmAuth = vmAuth
            )
        }

        composable(Routes.Register.route) {
            VRegister(
                navController = navController,
                vmAuth = vmAuth
            )
        }

        composable(
            route = Routes.Habitacion.route,
            arguments = listOf(navArgument("idHabitacion") { type = NavType.IntType })
        ) { backStackEntry ->
            val idHabitacion = backStackEntry.arguments?.getInt("idHabitacion") ?: 0
            V2Habitacion(
                navController = navController,
                vmHabitacion = vmHabitacion,
                vmAuth = vmAuth,
                idHabitacion = idHabitacion
            )
        }

        composable(
            route = Routes.Reserva.route,
            arguments = listOf(navArgument("idHabitacion") { type = NavType.IntType })
        ) { backStackEntry ->
            val idHabitacion = backStackEntry.arguments?.getInt("idHabitacion") ?: 0
            V3Reserva(
                navController = navController,
                vmReserva = vmReserva,
                vmAuth = vmAuth,
                vmHabitacion = vmHabitacion,
                idHabitacion = idHabitacion
            )
        }

        composable(Routes.ListaReservas.route) {
            V4ListaReservas(
                navController = navController,
                vmReserva = vmReserva,
                vmAuth = vmAuth,
                vmHabitacion = vmHabitacion
            )
        }

        composable(
            route = Routes.CancelarReserva.route,
            arguments = listOf(navArgument("idReserva") { type = NavType.IntType })
        ) { backStackEntry ->
            val idReserva = backStackEntry.arguments?.getInt("idReserva") ?: 0
            V5CancelarReserva(
                navController = navController,
                vmReserva = vmReserva,
                idReserva = idReserva
            )
        }

        composable(Routes.AñadirHabitacion.route) {
            VAñadirHabitacion(
                navController = navController,
                vmHabitacion = vmHabitacion,
                vmAuth = vmAuth
            )
        }
    }
}

sealed class Routes(val route: String) {
    object ListaHabitaciones : Routes("lista_habitaciones")
    object Login : Routes("login")
    object Register : Routes("register")

    object Habitacion : Routes("habitacion/{idHabitacion}") {
        fun createRoute(idHabitacion: Int) = "habitacion/$idHabitacion"
    }

    object Reserva : Routes("reserva/{idHabitacion}") {
        fun createRoute(idHabitacion: Int) = "reserva/$idHabitacion"
    }

    object ListaReservas : Routes("lista_reservas")

    object CancelarReserva : Routes("cancelar_reserva/{idReserva}") {
        fun createRoute(idReserva: Int) = "cancelar_reserva/$idReserva"
    }

    object AñadirHabitacion : Routes("añadir_habitacion")
}