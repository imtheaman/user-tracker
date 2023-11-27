package com.example.usertracker.db.server

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ServerDao {
    @Query("SELECT * FROM serverstore LIMIT 1")
    fun getServerEntity(): ServerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: ServerEntity)
}