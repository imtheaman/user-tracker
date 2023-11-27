package com.example.usertracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import androidx.core.content.PermissionChecker
import com.example.usertracker.dto.NotificationDto
import com.example.usertracker.dto.UserDataDto
import com.example.usertracker.db.usersession.UserSessionDb
import com.example.usertracker.dto.AppUseDto
import com.example.usertracker.dto.SerializedHashMap
import com.example.usertracker.tasks.services.SaveSessionDataService
import com.example.usertracker.tasks.services.SendSessionDataService
import kotlin.properties.Delegates

/* if specific action url not defined but method used inside the application, it'd make the network req. to the mainUrl */

class UserTracker {
    companion object {
        private val TAG: String = "UserTracker[module]"

        private lateinit var context: Context
        private lateinit var mainUrl: String
        private var retriesDelay by Delegates.notNull<Long>()
        private var maxRetries by Delegates.notNull<Int>()
        private lateinit var urlPaths: HashMap<String, String>
        private lateinit var sessionDb: UserSessionDb

        fun initialize(
            context: Context,
            mainUrl: String,
            urlPaths: HashMap<String, String> = hashMapOf(),
            retriesDelay: Long,
            maxRetries: Int
        ) {
            this.context = context
            this.mainUrl = mainUrl
            this.urlPaths = urlPaths
            this.retriesDelay = retriesDelay
            this.maxRetries = maxRetries
            this.sessionDb = UserSessionDb.getInstance(context)
        }

        fun appOpen() {
            val data = AppUseDto(
                System.currentTimeMillis(),
                getCurrentBatteryPercent(context),
                isInternetConnected(context),
                isWifiConnected(context)
            )

            val intent = Intent(context, SaveSessionDataService::class.java)
            intent.action = TrackerActions.APP_OPEN
            intent.putExtra("value", data)
            context.startService(intent)
        }

        fun appClose() {
            val data = AppUseDto(
                System.currentTimeMillis(),
                getCurrentBatteryPercent(context),
                isInternetConnected(context),
                isWifiConnected(context)
            )

            val intent = Intent(context, SaveSessionDataService::class.java)
            intent.action = TrackerActions.APP_CLOSE
            intent.putExtra("value", data)
            context.startService(intent)

            val sendSessionIntent = Intent(context, SendSessionDataService::class.java)
            sendSessionIntent.action = TrackerActions.SEND_SESSION
            val value = sessionDb.userSession().getAll()
            sendSessionIntent.putExtra("url", mainUrl)
            sendSessionIntent.putExtra("max_retries", maxRetries)
            sendSessionIntent.putExtra("retries_delay", retriesDelay)
            sendSessionIntent.putExtra("value", value)
            context.startService(sendSessionIntent)
        }

        fun appData(data: SerializedHashMap<String, Any>) {
            val intent = Intent(context, SaveSessionDataService::class.java)
            intent.action = TrackerActions.APP_DATA
            intent.putExtra("data", data)

            context.startService(intent)
        }

        fun notificationClick(data: NotificationDto, callback: (() -> Unit)? = null) {
            val intent = Intent(context, SaveSessionDataService::class.java)
            intent.action = TrackerActions.NOTIFICATION_CLICK
            intent.putExtra("value", data)

            context.startService(intent)
        }

        fun notificationDismiss(data: NotificationDto, callback: (() -> Unit)? = null) {
            val intent = Intent(context, SaveSessionDataService::class.java)
            intent.action = TrackerActions.NOTIFICATION_DISMISS
            intent.putExtra("value", data)

            context.startService(intent)
        }

        fun deviceInfo() {
            // Get the PackageManager
            val packageManager: PackageManager = context.packageManager

            // Create an Intent to query for all apps
            val mainIntent = packageManager.getLaunchIntentForPackage("android.intent.category.LAUNCHER")
            val intent = Intent(mainIntent)
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_LAUNCHER)

            // Query for all apps
            val resolveInfos: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

            // Get information about each app
            val packages = resolveInfos.map {it.loadLabel(packageManager).toString()}
            val userData = UserDataDto(
                packages,
                /* can have a permissions variable and loop over that to check permission status*/
                hashMapOf(/* hardcoded as of now */ "ACCESS_WIFI_STATE" to (
                        PermissionChecker.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) == PermissionChecker.PERMISSION_GRANTED)
                ),
                hashMapOf(
                    "devicename" to Build.DEVICE,
                    "model" to Build.MODEL
                    /* and more data we need */
                )
            )

            val url = "${mainUrl}/${urlPaths["DEVICE_INFO"]}"
            val sendIntent = Intent(context, SendSessionDataService::class.java)
            sendIntent.action = TrackerActions.SEND_DEVICEINFO
            sendIntent.putExtra("url", url)
            sendIntent.putExtra("value", userData)
            sendIntent.putExtra("max_retries", maxRetries)
            sendIntent.putExtra("retries_delay", retriesDelay)

            context.startService(intent)
        }
    }
}
