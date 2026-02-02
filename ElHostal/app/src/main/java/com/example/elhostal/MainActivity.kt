package com.example.elhostal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
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
import com.example.elhostal.domain.entities.UsuarioLoggeado
import com.example.elhostal.domain.roles.UserRole
import com.example.elhostal.ui.factory.AuthFactory
import com.example.elhostal.ui.factory.HabitacionFactory
import com.example.elhostal.ui.factory.ReservaFactory
import com.example.elhostal.ui.theme.ElHostalTheme
import com.example.elhostal.ui.viewmodels.VMAuth
import com.example.elhostal.ui.viewmodels.VMHabitacion
import com.example.elhostal.ui.viewmodels.VMReserva
import com.example.elhostal.ui.views.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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

        // CREAR LAS BASES DE DATOS PRIMERO
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
        )
            .fallbackToDestructiveMigration()  // AGREGAR ESTA LÍNEA
            .build()

        // AHORA SÍ CREAR EL ADMIN
        crearAdminInicial()

        enableEdgeToEdge()
        setContent {
            ElHostalTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "V1ListaHabitaciones"
                ) {
                    composable("V1ListaHabitaciones") {
                        ListaHabitaciones(
                            navController,
                            vmHabitacion
                        )
                    }
                    composable("V2Habitacion/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0

                        V2Habitacion(
                            navController,
                            vmHabitacion,
                            vmReserva,
                            id
                        )
                    }
                    composable("V3Reserva/{id}/{idUsuario}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                        val idUsuario = backStackEntry.arguments?.getString("idUsuario")?.toIntOrNull() ?: 0

                        V3Reserva(
                            navController,
                            vmHabitacion,
                            vmReserva,
                            id,
                            idUsuario
                        )

                    }

                    composable("V4ListaReservas") {
                        V4ListaReservas(
                            navController,
                            vmHabitacion,
                            vmReserva
                        )
                    }

                    composable("VRegister") {
                        VRegister(
                            navController,
                            vmAuth
                        )
                    }
                    composable("VLogin") {
                        VLogin(
                            navController,
                            vmAuth
                        )
                    }
                    composable ("VAñadirHabitacion"){
                        VAñadirHabitacion(
                            navController,
                            vmHabitacion
                        )
                    }
                }
            }
        }
    }

    //funcion que crea un usuario administrador justo al iniciar la aplicacion
    private fun crearAdminInicial() {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuarios = databaseU.usuarioLoggeadoDAO().getAllUsuariosLoggeados().first()

            // Si no hay ningún admin, crear uno
            val hayAdmin = usuarios.any { it.role == UserRole.ADMIN }

            if (!hayAdmin) {
                val admin = UsuarioLoggeado(
                    nombre = "admin",
                    contraseña = "admin",
                    role = UserRole.ADMIN
                )
                databaseU.usuarioLoggeadoDAO().addUsuarioLoggeado(admin)
            }
        }
    }
}