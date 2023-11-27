package com.example.usertracker.dto

import java.io.Serializable

data class UserSessionDto(
    var opened: AppUseDto? = null,
    var closed: AppUseDto? = null,
    var data: HashMap<String, Any>? = null,
    var retries: List<String> = listOf(),
    var notification: NotificationDto? = null
): Serializable {
    fun setOpened(value: AppUseDto) {
        this.opened = value
    }
    fun setClosed(value: AppUseDto) {
        this.closed = value
    }

    fun setData(value: HashMap<String, Any>) {
        this.data = value
    }
    fun setNotification(value: NotificationDto) {
        this.notification = value
    }
    fun setRetries(value: String) {
       this.retries.plus(value)
    }
}
