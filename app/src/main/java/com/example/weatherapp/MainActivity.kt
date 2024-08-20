package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
   private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
       binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       supportActionBar?.hide()
        fetchWeatherdata("Delhi")
        searchCity()
        }

    private fun searchCity() {
        val searchView = binding.searchview
        searchView.setOnQueryTextListener(object :android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherdata(query) //query is passed in function which were taken by searchview
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }



     private fun fetchWeatherdata(cityName :String) {
        val retrofit = Retrofit.Builder()     //retrofit is used to fetch API data
            .addConverterFactory(GsonConverterFactory.create())    //GSON converter is used to convert data
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getweatherdata("$cityName" , "13dc457e5059ee8ee3c9ab242dd9fdff" , "metric")
        response.enqueue(object : Callback<weatherApp>{
            override fun onResponse(call: Call<weatherApp>, response: Response<weatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    //taking data from API
                    val temperature =responseBody.main.temp.toString()
                    val humidity =responseBody.main.humidity
                    val windSpeed =responseBody.wind.speed
                    val sunRise =responseBody.sys.sunrise.toLong()
                    val sunSet =responseBody.sys.sunset.toLong()
                    val seaLevel =responseBody.main.pressure
                    val condition =responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxTemp =responseBody.main.temp_max
                    val minTemp =responseBody.main.temp_min

                   //showing data in app
                    binding.temp.text="$temperature"
                    binding.tvhumidity.text="$humidity"
                    binding.tvwindspeed.text="$windSpeed"
                    binding.tvsunrise.text="${time(sunRise)}"
                    binding.tvsunset.text="${time(sunSet)}"
                    binding.tvsea.text="$seaLevel"
                    binding.Condition.text="$condition"
                    binding.conditiontv.text="$condition"
                    binding.maxtemp.text="Max: $maxTemp"
                    binding.mintemp.text="Min: $minTemp"
                    binding.location.text="$cityName"
                    binding.day.text=day(System.currentTimeMillis())
                    binding.date.text=date()

                    changeackgroundImage(condition)
                }
            }

            override fun onFailure(call: Call<weatherApp>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //changing of background and lottie animation according to condition
    private fun changeackgroundImage(condition: String) {
        when(condition){
            "Partly Clouds","Clouds","Overcast","Mist","Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.cloud)
                binding.lottieAnimationView.setAnimation(R.raw.cloudlottie)
            }
            "Clear Sky","Clear","Sunny" -> {
                binding.root.setBackgroundResource(R.drawable.sunny)
                binding.lottieAnimationView.setAnimation(R.raw.sunnylottie)
            }
            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain","Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain)
                binding.lottieAnimationView.setAnimation(R.raw.rainlottie)
            }
            "Light Snow","Moderate Snow","Blizzard","Heavy Snow" -> {
                binding.root.setBackgroundResource(R.drawable.cloud)
                binding.lottieAnimationView.setAnimation(R.raw.cloudlottie)
            }else->
        {
            binding.root.setBackgroundResource(R.drawable.sunny)
            binding.lottieAnimationView.setAnimation(R.raw.sunnylottie)
        }

        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))
    }
    private fun date(): String {
        val sdf =SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun day(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }

}

