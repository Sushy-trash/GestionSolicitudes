
package com.example.gestionesolicitudes.viewmodel

import com.example.gestionsolicitudes.data.model.QrResult
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import com.example.gestionsolicitudes.viewmodel.QrViewModel

class QrViewModelTest : FunSpec({

    test("Debe procesar correctamente un QR") {
        val viewModel = QrViewModel()

        viewModel.onQrDetected("SOLICITUD-123")

        viewModel.qrResult.value shouldBe QrResult("SOLICITUD-123")
    }

    test("Debe conservar el contenido recibido del QR") {
        val viewModel = QrViewModel()
        val contenido = "https://ejemplo.cl/solicitud/45"

        viewModel.onQrDetected(contenido)

        viewModel.qrResult.value?.content shouldBe contenido
    }

    test("Debe limpiar el resultado del QR") {
        val viewModel = QrViewModel()

        viewModel.onQrDetected("SOLICITUD-123")
        viewModel.clearResult()

        viewModel.qrResult.value shouldBe null
    }
})
