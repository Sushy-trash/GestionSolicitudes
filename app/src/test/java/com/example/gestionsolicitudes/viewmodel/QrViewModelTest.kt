package com.example.gestionesolicitudes.viewmodel

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.example.gestionsolicitudes.data.model.QrResult
import com.example.gestionsolicitudes.viewmodel.QrViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class QrViewModelTest : FunSpec({

    beforeSpec {
        ArchTaskExecutor.getInstance().setDelegate(
            object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) {
                    runnable.run()
                }

                override fun postToMainThread(runnable: Runnable) {
                    runnable.run()
                }

                override fun isMainThread(): Boolean = true
            }
        )
    }

    afterSpec {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

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