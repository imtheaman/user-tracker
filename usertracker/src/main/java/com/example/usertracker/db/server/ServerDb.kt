package com.example.usertracker.db.server

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ServerDao::class], version = 1)
abstract class ServerDb: RoomDatabase() {
   abstract fun serverInfo(): ServerDao

   companion object {
       @Volatile
       private var instance: ServerDb? = null

       fun getInstance(context: Context): ServerDb {
           return instance ?: synchronized(this) {
               instance ?: Room.databaseBuilder(
                   context.applicationContext,
                   ServerDb::class.java,
                   "serverstore"
               ).build().also { instance = it }
           }
       }
   }
}