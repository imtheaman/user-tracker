package com.example.usertracker.tasks.services

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.usertracker.TrackerActions
import com.example.usertracker.RetrofitInstance
import com.example.usertracker.dto.ApiResponseDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.http.Body
import retrofit2.http.Url


class SendSessionDataService : IntentService("SendSessionDataService") {
    private val TAG = SendSessionDataService::class.java.simpleName
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) return
        val url = intent.getStringExtra("url")
        val body = intent.getSerializableExtra("value") as Body
        val maxRetries = intent.getIntExtra("max_retries", 3)
        val retriesDelay = intent.getLongExtra("retries_delay", 5000)

        when (intent.action) {
            /* based on the requirements we can create multiple Actions and handle 'em here */
            /* as of now just calling the method (repetitive), we can also customize it the way we want and it's not tightly coupled */
            TrackerActions.SEND_SESSION -> {
                val success = makeNetworkRequest(url,body)
                if (!success) {
                    retryNetworkRequest(url, body, maxRetries, retriesDelay)
                }

            }

            TrackerActions.SEND_DEVICEINFO -> {
                val success = makeNetworkRequest(url,body)
                if (!success) {
                    retryNetworkRequest(url, body, maxRetries, retriesDelay)
                }
            }
        }
    }

    private fun makeNetworkRequest(@Url url: String, @Body body: Body): Boolean {
        val scope = CoroutineScope(Dispatchers.IO)
        var response: ApiResponseDto? = null

        try {
            scope.launch {
                response = RetrofitInstance.apiService().sendData(url, body)
            }
        } catch (e: HttpException) {
            Log.e(TAG, e.message())
            e.printStackTrace()
        } catch (e: Exception) {
            e.message?.let { Log.e(TAG, it) }
            e.printStackTrace()
        }
        return response?.success ?: false
    }

    private fun retryNetworkRequest(@Url url: String, @Body body: Body, retriesLeft: Int, retriesDelay: Long) {
        if (retriesLeft > 0) {
            Log.d(TAG,"Retrying network request.")

            Handler(Looper.getMainLooper()).postDelayed({
                val success = makeNetworkRequest(url, body)
                if (!success) {
                    retryNetworkRequest(url, body, retriesLeft-1, retriesDelay)
                }
            }, retriesDelay)
        } else {
            Log.e(TAG, "Max retries reached. Network request failed.")
            val intent = Intent(applicationContext, SaveServerDataService::class.java)
            intent.action = TrackerActions.SERVER_STATUS
            intent.putExtra("SERVER_OKAY", false)
            applicationContext.startService(intent)
        }
    }
}
