package com.example.final_project_team10.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface TMDBService {
    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<Movie_Info>

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int?,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("vote_count.gte") minVotes: Int = 1000
    ): Response<Movie_Info_Genre>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<Movie_Video_Response>

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        fun create(): TMDBService {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(TMDBService::class.java)
        }
    }
}