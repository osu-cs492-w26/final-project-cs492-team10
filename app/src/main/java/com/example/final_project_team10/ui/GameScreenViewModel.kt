package com.example.final_project_team10.ui

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

    //this is for loading in random movie ids
    fun loadRandomMovieInfo (apiKey: String) {
        //we can also increase the random gen number values
        val randomNumberA = Random.nextInt(1, 1000)
        var randomNumberB = Random.nextInt(1, 1000)

        //checks for random duplicates
        while (randomNumberB == randomNumberA){
            randomNumberB = Random.nextInt(1, 1000)
        }

        viewModelScope.launch {
            _loading.value = true
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

    fun addToScore() {
        _score.value = (_score.value ?: 0) + 1
    }

    fun resetScore() {
        _score.value = 0
    }
}