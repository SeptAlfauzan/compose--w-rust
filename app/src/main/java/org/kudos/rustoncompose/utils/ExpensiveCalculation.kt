package org.kudos.rustoncompose.utils

import uniffi.sample.expensiveCalculation

object ExpensiveCalculation {

    fun expensiveCalculationRs(intensity: ULong) = expensiveCalculation(intensity)
    fun expensiveCalculationKt(intensity: ULong): ULong {
        // Perform CPU-intensive calculations
        var result = 0UL
        for (i in 1UL..intensity) {
            result += fibonacci(i)
        }

        // Simulate complex business logic
        return when (intensity) {
            in 1UL..3UL -> result + 10UL
            in 4UL..7UL -> result + 50UL
            else -> result + 100UL
        }
    }

    private fun fibonacci(n: ULong): ULong {
        return when (n) {
            0UL -> 0UL
            1UL -> 1UL
            else -> fibonacci(n - 1UL) + fibonacci(n - 2UL)
        }
    }
}