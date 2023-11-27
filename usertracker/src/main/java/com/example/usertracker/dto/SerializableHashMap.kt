package com.example.usertracker.dto

import java.io.Serializable

data class SerializedHashMap<K,V>(val data: HashMap<K,V>) : Serializable
