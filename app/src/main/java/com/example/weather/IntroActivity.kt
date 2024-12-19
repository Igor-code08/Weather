package com.example.weather

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val adapter = IntroPagerAdapter(getIntroSlides())
        viewPager.adapter = adapter

        // Переход к MainActivity после последнего слайда
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == adapter.itemCount - 1) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                        finish()
                    }, 1000)
                }
            }
        })
    }

    private fun getIntroSlides(): List<IntroSlide> {
        return listOf(
            IntroSlide("Добро пожаловать!", "Это приложение для прогноза погоды.", R.drawable.weather_image_1),
            IntroSlide("Прогноз погоды", "Получайте актуальную информацию о погоде.", R.drawable.weather_image_2),
            IntroSlide("Начнем!", "Введите город или используйте GPS для прогноза.", R.drawable.weather_image_3)
        )
    }
}