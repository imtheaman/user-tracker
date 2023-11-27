package com.example.usertracker.dto

import java.io.Serializable

data class NotificationDto(
    val type: String = "DEFAULT",
    val title: String? = null,
    val image: String? = null, //url of the image or local image uri
    val message: String = "",
    val clicked: Boolean = false,
    val timeStayedInTray: Long = 0, //System.currentTimeMillis()
    val dismissed: Boolean = false
): Serializable
