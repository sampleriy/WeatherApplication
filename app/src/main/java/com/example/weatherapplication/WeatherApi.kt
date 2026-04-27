package com.example.weatherapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    // 🌍 Get location from city (works worldwide)
    @GET("geo/1.0/direct")
    fun getLocation(
        @Query("q") city: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): Call<List<GeoResponseItem>>

    // 🌦 Get weather using lat/lon
    @GET("data/2.5/weather")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Call<WeatherResponse>
}