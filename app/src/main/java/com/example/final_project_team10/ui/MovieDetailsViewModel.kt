package com.example.final_project_team10.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project_team10.data.Movie_Info
import com.example.final_project_team10.data.Movie_Info_Repository
import com.example.final_project_team10.data.Movie_Video
import com.example.final_project_team10.data.TMDBService
import kotlinx.coroutines.launch

class MovieDetailsViewModel : ViewModel() {

    private val repository = Movie_Info_Repository(TMDBService.create())

    private val _movieDetailsResults = MutableLiveData<Movie_Info?>(null)
    val movieDetails: LiveData<Movie_Info?> = _movieDetailsResults

    private val _trailerResults = MutableLiveData<Movie_Video?>(null)
    val trailer: LiveData<Movie_Video?> = _trailerResults

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun loadMovieInfo(movieId: Int, apiKey: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadMovieInfo(movieId, apiKey)
            _loading.value = false

            _error.value = result.exceptionOrNull()
            _movieDetailsResults.value = result.getOrNull()
        }
    }

    fun loadMovieTrailer(movieId: Int, apiKey: String) {
        viewModelScope.launch {
            val result = repository.loadMovieVideos(movieId, apiKey)

            if (result.isSuccess) {
                val videos = result.getOrNull().orEmpty()

                val bestTrailer = videos.firstOrNull {
                    it.site == "YouTube" && it.type == "Trailer"
                } ?: videos.firstOrNull {
                    it.site == "YouTube"
                }

                _trailerResults.value = bestTrailer
            } else {
                _trailerResults.value = null
            }
        }
    }
}