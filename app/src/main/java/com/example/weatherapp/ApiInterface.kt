package com.example.weatherapp

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("Weather")
    fun getweatherdata(
        @Query("q") city: String,
        @Query("appId") appId: String,
        @Query("units") units: String,
        ) : retrofit2.Call<weatherApp>


    }
