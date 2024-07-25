package com.example.weatherapp

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather")
    fun getweatherdata(
        @Query("q") city: String,
        @Query("appid") appId: String,
        @Query("units") units: String,
        ) : retrofit2.Call<weatherApp>


    }
