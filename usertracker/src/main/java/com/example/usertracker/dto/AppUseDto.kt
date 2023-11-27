package com.example.usertracker.dto

import java.io.Serializable

data class AppUseDto(
    val time: Long,
    val batteryPercent: Number,
    val internetConnected: Boolean,
    val onWifi: Boolean
): Serializable
