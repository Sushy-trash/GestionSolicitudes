package com.example.gestionsolicitudes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestionsolicitudes.ui.screen.ListaSolicitudesScreen
import com.example.gestionsolicitudes.ui.screen.SolicitudFormScreen
import com.example.gestionsolicitudes.viewmodel.SolicitudViewModel
import com.example.gestionsolicitudes.ui.view.QrScannerScreen
import com.example.gestionsolicitudes.viewmodel.QrViewModel
import com.example.gestionsolicitudes.ui.screen.PostScreen
import com.example.gestionsolicitudes.viewmodel.PostViewModel

@Composable
fun AppNavigation(
    viewModel: SolicitudViewModel,
    qrViewModel: QrViewModel,
    postViewModel: PostViewModel,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    val postUiState by postViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.guardadoExitosamente) {
        if (uiState.guardadoExitosamente) {
            navController.popBackStack()
            viewModel.limpiarMensaje()
        }
    }

    NavHost(
        navController = navController,
        startDestination = "lista_solicitudes"
    ) {
        composable("lista_solicitudes") {
            ListaSolicitudesScreen(
                uiState = uiState,
                onNuevaSolicitud = {
                    viewModel.prepararNuevaSolicitud()
                    navController.navigate("formulario_solicitud")
                },
                onSeleccionarSolicitud = { solicitud ->
                    viewModel.cargarSolicitud(solicitud.id)
                    navController.navigate("formulario_solicitud")
                },
                onEliminarSolicitud = { id ->
                    viewModel.eliminarSolicitud(id)
                }
            )
        }

        composable("formulario_solicitud") {
            SolicitudFormScreen(
                uiState = uiState,
                onTituloChange = viewModel::actualizarTitulo,
                onDescripcionChange = viewModel::actualizarDescripcion,
                onCategoriaChange = viewModel::actualizarCategoria,
                onServicioChange = viewModel::actualizarServicio,
                onGuardar = viewModel::guardarSolicitud,
                onEscanearQr = {
                    navController.navigate("escanner_qr")
                },
                onVolver = {
                    viewModel.limpiarMensaje()
                    navController.popBackStack()
                }

            )
        }
        composable("escanner_qr") {
            QrScannerScreen(
                viewModel = qrViewModel,
                hasCameraPermission = hasCameraPermission,
                onRequestPermission = onRequestPermission,
                onQrDetectado = { codigo ->
                    viewModel.consultarSolicitudPorCodigo(codigo)
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
        composable("publicaciones") {
            PostScreen(
                uiState = postUiState,
                onReintentar = postViewModel::cargarPosts
            )
        }
    }
}