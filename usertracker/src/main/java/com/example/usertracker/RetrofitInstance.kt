package com.example.usertracker

import com.example.usertracker.dto.ApiResponseDto
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    interface ApiInterface {
        @POST
        suspend fun sendData(@Url url: String, @Body data: Any): ApiResponseDto
    }
    fun apiService(): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}
