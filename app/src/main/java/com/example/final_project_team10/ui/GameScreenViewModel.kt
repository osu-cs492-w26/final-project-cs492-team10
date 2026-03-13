package com.example.final_project_team10.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_team10.data.Movie_Info
import com.example.final_project_team10.data.Movie_Info_Repository
import com.example.final_project_team10.data.TMDBService
import kotlin.random.Random

import kotlinx.coroutines.launch

//this is also almost the same as the assignment 4 starter code
class GameScreenViewModel : ViewModel(){

    private val API_KEY = "142812ec11e8136f3be45a8439922fa8"
    private val repository = Movie_Info_Repository(TMDBService.create())

    //calling the first movie
    private val _movieAResults = MutableLiveData<Movie_Info?>(null)
    val movieA: LiveData<Movie_Info?> = _movieAResults

    //calling the second movie
    private val _movieBResults = MutableLiveData<Movie_Info?>(null)
    val movieB: LiveData<Movie_Info?> = _movieBResults

    //keeps score
    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int> = _score

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private var currentGenreId: Int? = null
    private val moviePool = mutableListOf<Movie_Info>()
    private val seenIds = mutableSetOf<Int>()


    fun preloadMovies(apiKey: String) {
        if (moviePool.isNotEmpty()) {
            startGameFromPool()
            return
        }

        viewModelScope.launch {
            _loading.value = true

            val pagesToLoad = 1
            val randomPages = (1..100).shuffled().take(pagesToLoad)
            Log.d("Random Pages", "Random pages are ${randomPages}")


            for (page in randomPages) {
                val result = repository.loadMoviesByGenre(currentGenreId, apiKey, page, true)
                if (result.isSuccess) {
                    val movies = result.getOrNull() ?: emptyList()
                    movies.forEach { movie ->
                        if (seenIds.add(movie.id)) {
                            moviePool.add(movie)
                        }
                    }
                }
            }

            moviePool.shuffle()

            _loading.value = false

            // START THE GAME AFTER LOADING
            startGameFromPool()
        }
    }

    fun startGameFromPool() {
        if (moviePool.size < 2) return

        val first = moviePool.removeAt(0)
        val second = moviePool.removeAt(0)

        _movieAResults.value = first
        _movieBResults.value = second
    }

    fun loadNextFromPool() {
        val currentA = _movieAResults.value ?: return
        val currentB = _movieBResults.value ?: return

        val winner =
            if (currentA.vote_average >= currentB.vote_average) currentA else currentB

        val targetRating = winner.vote_average

        val window = 1.0
        val minRating = targetRating - window
        val maxRating = targetRating + window

        var candidates = moviePool.filter {
            it.vote_average in minRating..maxRating
        }

        if (candidates.isEmpty()) {
            candidates = moviePool
        }

        if (candidates.isEmpty()) {
            Log.e("GameScreen", "No movies left in pool")
            return
        }

        val nextMovie = candidates.random()

        moviePool.remove(nextMovie)

        _movieAResults.value = winner
        _movieBResults.value = nextMovie
    }


    //this is for loading in random movie ids
    fun loadRandomMovieInfo(apiKey: String) {
        viewModelScope.launch {
            _loading.value = true

            if (moviePool.size < 2) {
                Log.e("GameScreen", "Movie pool does not contain enough movies")
                _loading.value = false
                return@launch
            }

            moviePool.shuffle()

            Log.d("Movie Pool", moviePool.map { it.title }.toString())
            Log.d("Movie Pool Count", "${moviePool.size}")
            val movieA = moviePool.removeAt(0)
            val movieB = moviePool.removeAt(0)

            _movieAResults.value = movieA
            _movieBResults.value = movieB

            _loading.value = false
        }
    }



    fun addToScore() {
        _score.value = (_score.value ?: 0) + 1
    }

    fun resetScore() {
        _score.value = 0
    }

//    Function to reset moviePool and usedMovieIds on game restart
    fun resetGame() {
        Log.d("GamePool", "Pool size before reset: ${moviePool.size}")
        moviePool.clear()
        seenIds.clear()
        Log.d("GamePool", "Pool size before preload: ${moviePool.size}")
        preloadMovies(API_KEY)
        Log.d("GamePool", "Pool size after preload: ${moviePool.size}")
    }

    fun setGenreId(genreId: Int?) {
        currentGenreId = genreId
    }
}