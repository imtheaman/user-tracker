package com.example.usertracker.dto

import java.io.Serializable

data class SerializedList<T>(val items: List<T>): Serializable
