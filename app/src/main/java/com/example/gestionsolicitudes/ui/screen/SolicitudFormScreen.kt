package com.example.gestionsolicitudes.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestionsolicitudes.viewmodel.SolicitudUiState

private val categoriasSolicitud = listOf(
    "Infraestructura",
    "Aseo",
    "Iluminación",
    "Seguridad",
    "Otro"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudFormScreen(
    uiState: SolicitudUiState,
    onTituloChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCategoriaChange: (String) -> Unit,
    onServicioChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onVolver: () -> Unit
) {
    var categoriaMenuExpandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.solicitudEditandoId == null) {
                            "Nueva solicitud"
                        } else {
                            "Editar solicitud"
                        }
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Completa los datos de tu solicitud.",
                style = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = onTituloChange,
                label = { Text("Título *") },
                isError = uiState.tituloError != null,
                supportingText = {
                    uiState.tituloError?.let { Text(it) }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = onDescripcionChange,
                label = { Text("Descripción *") },
                isError = uiState.descripcionError != null,
                supportingText = {
                    uiState.descripcionError?.let { Text(it) }
                },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                OutlinedButton(
                    onClick = {
                        categoriaMenuExpandido = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (uiState.categoria.isBlank()) {
                            "Seleccionar categoría *"
                        } else {
                            "Categoría: ${uiState.categoria}"
                        }
                    )
                }

                DropdownMenu(
                    expanded = categoriaMenuExpandido,
                    onDismissRequest = {
                        categoriaMenuExpandido = false
                    }
                ) {
                    categoriasSolicitud.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria) },
                            onClick = {
                                onCategoriaChange(categoria)
                                categoriaMenuExpandido = false
                            }
                        )
                    }
                }

                if (uiState.categoriaError != null) {
                    Text(
                        text = uiState.categoriaError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            OutlinedTextField(
                value = uiState.servicio,
                onValueChange = onServicioChange,
                label = { Text("Servicio relacionado (opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.mensajeError != null) {
                Text(
                    text = uiState.mensajeError,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (uiState.guardadoExitosamente) {
                Text(
                    text = "¡Solicitud guardada correctamente!",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onGuardar,
                enabled = !uiState.guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.guardando) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        if (uiState.solicitudEditandoId == null) {
                            "Guardar solicitud"
                        } else {
                            "Guardar cambios"
                        }
                    )
                }
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}