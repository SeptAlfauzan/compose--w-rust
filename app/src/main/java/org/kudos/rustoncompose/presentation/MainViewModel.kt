package org.kudos.rustoncompose.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.kudos.rustoncompose.domain.Calculation
import org.kudos.rustoncompose.utils.ExpensiveCalculation
import org.kudos.rustoncompose.utils.UiState

class MainViewModel : ViewModel() {
    private val _calculation: MutableStateFlow<UiState<Calculation<Long>>> =
        MutableStateFlow(UiState.Success(Calculation(0L, null)))
    val calculation: StateFlow<UiState<Calculation<Long>>> = _calculation
    private var _coroutineJob: Job? = null

    fun cancelCalculation(){
        _calculation.value = UiState.Success(Calculation(value = 0, executionTime = null))
        _coroutineJob?.cancel()
    }

    fun calculateWithKt(intensity: ULong) {
        _coroutineJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                _calculation.value = UiState.Loading
                var data: ULong? = null
                val time = measureTimeElapses {
                    data = ExpensiveCalculation.expensiveCalculationKt(intensity)
                }
                _calculation.value = UiState.Success(Calculation((data ?: 0UL).toLong(), time))
            } catch (e: Exception) {
                _calculation.value = UiState.Error(e.message ?: "Error")
            }
        }
    }
    fun calculateWithRs(intensity: ULong) {
        _coroutineJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                _calculation.value = UiState.Loading
                var data: ULong? = null
                val time = measureTimeElapses {
                    data = ExpensiveCalculation.expensiveCalculationRs(intensity)
                }
                _calculation.value = UiState.Success(Calculation((data ?: 0UL).toLong(), time))
            } catch (e: Exception) {
                _calculation.value = UiState.Error(e.message ?: "Error")
            }
        }
    }

    private fun measureTimeElapses(func: () -> Unit): Long {
        val startTime = System.currentTimeMillis()
        func()
        val endTime = System.currentTimeMillis()
        Log.d(TAG, "measureTimeElapses: ${endTime - startTime}")
        return endTime - startTime
    }
}