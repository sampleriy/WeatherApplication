package com.example.weatherapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_CODE = 100

    private val apiKey = "f1b144b9395dc45c0077c122e018b453" // 🔴 Replace with your API key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Views
        val etCity = findViewById<EditText>(R.id.etCity)
        val btnGetWeather = findViewById<Button>(R.id.btnGetWeather)

        val tvCity = findViewById<TextView>(R.id.tvCity)
        val tvTemp = findViewById<TextView>(R.id.tvTemp)
        val tvFeels = findViewById<TextView>(R.id.tvFeels)
        val tvHumidity = findViewById<TextView>(R.id.tvHumidity)
        val tvWind = findViewById<TextView>(R.id.tvWind)
        val tvCondition = findViewById<TextView>(R.id.tvCondition)

        val imgWeather = findViewById<ImageView>(R.id.imgWeather)

        val btnProfile = findViewById<Button>(R.id.btnProfile)
        val btnSettings = findViewById<Button>(R.id.btnSettings)

        // 🔹 Manual Search
        btnGetWeather.setOnClickListener {
            val city = etCity.text.toString().trim()

            if (city.isEmpty()) {
                Toast.makeText(this, "Enter city name", Toast.LENGTH_SHORT).show()
            } else {
                getWeather(
                    city,
                    tvCity, tvTemp, tvFeels, tvHumidity, tvWind, tvCondition, imgWeather
                )
            }
        }

        // 🔹 Auto GPS Weather
        checkLocationPermission(
            tvCity, tvTemp, tvFeels, tvHumidity, tvWind, tvCondition, imgWeather
        )

        // 🔹 Navigation
        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    // ✅ Permission check
    private fun checkLocationPermission(
        tvCity: TextView,
        tvTemp: TextView,
        tvFeels: TextView,
        tvHumidity: TextView,
        tvWind: TextView,
        tvCondition: TextView,
        imgWeather: ImageView
    ) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation(
                tvCity, tvTemp, tvFeels, tvHumidity, tvWind, tvCondition, imgWeather
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    // ✅ Get location
    private fun getCurrentLocation(
        tvCity: TextView,
        tvTemp: TextView,
        tvFeels: TextView,
        tvHumidity: TextView,
        tvWind: TextView,
        tvCondition: TextView,
        imgWeather: ImageView
    ) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) return

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                getWeatherByLocation(
                    it.latitude,
                    it.longitude,
                    tvCity, tvTemp, tvFeels, tvHumidity, tvWind, tvCondition, imgWeather
                )
            }
        }
    }

    // ✅ Weather using GPS
    private fun getWeatherByLocation(
        lat: Double,
        lon: Double,
        tvCity: TextView,
        tvTemp: TextView,
        tvFeels: TextView,
        tvHumidity: TextView,
        tvWind: TextView,
        tvCondition: TextView,
        imgWeather: ImageView
    ) {
        val url =
            "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric"

        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Location Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body?.string() ?: return
                val json = JSONObject(data)

                val cityName = json.getString("name")
                val main = json.getJSONObject("main")
                val wind = json.getJSONObject("wind")
                val weather = json.getJSONArray("weather").getJSONObject(0)

                val temp = main.getDouble("temp")
                val feels = main.getDouble("feels_like")
                val humidity = main.getInt("humidity")
                val windSpeed = wind.getDouble("speed")
                val condition = weather.getString("main")

                val icon = weather.getString("icon")
                val iconUrl = "https://openweathermap.org/img/wn/$icon@2x.png"

                runOnUiThread {
                    tvCity.text = "📍 $cityName"
                    tvTemp.text = "🌡 Temp: $temp °C"
                    tvFeels.text = "🤒 Feels Like: $feels °C"
                    tvHumidity.text = "💧 Humidity: $humidity %"
                    tvWind.text = "🌬 Wind: $windSpeed m/s"
                    tvCondition.text = "🌥 Condition: $condition"

                    Glide.with(this@MainActivity)
                        .load(iconUrl)
                        .into(imgWeather)
                }
            }
        })
    }

    // ✅ Weather by city
    private fun getWeather(
        city: String,
        tvCity: TextView,
        tvTemp: TextView,
        tvFeels: TextView,
        tvHumidity: TextView,
        tvWind: TextView,
        tvCondition: TextView,
        imgWeather: ImageView
    ) {
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric"

        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Network Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body?.string() ?: return
                val json = JSONObject(data)

                val cityName = json.getString("name")
                val main = json.getJSONObject("main")
                val wind = json.getJSONObject("wind")
                val weather = json.getJSONArray("weather").getJSONObject(0)

                val icon = weather.getString("icon")
                val iconUrl = "https://openweathermap.org/img/wn/$icon@2x.png"

                runOnUiThread {
                    tvCity.text = "📍 $cityName"
                    tvTemp.text = "🌡 Temp: ${main.getDouble("temp")} °C"
                    tvFeels.text = "🤒 Feels Like: ${main.getDouble("feels_like")} °C"
                    tvHumidity.text = "💧 Humidity: ${main.getInt("humidity")} %"
                    tvWind.text = "🌬 Wind: ${wind.getDouble("speed")} m/s"
                    tvCondition.text = "🌥 Condition: ${weather.getString("main")}"

                    Glide.with(this@MainActivity)
                        .load(iconUrl)
                        .into(imgWeather)
                }
            }
        })
    }

    // ✅ Permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            recreate()
        }
    }
}