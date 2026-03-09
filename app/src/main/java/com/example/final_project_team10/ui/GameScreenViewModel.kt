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
    private val usedMovieIds = mutableSetOf<Int>()

    //this is for loading in movie id normally
    /*fun loadMovieInfo (movieIdA: Int, movieIdB: Int, apiKey: String) {
        viewModelScope.launch {
            _loading.value = true
            val resultA = repository.loadMovieInfo(movieIdA, apiKey)
            val resultB = repository.loadMovieInfo(movieIdB, apiKey)
            _loading.value = false
            _error.value = resultA.exceptionOrNull() ?: resultB.exceptionOrNull()
            _movieAResults.value = resultA.getOrNull()
            _movieBResults.value = resultB.getOrNull()
        }
    }*/

    fun preloadMovies(apiKey: String) {
        if (moviePool.isNotEmpty()) {
            startGameFromPool()
            return
        }

        viewModelScope.launch {
            _loading.value = true

            val pagesToLoad = 10

            for (page in 1..pagesToLoad) {
                val result = repository.loadMoviesByGenre(currentGenreId, apiKey, page)

                if (result.isSuccess) {
                    val movies = result.getOrNull() ?: emptyList()

                    moviePool.addAll(movies)
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

        val first = moviePool.first { it.id !in usedMovieIds }
        usedMovieIds.add(first.id)

        val second = moviePool.first { it.id !in usedMovieIds }
        usedMovieIds.add(second.id)

        _movieAResults.value = first
        _movieBResults.value = second
    }

    fun loadNextFromPool() {
        val currentA = _movieAResults.value ?: return
        val currentB = _movieBResults.value ?: return

        val winner =
            if (currentA.vote_average >= currentB.vote_average) currentA else currentB

        val targetRating = winner.vote_average
        val higher = Random.nextBoolean()

        val window = 1.0
        val minRating = targetRating - window
        val maxRating = targetRating + window

        var candidates = moviePool.filter {
            it.id !in usedMovieIds &&
                    it.vote_average in minRating..maxRating
        }

        if (candidates.isEmpty()) {
            candidates = moviePool.filter { it.id !in usedMovieIds }
        }

        val nextMovie = candidates.random()

        usedMovieIds.add(nextMovie.id)

        _movieAResults.value = winner
        _movieBResults.value = nextMovie
    }

    //this is for loading in random movie ids
    fun loadRandomMovieInfo (apiKey: String) {
        viewModelScope.launch {
            _loading.value = true
            //search by genre
            if (currentGenreId != null) {
                val randomPageNumber = Random.nextInt(1, 100)
                val genre_movies = repository.loadMoviesByGenre(currentGenreId, apiKey, randomPageNumber)
                _loading.value = false

                if (genre_movies.isFailure || genre_movies.getOrNull() == null) {
                    loadRandomMovieInfo(apiKey)
                    return@launch
                }

                //found online
                //this shuffles the movie genre list instead of randomly calling
                val shuffled = genre_movies.getOrNull()!!.shuffled()
                _movieAResults.value = shuffled[0]
                _movieBResults.value = shuffled[1]
            }

            //search by random id
            else {
                //we can also increase the random gen number values
                val randomNumberA = Random.nextInt(1, 1000)
                var randomNumberB = Random.nextInt(1, 1000)

                //checks for random duplicates
                while (randomNumberB == randomNumberA){
                    randomNumberB = Random.nextInt(1, 1000)
                }

                val resultA = repository.loadMovieInfo(randomNumberA, apiKey)
                val resultB = repository.loadMovieInfo(randomNumberB, apiKey)
                _loading.value = false

                //found this function online
                //this is so that the function return only successful movieID
                //without this there were issues where some random values would return errors
                if (resultA.isFailure || resultA.getOrNull() == null ||
                    resultB.isFailure || resultB.getOrNull() == null) {
                    loadRandomMovieInfo(apiKey)
                    return@launch
                }

                _error.value = resultA.exceptionOrNull() ?: resultB.exceptionOrNull()
                _movieAResults.value = resultA.getOrNull()
                _movieBResults.value = resultB.getOrNull()
            }

        }
    }

    //on rounds after the first, keep the higher-rated movie as A and load a new random B
    fun loadNextRound(apiKey: String) {
        if ((_score.value ?: 0) == 0) {
            loadRandomMovieInfo(apiKey)
            return
        }

        val currentA = _movieAResults.value
        val currentB = _movieBResults.value

        //check the winner
        val winner = if ((currentA?.vote_average ?: 0.0) >= (currentB?.vote_average ?: 0.0)) currentA else currentB

        viewModelScope.launch {
            _loading.value = true

            //search by genre
            if (currentGenreId != null) {
                val randomPageNumber = Random.nextInt(1, 100)
                val genre_movies = repository.loadMoviesByGenre(currentGenreId, apiKey, randomPageNumber)
                _loading.value = false

                if (genre_movies.isFailure || genre_movies.getOrNull() == null) {
                    loadNextRound(apiKey)
                    return@launch
                }
                _movieAResults.value = winner
                _movieBResults.value = genre_movies.getOrNull()!!.shuffled().first()

            //search by random id
            }
            else {
                val randomNumberB = Random.nextInt(1, 1000)
                val resultB = repository.loadMovieInfo(randomNumberB, apiKey)
                _loading.value = false

                if (resultB.isFailure || resultB.getOrNull() == null) {
                    loadNextRound(apiKey)
                    return@launch
                }
                _movieAResults.value = winner
                _movieBResults.value = resultB.getOrNull()
            }
        }
    }

    fun addToScore() {
        _score.value = (_score.value ?: 0) + 1
    }

    fun resetScore() {
        _score.value = 0
    }

    fun setGenreId(genreId: Int?) {
        currentGenreId = genreId
    }
}