package com.example.usertracker.tasks.services

import android.app.IntentService
import android.content.Intent
import com.example.usertracker.dto.NotificationDto
import com.example.usertracker.TrackerActions
import com.example.usertracker.db.usersession.UserSessionDb
import com.example.usertracker.db.usersession.UserSessionEntity
import com.example.usertracker.dto.AppUseDto
import com.example.usertracker.getCurrentBatteryPercent
import com.example.usertracker.isInternetConnected
import com.example.usertracker.isWifiConnected

class SaveSessionDataService : IntentService("UserSessionDataService") {
    private var sessionDb: UserSessionDb? = null
    private var sessionId: Long? = null
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        if (sessionDb == null) {
            sessionDb = UserSessionDb.getInstance(applicationContext)
            sessionId = sessionDb!!.userSession().insert(UserSessionEntity())
        }
        when (intent.action) {
            TrackerActions.APP_OPEN -> sessionDb!!.userSession().updateOpened(
                id = sessionId!!,
                opened = AppUseDto(
                    time = System.currentTimeMillis(),
                    batteryPercent = getCurrentBatteryPercent(applicationContext),
                    onWifi = isWifiConnected(applicationContext),
                    internetConnected = isInternetConnected(applicationContext)
                )
            )

            TrackerActions.APP_CLOSE -> sessionDb!!.userSession().updateClosed(
                id = sessionId!!,
                closed = AppUseDto(
                    time = System.currentTimeMillis(),
                    batteryPercent = getCurrentBatteryPercent(applicationContext),
                    onWifi = isWifiConnected(applicationContext),
                    internetConnected = isInternetConnected(applicationContext)
                )
            )

            TrackerActions.NETWORK_REQ_RETRIES -> sessionDb!!.userSession().updateRetries(
                id = sessionId!!,
                value = intent.getStringExtra("value")
            )

            TrackerActions.APP_DATA -> {
                if (intent.getSerializableExtra("data") != null) {
                  sessionDb!!.userSession().updateDataHashMap(
                      id = sessionId!!,
                      data = intent.getSerializableExtra("data") as HashMap<String, Any>
                  )
                } else {
                    sessionDb!!.userSession().updateData(
                        id = sessionId!!,
                        key = intent.getStringExtra("key"),
                        value = intent.getSerializableExtra("value") as Any
                    )
                }
            }

            TrackerActions.NOTIFICATION_CLICK -> sessionDb!!.userSession().updateNotification(
                id = sessionId!!,
                notification = intent.getSerializableExtra("value") as NotificationDto
            )

            TrackerActions.NOTIFICATION_DISMISS -> sessionDb!!.userSession().updateNotification(
                id = sessionId!!,
                notification = intent.getSerializableExtra("value") as NotificationDto
            )
        }
    }
}