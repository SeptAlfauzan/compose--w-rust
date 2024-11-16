package org.kudos.rustoncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import org.kudos.rustoncompose.presentation.MainView
import org.kudos.rustoncompose.presentation.MainViewModel
import org.kudos.rustoncompose.ui.theme.RustOnComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewmodel by viewModels<MainViewModel>()
            RustOnComposeTheme {
                MainView(
                    calculateWithRust = { num -> viewmodel.calculateWithRs(num) },
                    calculateWithKotlin = { num -> viewmodel.calculateWithKt(num) },
                    cancelCalculation = { viewmodel.cancelCalculation() },
                    uiState = viewmodel.calculation.collectAsState().value
                )
            }
        }
    }
}
