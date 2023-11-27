package com.example.usertracker.dto

data class ApiResponseDto(
    val success: Boolean,
    val error: String,
    val data: Any
)
