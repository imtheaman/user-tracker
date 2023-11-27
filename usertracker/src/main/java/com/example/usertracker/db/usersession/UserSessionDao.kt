package com.example.usertracker.db.usersession

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.usertracker.dto.NotificationDto
import com.example.usertracker.dto.AppUseDto
import com.example.usertracker.dto.SerializedList

@Dao
interface UserSessionDao {
    @Query("SELECT * from sessionstore")
    fun getAll(): SerializedList<UserSessionEntity>

    @Query("SELECT * FROM sessionstore ORDER BY timestamp DESC LIMIT 1")
    fun getLatestData(): UserSessionEntity

    @Insert
    fun insert(data: UserSessionEntity) : Long

    @Query("UPDATE sessionstore SET retries = :value || retries WHERE id LIKE :id")
    fun updateRetries(
        id: Long,
        value: String
    )

    @Query("UPDATE sessionstore SET opened = :opened where id LIKE :id")
    fun updateOpened(
        id: Long,
        opened: AppUseDto
    )

    @Query("UPDATE sessionstore SET closed = :closed where id LIKE :id")
    fun updateClosed(
        id: Long,
        closed: AppUseDto
    )

    @Query("UPDATE sessionstore SET notification = :notification WHERE id LIKE :id")
    fun updateNotification(
        id: Long,
        notification: NotificationDto
    )

    @Query("UPDATE sessionstore SET data = json_insert(data, :key, :value) WHERE id LIKE :id")
    fun updateData(
        id: Long,
        key: String,
        value: Any
    )

    @Query("UPDATE sessionstore SET data = :data WHERE id LIKE :id")
    fun updateDataHashMap(
        id: Long,
        data: HashMap<String, Any>
    )

    @Query("DELETE From sessionstore where id = :id")
    fun delete(id : Long)

    @Query("SELECT * from sessionstore where id Like :id")
    fun get(id : Long): UserSessionEntity
}
