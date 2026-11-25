package com.example.gestionsolicitudes.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestionsolicitudes.model.SolicitudEntity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaSolicitudesScreen(
    uiState: SolicitudUiState,
    onNuevaSolicitud: () -> Unit,
    onSeleccionarSolicitud: (SolicitudEntity) -> Unit,
    onEliminarSolicitud: (Long) -> Unit,
    onVerPublicaciones: () -> Unit
) {
    var solicitudAEliminar by remember {
        mutableStateOf<SolicitudEntity?>(null)
    }

    solicitudAEliminar?.let { solicitud ->

        AlertDialog(
            onDismissRequest = {
                solicitudAEliminar = null
            },
            title = {
                Text("Eliminar solicitud")
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar " +
                            "\"${solicitud.titulo}\"? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEliminarSolicitud(solicitud.id)
                        solicitudAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        solicitudAEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mis solicitudes")
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = onNuevaSolicitud
            ) {
                Text("+ Nueva solicitud")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Solicitudes registradas: ${uiState.solicitudes.size}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.padding(4.dp))
            Button(
                onClick = onVerPublicaciones,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver publicaciones externas")
            }

            if (uiState.solicitudes.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Todavía no tienes solicitudes.",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Pulsa «Nueva solicitud» para registrar la primera."
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.solicitudes,
                        key = { solicitud -> solicitud.id }
                    ) { solicitud ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSeleccionarSolicitud(solicitud)
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = solicitud.titulo,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "Código: ${solicitud.codigo}"
                                )

                                Text(
                                    text = "Categoría: ${solicitud.categoria}"
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Estado: ")

                                    Text(
                                        text = solicitud.estado,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }

                                Text(
                                    text = "Toca para consultar o editar",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Button(
                                    onClick = {
                                        solicitudAEliminar = solicitud
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Eliminar solicitud")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}