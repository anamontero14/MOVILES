package com.example.boletin1.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.boletin1.ui.ui.theme.Boletin1Theme

class Ejercicio8 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Boletin1Theme {

            }
        }
    }
}

@Composable
fun CambiarModo() {

    

}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Boletin1Theme {
        Greeting("Android")
    }
}*/