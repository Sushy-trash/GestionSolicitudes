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

@Composable
fun AppNavigation(
    viewModel: SolicitudViewModel
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

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
                    viewModel.limpiarMensaje()
                    navController.navigate("formulario_solicitud")
                },
                onSeleccionarSolicitud = { solicitud ->
                    // La edición se conectará cuando implementemos
                    // la carga de una solicitud existente en el ViewModel.
                    navController.navigate("formulario_solicitud")
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
                onVolver = {
                    viewModel.limpiarMensaje()
                    navController.popBackStack()
                }
            )
        }
    }
}