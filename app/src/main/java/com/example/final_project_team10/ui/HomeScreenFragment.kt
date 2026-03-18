package com.example.final_project_team10.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.final_project_team10.R

class HomeScreenFragment : Fragment(R.layout.fragment_home) {

    private lateinit var moviePosterOne: ImageView
    private lateinit var moviePosterTwo: ImageView
    private lateinit var moviePosterThree: ImageView

    private val moviePosterUrls = listOf(
        "https://image.tmdb.org/t/p/w500/RYMX2wcKCBAr24UyPD7xwmjaTn.jpg",
        "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg",
        "https://image.tmdb.org/t/p/w500/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg",
        "https://image.tmdb.org/t/p/w500/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg",
        "https://image.tmdb.org/t/p/w500/p96dm7sCMn4VYAStA6siNz30G1r.jpg",
        "https://image.tmdb.org/t/p/w500/vN5B5WgYscRGcQpVhHl6p9DDTP0.jpg",
        "https://image.tmdb.org/t/p/w500/b4Oe15CGLL61Ped0RAS9JpqdmCt.jpg"
    )

    private var nextPosterIndex = 0

    private val scrollSpeed = 6f
    private val gap = 60f

    private val handler = Handler(Looper.getMainLooper())

    private val moveRunnable = object : Runnable {
        override fun run() {
            movePosters()
            handler.postDelayed(this, 16L)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scoreboardButton: Button = view.findViewById(R.id.button_scoreboard)
        val startGameButton: Button = view.findViewById(R.id.button_start_game)
        val optionsButton: Button = view.findViewById(R.id.button_options)

        moviePosterOne = view.findViewById(R.id.moviePosterOne)
        moviePosterTwo = view.findViewById(R.id.moviePosterTwo)
        moviePosterThree = view.findViewById(R.id.moviePosterThree)

        startGameButton.setOnClickListener {
            Log.d("Start Game Button", "Navigating to Game Page")
            val directions = HomeScreenFragmentDirections.navigateToGamePage()
            findNavController().navigate(directions)
        }

        optionsButton.setOnClickListener {
            Log.d("Open settings", "Navigating to Settings Page")
            val directions = HomeScreenFragmentDirections.navigateToSettingsPage()
            findNavController().navigate(directions)
        }

        scoreboardButton.setOnClickListener {
            Log.d("Open scoreboard", "Navigating to Scoreboard Page")
            val directions = HomeScreenFragmentDirections.navigateToScoreboardPage()
            findNavController().navigate(directions)
        }

        moviePosterOne.post {
            setupPosters()
            handler.post(moveRunnable)
        }
    }

    private fun setupPosters() {
        loadNextPoster(moviePosterOne)
        loadNextPoster(moviePosterTwo)
        loadNextPoster(moviePosterThree)

        val parentHeight = 0f
        // val parentHeight = requireView().height.toFloat()
        val posterHeight = moviePosterOne.height.toFloat()

        moviePosterOne.translationY = parentHeight
        moviePosterTwo.translationY = parentHeight + posterHeight + gap
        moviePosterThree.translationY = parentHeight + 2f * (posterHeight + gap)
    }

    private fun movePosters() {
        val posterHeight = moviePosterOne.height.toFloat()

        moviePosterOne.translationY -= scrollSpeed
        moviePosterTwo.translationY -= scrollSpeed
        moviePosterThree.translationY -= scrollSpeed

        if (moviePosterOne.translationY <= -posterHeight) {
            moviePosterOne.translationY = moviePosterThree.translationY + posterHeight + gap
            loadNextPoster(moviePosterOne)
        }

        if (moviePosterTwo.translationY <= -posterHeight) {
            moviePosterTwo.translationY = moviePosterOne.translationY + posterHeight + gap
            loadNextPoster(moviePosterTwo)
        }

        if (moviePosterThree.translationY <= -posterHeight) {
            moviePosterThree.translationY = moviePosterTwo.translationY + posterHeight + gap
            loadNextPoster(moviePosterThree)
        }
    }

    private fun loadNextPoster(imageView: ImageView) {
        val posterUrl = moviePosterUrls[nextPosterIndex]
        imageView.load(posterUrl)
        nextPosterIndex = (nextPosterIndex + 1) % moviePosterUrls.size
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(moveRunnable)
    }
}