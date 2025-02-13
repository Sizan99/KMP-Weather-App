package data.repository

import network.ApiService

class WeatherRepository {
    private val apiService = ApiService()
    suspend fun getWeatherByLatLon(lat: Double, lon: Double)= apiService.getWeatherByLatLon(lat, lon)
    suspend fun getWeatherByCity(city: String)= apiService.getWeatherByCity(city)
}