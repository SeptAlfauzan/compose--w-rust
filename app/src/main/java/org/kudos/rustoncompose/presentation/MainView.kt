package org.kudos.rustoncompose.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kudos.rustoncompose.domain.Calculation
import org.kudos.rustoncompose.utils.UiState

const val TAG = "MainView"

@Composable
fun MainView(
    calculateWithRust: (ULong) -> Unit,
    calculateWithKotlin: (ULong) -> Unit,
    cancelCalculation: () -> Unit,
    uiState: UiState<Calculation<Long>>
) {
    var number by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            uiState.let { state ->
                when(state){
                    is UiState.Error -> Text("Error: ${state.message}")
                    is UiState.Loading -> CircularProgressIndicator()
                    is UiState.Success -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Result ${state.data.value}")
                        Text("Time Execution ${state.data.executionTime}ms")
                    }
                }
            }

            TextField(
                value = number.toString(),
                onValueChange = { number = it.toIntOrNull() ?: 0 },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Button(
                    enabled = uiState !is UiState.Loading,
                    onClick = {
                    coroutineScope.launch(Dispatchers.IO) { calculateWithRust(number.toULong()) }
                }) {
                    Text("Calc /w Rust")
                }
                Button(
                    enabled = uiState !is UiState.Loading,
                    onClick = {
                    coroutineScope.launch(Dispatchers.IO) { calculateWithKotlin(number.toULong()) }
                }) {
                    Text("Calc /w Kotlin")
                }
            }
            Button(onClick = cancelCalculation) {
                Text("Cancel Calculation")
            }
        }
    }
}