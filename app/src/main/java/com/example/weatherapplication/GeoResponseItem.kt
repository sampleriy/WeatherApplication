package com.example.weatherapplication

data class GeoResponseItem(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String?
)