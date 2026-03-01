package com.example.final_project_team10.data

import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBService {
    @GET("movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<Movie_Info>

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        fun create() : TMDBService {
            val moshi = Moshi.Builder()
                //not sure
                .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(TMDBService::class.java)
        }
    }
}