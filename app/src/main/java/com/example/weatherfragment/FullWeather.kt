package com.example.weatherfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL


class FullWeather: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.full, container, false)
    }
    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        GlobalScope.launch(Dispatchers.IO) {
            try {
                val apiKey = "0cae94396adb869c5bc9e251426292be"
                val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=Irkutsk&appid=$apiKey&units=metric"
                val stream = URL(weatherURL).openStream()
                val data = stream.bufferedReader().use { it.readText() }

                val gson = Gson()
                val weatherData = gson.fromJson(data, WeatherData::class.java)



                val temperature = weatherData.main.temp
                val feelsLike = weatherData.main.feels_like
                val windSpeed = weatherData.wind.speed
                val description = weatherData.weather.firstOrNull()?.description

                val temperatureTextView: TextView = view.findViewById(R.id.temperatureTextView)
                val feelsLikeTextView: TextView = view.findViewById(R.id.feelsLikeTextView)
                val windSpeedTextView: TextView = view.findViewById(R.id.windSpeedTextView)
                val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)

                val windSpeedText = "Wind speed: $windSpeed m/s"
                val temperatureText = "Temperature: $temperature °C"
                val feelsLikeText = "Temperature feels like: $feelsLike °C"

                val iconCode = when (description){
                    "thunderstorm with light rain",
                    "thunderstorm with rain",
                    "thunderstorm with heavy rain",
                    "light thunderstorm",
                    "thunderstorm",
                    "heavy thunderstorm",
                    "ragged thunderstorm",
                    "thunderstorm with light drizzle",
                    "thunderstorm with drizzle",
                    "thunderstorm with heavy drizzle" -> "11d"
                    "light intensity drizzle",
                    "drizzle",
                    "heavy intensity drizzle",
                    "light intensity drizzle rain",
                    "drizzle rain",
                    "heavy intensity drizzle rain",
                    "shower rain and drizzle",
                    "heavy shower rain and drizzle",
                    "shower drizzle",
                    "light intensity shower rain",
                    "shower rain",
                    "heavy intensity shower rain",
                    "ragged shower rain"-> "09d"
                    "light rain",
                    "moderate rain",
                    "heavy intensity rain",
                    "very heavy rain",
                    "extreme rain" -> "10d"
                    "freezing rain",
                    "light snow",
                    "snow",
                    "heavy snow",
                    "sleet",
                    "light shower sleet",
                    "shower sleet",
                    "light rain and snow",
                    "rain and snow",
                    "light shower snow",
                    "shower snow",
                    "heavy shower snow" -> "13d"
                    "mist",
                    "smoke",
                    "haze",
                    "sand/dust whirls",
                    "fog",
                    "sand",
                    "dust",
                    "volcanic ash",
                    "squalls",
                    "tornado" -> "50d"
                    "clear sky" -> "01d"
                    "few clouds" -> "02d"
                    "scattered clouds" -> "03d"
                    "broken clouds",
                    "overcast clouds" -> "04d"

                    else -> {"01d"}
                }
                val imageUrl = "https://openweathermap.org/img/w/$iconCode.png"

                val imageView: ImageView = view.findViewById(R.id.imageView)

                requireActivity().runOnUiThread {
                    temperatureTextView.text = temperatureText
                    feelsLikeTextView.text = feelsLikeText
                    windSpeedTextView.text = windSpeedText
                    descriptionTextView.text = description

                    Glide.with(requireContext())
                        .load(imageUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}