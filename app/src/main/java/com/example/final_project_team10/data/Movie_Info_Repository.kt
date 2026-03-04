package com.example.final_project_team10.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

//the caching system is the same as the one used in our assignments
class Movie_Info_Repository (
    private val service: TMDBService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    //for caching
    private var currentMovieId: Int? = null
    private var cachedMovie: Movie_Info? = null

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    suspend fun loadMovieInfo(
        movieId: Int,
        apiKey: String
    ): Result<Movie_Info?> {

        return if (shouldFetch(movieId)) {
            withContext(ioDispatcher) {
                try {
                    val response = service.getMovie(movieId, apiKey)
                    if (response.isSuccessful) {
                        cachedMovie = response.body()
                        timeStamp = timeSource.markNow()
                        currentMovieId = movieId
                        Result.success(cachedMovie)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Result.failure(e)
                }
            }
        } else {
            Result.success(cachedMovie!!)
        }
    }
    private fun shouldFetch(movieId: Int): Boolean =
        cachedMovie == null
        || movieId != currentMovieId
        || (timeStamp + cacheMaxAge).hasPassedNow()
}