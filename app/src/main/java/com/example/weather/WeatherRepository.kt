package com.example.weather

import android.app.VoiceInteractor
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class WeatherRepository {

    private val client = OkHttpClient()
    private val gson = Gson()

    private val apiKey = "abjottr9bhc6d0buc1e7" //  мой ключ
    private val baseUrl = "https://api.weather.yandex.ru/v2/forecast"

    suspend fun getWeatherByCity(cityName: String): WeatherData {
        val url = "$baseUrl?geoid=$cityName&lang=ru_RU"

        val request = Request.Builder()
            .url(url)
            .addHeader("abjottr9bhc6d0buc1e7", apiKey)
            .build()

        return executeRequest(request)
    }

    suspend fun getWeatherByCoordinates(latitude: Double, longitude: Double): WeatherData {
        val url = "$baseUrl?lat=$latitude&lon=$longitude&lang=ru_RU"

        val request = Request.Builder()
            .url(url)
            .addHeader("Xabjottr9bhc6d0buc1e7", apiKey)
            .build()

        return executeRequest(request)
    }

    private suspend fun executeRequest(request: Request): WeatherData {
        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw Exception("Ошибка API")
            val responseBody = response.body?.string() ?: throw Exception("Пустой ответ")
            gson.fromJson(responseBody, WeatherData::class.java)
        }
    }
}



