package com.example.final_project_team10.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.final_project_team10.R
import com.example.final_project_team10.data.Movie_Info
import com.example.final_project_team10.data.TMDBService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameScreenFragment : Fragment(R.layout.fragment_game) {

    private val API_KEY = "142812ec11e8136f3be45a8439922fa8"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //basic printing function, will change later
        val titleText: TextView = view.findViewById(R.id.titleText)
        val dateText: TextView = view.findViewById(R.id.dateText)
        val overviewText: TextView = view.findViewById(R.id.overviewText)
        val ratingText: TextView = view.findViewById(R.id.ratingText)

        val tmdbService = TMDBService.create()

//        tmdbService.getMovie(550, API_KEY).enqueue(object : Callback<Movie_Info> {
//            override fun onResponse(call: Call<Movie_Info>, response: Response<Movie_Info>) {
//                if (response.isSuccessful) {
//                    val movie = response.body()
//                    titleText.text = movie?.title
//                    dateText.text = "Release Date: ${movie?.release_date}"
//                    ratingText.text = "Rating: ${movie?.vote_average}"
//                    overviewText.text = movie?.overview
//                }
//            }
//
//            override fun onFailure(call: Call<Movie_Info>, t: Throwable) {
//                titleText.text = "Failed: ${t.message}"
//            }
//        })
    }
}