package com.example.usertracker.db.usersession

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.usertracker.dto.NotificationDto
import com.example.usertracker.dto.AppUseDto
import java.io.Serializable

@Entity(tableName = "sessionstore")
data class UserSessionEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "opened") var opened: AppUseDto? = null,
    @ColumnInfo(name = "closed") var closed: AppUseDto? = null,
    @ColumnInfo(name = "data") var data: HashMap<String, Any> = hashMapOf(),
    @ColumnInfo(name = "retries") var retries: List<String> = listOf(),
    @ColumnInfo(name = "notification") var notification: NotificationDto? = null,
    @ColumnInfo(name = "timestamp") var timestamp: Long = System.currentTimeMillis()
): Serializable
