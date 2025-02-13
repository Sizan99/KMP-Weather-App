# Weather App 🌦️

A cross-platform weather application built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (Compose for iOS and Android)**. It fetches real-time weather data using OpenWeather API, leveraging **Dependency Injection**, **Location Tracking**, and **State Management**.

---

## 🚀 Features

- 🌍 **Real-time Weather Data**: Fetches current weather conditions based on user location or searched city.
- 📍 **GPS Location Support**: Uses `moko-geo` for fetching device location.
- 🔍 **City Search**: Allows searching for weather by city name.
- 🎨 **Modern UI**: Designed with Compose Multiplatform for both Android & iOS.
- 💾 **State Management**: Implements `StateFlow` for handling UI state.
- 🔄 **Dependency Injection**: Uses Koin for managing dependencies.
- 🌎 **Multiplatform Support**: Runs on Android and iOS with shared business logic.

---

## 🏗️ Tech Stack

| Category            | Technology |
|--------------------|------------|
| **Language**      | Kotlin |
| **Framework**     | Kotlin Multiplatform |
| **State Management** | Kotlin StateFlow |
| **Networking**    | Ktor Client |
| **Dependency Injection** | Koin / Manual DI |
| **Permissions**   | moko-permissions |
| **Location Tracking** | moko-geo |
| **Architecture**  | MVVM (Model-View-ViewModel) |
| **Build System**  | Gradle Kotlin DSL |

---

## 🛠️ Setup & Installation

### Prerequisites

- Install **Android Studio Giraffe+**
- Install **Kotlin Multiplatform Plugin**
- OpenWeather API Key

### Steps

1. **Clone the repository**:

   ```sh
   git clone https://github.com/yourusername/weather-app.git
   cd weather-app
   ```

2. **Configure API Key**:

   - Open `WeatherRepository.kt`
   - Replace `"YOUR_API_KEY"` with your OpenWeather API Key.

3. **Build and Run**:

   ```sh
   ./gradlew build
   ./gradlew installDebug
   ```

   For iOS:

   ```sh
   ./gradlew iosDeploy
   ```

---

## 🌐 API Used

We use **OpenWeather API** to fetch real-time weather data.

### Example API Response

```json
{
  "coord": { "lon": 90.4125, "lat": 23.8103 },
  "weather": [ { "id": 800, "main": "Clear", "description": "clear sky", "icon": "01d" } ],
  "main": {
    "temp": 303.15,
    "feels_like": 306.26,
    "pressure": 1013,
    "humidity": 74
  },
  "wind": { "speed": 4.63, "deg": 120 },
  "name": "Dhaka"
}
```

---

## 🔄 Dependency Injection

The project follows **Dependency Injection** using `moko-geo`, `moko-permissions`, and `Ktor`:

```kotlin
class WeatherViewModel(private val locationTracker: LocationTracker) : ViewModel() {

    private val _appState = MutableStateFlow<AppState>(AppState.Loading)
    val appState = _appState.asStateFlow()

    fun searchByCity(city: String) {
        viewModelScope.launch {
            _appState.value = AppState.Loading
            try {
                val result = repository.getWeatherByCity(city)
                _appState.value = AppState.Success(result)
            } catch (e: Exception) {
                _appState.value = AppState.Error(e.message.toString())
            }
        }
    }
}
```

---


## 🤝 Contributing

Contributions are welcome! Open an issue or submit a PR. 

---


## 👨‍💻 Author

Developed by **Shiam Ahmed Sizan**.

