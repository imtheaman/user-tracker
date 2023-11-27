package com.example.usertracker.dto

import java.io.Serializable

data class UserDataDto(
    val applications: List<String>,
    val permissions: HashMap<String, Boolean>,
    val device: HashMap<String, String>
): Serializable
