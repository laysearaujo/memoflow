package com.memoflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MemoFlowApp()
                }
            }
        }
    }
}

// Modelo de dados simples (Como um dicionário em Python)
data class Estudo(val assunto: String, val proximaRevisao: String)

@Composable
fun MemoFlowApp() {
    var textoAssunto by remember { mutableStateOf("") }
    val listaEstudos = remember { mutableStateListOf<Estudo>() }

    Column(modifier = Modifier.padding(20.dp).fillMaxSize()) {
        Text("🧠 MemoFlow", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = textoAssunto,
            onValueChange = { textoAssunto = it },
            label = { Text("O que você estudou?") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (textoAssunto.isNotBlank()) {
                    // Lógica da Curva: Primeira revisão em 24h
                    val dataFormatada = LocalDateTime.now().plusDays(1)
                        .format(DateTimeFormatter.ofPattern("dd/MM - HH:mm"))

                    listaEstudos.add(Estudo(textoAssunto, dataFormatada))
                    textoAssunto = "" // Limpa o campo
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
        ) {
            Text("Agendar Revisão (24h)")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Cronograma de Revisões:", style = MaterialTheme.typography.titleMedium)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 8.dp)) {
            items(listaEstudos) { estudo ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = estudo.assunto, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "Revisar em: ${estudo.proximaRevisao}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
