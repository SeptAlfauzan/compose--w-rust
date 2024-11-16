package org.kudos.rustoncompose.domain

data class Calculation<T>(
    val value: T,
    val executionTime: Long?
)