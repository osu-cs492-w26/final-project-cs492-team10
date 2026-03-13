package com.example.final_project_team10.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import com.example.final_project_team10.R
import com.example.final_project_team10.data.Movie_Info
import com.google.android.material.progressindicator.CircularProgressIndicator
import android.widget.ImageButton
import android.widget.ImageView
import coil.load
import com.google.android.material.button.MaterialButton

//everything in here should be an almost direct copy from the assignment 4 startercode
class GameScreenFragment : Fragment(R.layout.fragment_game) {

    private val API_KEY = "142812ec11e8136f3be45a8439922fa8"
    private val viewModel: GameScreenViewModel by viewModels()

    private lateinit var prefs: SharedPreferences
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var loadingErrorTV: TextView

    private lateinit var movieABox: LinearLayout
    private lateinit var movieBBox: LinearLayout

    //for movie A
    private lateinit var titleTextA: TextView
    private lateinit var dateTextA: TextView
    private lateinit var posterImageA: ImageView
    private lateinit var detailsButtonA: ImageButton

    //for movie B
    private lateinit var titleTextB: TextView
    private lateinit var dateTextB: TextView
    private lateinit var posterImageB: ImageView
    private lateinit var detailsButtonB: ImageButton

    //result values
    private lateinit var ratingResultsA: TextView
    private lateinit var ratingResultsB: TextView
    private lateinit var gameResult: TextView

    private lateinit var nextGame: MaterialButton

    private var ratingRevealed = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        movieABox = view.findViewById(R.id.movieABox)
        movieBBox = view.findViewById(R.id.movieBBox)

        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        titleTextA = view.findViewById(R.id.titleTextA)
        dateTextA = view.findViewById(R.id.dateTextA)
        posterImageA = view.findViewById(R.id.posterImageA)
        detailsButtonA = view.findViewById(R.id.detailsButtonA)

        titleTextB = view.findViewById(R.id.titleTextB)
        dateTextB = view.findViewById(R.id.dateTextB)
        posterImageB = view.findViewById(R.id.posterImageB)
        detailsButtonB = view.findViewById(R.id.detailsButtonB)

        ratingResultsA = view.findViewById(R.id.tv_resultA)
        ratingResultsB = view.findViewById(R.id.tv_resultB)

        gameResult = view.findViewById(R.id.game_result)
        nextGame = view.findViewById(R.id.next_game)

        viewModel.movieA.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                bindMovieA(movie)
                movieABox.visibility = View.VISIBLE
            }
        }

        viewModel.movieB.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                bindMovieB(movie)
                movieBBox.visibility = View.VISIBLE
            }
        }

        detailsButtonA.setOnClickListener {
            val movie = viewModel.movieA.value ?: return@setOnClickListener
            MovieDetailsFragment.newInstance(movie.id, ratingRevealed)
                .show(childFragmentManager, "movieDetails")
        }

        detailsButtonB.setOnClickListener {
            val movie = viewModel.movieB.value ?: return@setOnClickListener
            MovieDetailsFragment.newInstance(movie.id, ratingRevealed)
                .show(childFragmentManager, "movieDetails")
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = getString(R.string.loading_error, error.message)
                loadingErrorTV.visibility = View.VISIBLE
                Log.e("GameScreenFragment", "Error fetching movie: ${error.message}")
                error.printStackTrace()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
                movieABox.visibility = View.INVISIBLE
                movieBBox.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }

        //clickable buttons
        movieABox.setOnClickListener {
            viewModel.movieA.value?.let { movie -> onMovieSelected(movie, "A") }
        }
        movieBBox.setOnClickListener {
            viewModel.movieB.value?.let { movie -> onMovieSelected(movie, "B") }
        }
    }

    override fun onResume() {
        super.onResume()
        //use this for normal movie loading
        //viewModel.loadMovieInfo(550, 680, API_KEY)

        // https://www.themoviedb.org/talk/630a76b6ca7ec6009247749c
        //this link helped with figuring out how to make the random moive by genre

        //gets the genre value to check with mode the game should be played
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val genrenum = prefs.getString("genre", "0")
        val genreid = genrenum?.toIntOrNull()?.takeIf { it != 0 }

        viewModel.setGenreId(genreid)

        // Added condition to make sure that movies don't reload after returning
        // from a movie detail's page.
        if (viewModel.movieA.value == null || viewModel.movieB.value == null) {
            viewModel.preloadMovies(API_KEY)
        }
    }

    private fun bindMovieA(movie: Movie_Info) {
        titleTextA.text = movie.title
        dateTextA.text = extractYear(movie.release_date)

        posterImageA.load(getPosterUrl(movie.poster_path)) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }
    }

    private fun bindMovieB(movie: Movie_Info) {
        titleTextB.text = movie.title
        dateTextB.text = extractYear(movie.release_date)

        posterImageB.load(getPosterUrl(movie.poster_path)) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }

        //Added this so the ratings for movie B is always initially shown as '???". We can remove if we want.
        ratingResultsB.text = getString(R.string.hidden_rating)
        ratingResultsB.visibility = View.VISIBLE
    }

    //on button click
    private fun onMovieSelected(movie: Movie_Info, selected_movie: String) {

        //gets the game mode from preference/setting from user
        val gamemode = prefs.getString(
            getString(R.string.pref_mode_key),
            getString(R.string.pref_mode_default_value)
        )

        //this gets the two movie name
        val movieA_name = viewModel.movieA.value
        val movieB_name = viewModel.movieB.value

        ratingRevealed = true

        //displays the rating of both
        ratingResultsA.text = "Rating: ${movieA_name?.vote_average}"
        ratingResultsA.visibility = View.VISIBLE

        ratingResultsB.text = "Rating:  ${movieB_name?.vote_average}"
        ratingResultsB.visibility = View.VISIBLE

        //compares the movie ratings and writes if the guess is wrong or not
        if (movieA_name != null && movieB_name != null) {
            val higher = if ( movieA_name.vote_average >=  movieB_name.vote_average) "A" else "B"
            if (selected_movie == higher) {
                viewModel.addToScore()
                gameResult.text = "Correct | Current Score: ${viewModel.score.value}"
                nextGame.text = "Next Round"
            }
            else {
                gameResult.text = "Incorrect | Final Score: ${viewModel.score.value}"
                viewModel.resetScore()
                viewModel.resetGame()
                nextGame.text = "New Game"
            }
            gameResult.visibility = View.VISIBLE
            nextGame.visibility = View.VISIBLE
            movieABox.isClickable = false
            movieBBox.isClickable = false

            //button to play next game
            nextGame.setOnClickListener {
                ratingRevealed = false
                ratingResultsA.visibility = View.INVISIBLE
                ratingResultsB.visibility = View.INVISIBLE
                gameResult.visibility = View.INVISIBLE
                movieABox.isClickable = true
                movieBBox.isClickable = true

                //checks the game mode setting to decide the game mode
                if (gamemode == "classic") {
                    viewModel.loadNextFromPool()
                } else {
                    viewModel.loadRandomMovieInfo(API_KEY)
                }

                nextGame.visibility = View.INVISIBLE
            }
        }
    }

    private fun extractYear(releaseDate: String?): String {
        if (releaseDate.isNullOrBlank() || releaseDate.length < 4) {
            return "Unknown"
        }
        return releaseDate.substring(0, 4)
    }

    private fun getPosterUrl(posterPath: String?): String? {
        return if (posterPath.isNullOrBlank()) {
            null
        } else {
            "https://image.tmdb.org/t/p/w500$posterPath"
        }
    }


}