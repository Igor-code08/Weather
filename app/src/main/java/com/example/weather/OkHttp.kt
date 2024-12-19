package com.example.weather

import okhttp3.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class OkHttp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Координаты города (Москва)
        val latitude = "55.7558" // Широта
        val longitude = "37.6173" // Долгота

        // URL для запроса к Yandex Weather API
        val url = "https://api.weather.yandex.ru/v2/forecast?lat=$latitude&lon=$longitude&lang=ru_RU&limit=1"

        // Ваш API-ключ (замените на свой ключ)
        val apiKey = "abjottr9bhc6d0buc1e7" // мой ключ

        // Создаем HTTP-клиент OkHttp
        val client = OkHttpClient()

        // Формируем запрос
        val request = Request.Builder()
            .url(url) // Указываем URL
            .addHeader("abjottr9bhc6d0buc1e7", apiKey) // Добавляем заголовок с ключом API
            .get() // Метод запроса GET
            .build()

        // Выполняем запрос асинхронно
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Обработка ошибки при выполнении запроса
                Log.e("Error", "Ошибка запроса: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // Получаем тело ответа в виде строки
                    val responseData = response.body?.string()
                    Log.d("Response", responseData ?: "Пустой ответ")
                } else {
                    // Обработка ошибки (например, неверный ключ или параметры)
                    Log.e("Error", "Ошибка: ${response.code}")
                }
            }
        })
    }
}