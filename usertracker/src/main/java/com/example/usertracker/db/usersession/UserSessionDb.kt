package com.example.usertracker.db.usersession

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserSessionDao::class], version = 1)
abstract class UserSessionDb : RoomDatabase() {
    abstract fun userSession(): UserSessionDao

    companion object {
        @Volatile
        private var instance: UserSessionDb? = null

        fun getInstance(context: Context): UserSessionDb {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserSessionDb::class.java,
                    "sessionstore"
                ).build().also { instance = it }
            }
        }
    }
}
