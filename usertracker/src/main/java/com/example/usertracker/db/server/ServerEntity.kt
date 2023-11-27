package com.example.usertracker.db.server

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "serverstore")
data class ServerEntity(
//  later we can add multiple other data as well
    @ColumnInfo(name = "isserverokay") val SERVER_OKAY: Boolean = true
)