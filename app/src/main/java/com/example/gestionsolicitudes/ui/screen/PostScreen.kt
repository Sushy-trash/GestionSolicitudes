
package com.example.gestionsolicitudes.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    uiState: PostUiState,
    onReintentar: () -> Unit,
    onVolver: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Publicaciones",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->

        when {
            uiState.cargando -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Cargando publicaciones...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            uiState.mensajeError != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No pudimos cargar las publicaciones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = uiState.mensajeError.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = onReintentar) {
                        Text("Intentar nuevamente")
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(padding),
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier.padding(
                                bottom = 8.dp
                            )
                        ) {
                            Text(
                                text = "Contenido externo",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Explora las publicaciones",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Consulta el contenido obtenido desde nuestra API externa.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = 16.dp,
                                            vertical = 12.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${uiState.posts.size}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )

                                    Spacer(modifier = Modifier.padding(horizontal = 6.dp))

                                    Text(
                                        text = if (uiState.posts.size == 1)
                                            "publicación disponible"
                                        else
                                            "publicaciones disponibles",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }

                    if (uiState.posts.isEmpty()) {
                        item {
                            Text(
                                text = "No hay publicaciones disponibles.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    items(
                        items = uiState.posts,
                        key = { post -> post.id }
                    ) { post ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 1.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = CircleShape
                                    ) {
                                        Text(
                                            text = "#${post.id}",
                                            modifier = Modifier.padding(
                                                horizontal = 12.dp,
                                                vertical = 6.dp
                                            ),
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = "PUBLICACIÓN",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = post.title.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase()
                                        else it.toString()
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = post.body.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase()
                                        else it.toString()
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

