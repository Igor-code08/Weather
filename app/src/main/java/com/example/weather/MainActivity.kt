package com.example.weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var weatherRepository: WeatherRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Связываем элементы интерфейса
        val cityInput = findViewById<EditText>(R.id.cityInput)
        val getWeatherButton = findViewById<Button>(R.id.getWeatherButton)
        val useGpsButton = findViewById<Button>(R.id.useGpsButton)
        val weatherInfoTextView = findViewById<TextView>(R.id.weatherInfoTextView)

        // Инициализация клиента для работы с GPS
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Инициализация репозитория для работы с API
        weatherRepository = WeatherRepository()

        // Обработчик кнопки "Получить погоду" по введенному городу
        getWeatherButton.setOnClickListener {
            val cityName = cityInput.text.toString().trim()
            if (cityName.isNotEmpty()) {
                fetchWeatherByCity(cityName, weatherInfoTextView)
            } else {
                Toast.makeText(this, "Введите название города", Toast.LENGTH_SHORT).show()
            }
        }

        // Обработчик кнопки "Использовать GPS" для получения местоположения
        useGpsButton.setOnClickListener {
            fetchWeatherByLocation(weatherInfoTextView)
        }
    }

    /**
     * Получение погоды по названию города
     */
    private fun fetchWeatherByCity(cityName: String, weatherInfoTextView: TextView) {
        lifecycleScope.launch {
            try {
                val weatherData = weatherRepository.getWeatherByCity(cityName)
                weatherInfoTextView.text = formatWeatherData(weatherData)
            } catch (e: Exception) {
                weatherInfoTextView.text = "Ошибка получения данных о погоде."
                e.printStackTrace()
            }
        }
    }

    /**
     * Получение погоды по текущему местоположению пользователя (GPS)
     */
    private fun fetchWeatherByLocation(weatherInfoTextView: TextView) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Запрашиваем разрешение на доступ к местоположению
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lifecycleScope.launch {
                    try {
                        val weatherData = weatherRepository.getWeatherByCoordinates(
                            location.latitude,
                            location.longitude
                        )
                        weatherInfoTextView.text = formatWeatherData(weatherData)
                    } catch (e: Exception) {
                        weatherInfoTextView.text = "Ошибка получения данных о погоде."
                        e.printStackTrace()
                    }
                }
            } else {
                weatherInfoTextView.text = "Не удалось определить местоположение."
            }
        }
    }

    /**
     * Форматируем данные о погоде для отображения в интерфейсе
     */
    private fun formatWeatherData(weatherData: WeatherData): String {
        return """
            Город: ${weatherData.city}
            Температура: ${weatherData.temperature}°C
            Описание: ${weatherData.description}
        """.trimIndent()
    }

    /**
     * Обработка результата запроса разрешений на доступ к местоположению
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Разрешение на местоположение предоставлено", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "Разрешение на местоположение отклонено", Toast.LENGTH_SHORT)
                .show()
        }
    }
}

