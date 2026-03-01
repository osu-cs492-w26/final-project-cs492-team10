package com.example.final_project_team10.ui

import android.os.Bundle
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.example.final_project_team10.R
import com.example.final_project_team10.data.TMDBService
import com.example.final_project_team10.data.Movie_Info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val API_KEY = "142812ec11e8136f3be45a8439922fa8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //basic printing function, will change later
        val titleText = findViewById<TextView>(R.id.titleText)
        val dateText = findViewById<TextView>(R.id.dateText)
        val overviewText = findViewById<TextView>(R.id.overviewText)
        val ratingText = findViewById<TextView>(R.id.ratingText)

        val tmdbService = TMDBService.create()

        tmdbService.getMovie(550, API_KEY).enqueue(object : Callback<Movie_Info> {
            override fun onResponse(call: Call<Movie_Info>, response: Response<Movie_Info>) {
                if (response.isSuccessful) {
                    val movie = response.body()
                    titleText.text = movie?.title
                    dateText.text = "Release Date: ${movie?.release_date}"
                    ratingText.text = "Rating: ${movie?.vote_average}"
                    overviewText.text = movie?.overview
                }
            }

            override fun onFailure(call: Call<Movie_Info>, t: Throwable) {
                titleText.text = "Failed: ${t.message}"
            }
        })
    }
}