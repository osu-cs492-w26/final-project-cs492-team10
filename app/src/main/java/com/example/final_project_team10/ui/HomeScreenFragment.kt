package com.example.final_project_team10.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.final_project_team10.R

class HomeScreenFragment : Fragment(R.layout.fragment_home) {

    private lateinit var moviePosterOne: ImageView
    private lateinit var moviePosterTwo: ImageView

    private val moviePosterUrls = listOf(
        "https://image.tmdb.org/t/p/w500/RYMX2wcKCBAr24UyPD7xwmjaTn.jpg",
        "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg",
        "https://image.tmdb.org/t/p/w500/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg",
        "https://image.tmdb.org/t/p/w500/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg",
        "https://image.tmdb.org/t/p/w500/p96dm7sCMn4VYAStA6siNz30G1r.jpg",
        "https://image.tmdb.org/t/p/w500/vN5B5WgYscRGcQpVhHl6p9DDTP0.jpg",
        "https://image.tmdb.org/t/p/w500/b4Oe15CGLL61Ped0RAS9JpqdmCt.jpg"
    )

    private var firstCurrentMoviePoster = 0
    private var secondCurrentMoviePoster = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scoreboardButton: Button = view.findViewById(R.id.button_scoreboard)
        val startGameButton: Button = view.findViewById(R.id.button_start_game)
        val optionsButton: Button = view.findViewById(R.id.button_options)

        moviePosterOne = view.findViewById(R.id.moviePosterOne)
        moviePosterTwo = view.findViewById(R.id.moviePosterTwo)

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
            displayPoster(moviePosterOne, true)

            moviePosterTwo.postDelayed({
                displayPoster(moviePosterTwo, false)
            }, 6500L)
        }
    }

    private fun displayPoster(moviePoster: ImageView, isFirstPoster: Boolean) {
        val currentIndex = if (isFirstPoster) {
            firstCurrentMoviePoster
        } else {
            secondCurrentMoviePoster
        }

        val posterUrl = moviePosterUrls[currentIndex]

        moviePoster.load(posterUrl) {
            listener(
                onSuccess = { _, _ ->
                    posterAnimation(moviePoster, isFirstPoster)
                }
            )
        }

        if (isFirstPoster) {
            firstCurrentMoviePoster = (firstCurrentMoviePoster + 2) % moviePosterUrls.size
        } else {
            secondCurrentMoviePoster = (secondCurrentMoviePoster + 2) % moviePosterUrls.size
        }
    }

    private fun posterAnimation(
        moviePoster: ImageView,
        isFirstPoster: Boolean
    ) {
        val screenHeight = requireView().height.toFloat()
        val posterHeight = moviePoster.height.toFloat()

        val startY = screenHeight + 150f
        val endY = -(posterHeight + 200f)

        moviePoster.translationY = startY

        val animator = ObjectAnimator.ofFloat(
            moviePoster,
            View.TRANSLATION_Y,
            startY,
            endY
        )

        animator.duration = 12000L
        animator.interpolator = LinearInterpolator()

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                displayPoster(moviePoster, isFirstPoster)
            }
        })

        animator.start()
    }
}