package com.example.usertracker.tasks.services

import android.app.IntentService
import android.content.Intent
import com.example.usertracker.TrackerActions
import com.example.usertracker.db.server.ServerDb
import com.example.usertracker.db.server.ServerEntity

/* later we might need to store more server related data like which apis are having issues or what are the health of different nodes, so created a separate service for that */
class SaveServerDataService: IntentService("ServerDataService") {
    private var serverDb: ServerDb? = null
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        if (serverDb == null) {
            serverDb = ServerDb.getInstance(applicationContext)
        }

        when (intent.action) {
           TrackerActions.SERVER_STATUS -> serverDb!!.serverInfo().insert(ServerEntity(intent.getBooleanExtra("SERVER_OKAY", true)))
        }
    }
}