package com.example.weather

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("geo_object") val city: String,
    @SerializedName("fact") val temperature: Double,
    @SerializedName("condition") val description: String
)