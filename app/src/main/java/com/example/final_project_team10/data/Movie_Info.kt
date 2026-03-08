package com.example.final_project_team10.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import kotlin.String

@JsonClass(generateAdapter = true)
data class Movie_Info_Genre(
    val page: Int,
    val results: List<Movie_Info>
)

@JsonClass(generateAdapter = true)
data class Genre(
    val id: Int,
    val name: String
)
@JsonClass(generateAdapter = true)
data class Movie_Info(
    val title: String,
    val overview: String,
    val release_date: String,
    val vote_average: Double,
    val id: Int,
    val poster_path: String?,
    val runtime: Int?,
    val budget: Long?,
    val revenue: Long?,
    val genres: List<Genre>?
)

@JsonClass(generateAdapter = true)
data class Movie_Video_Response(
    val id: Int,
    val results: List<Movie_Video>
)

@JsonClass(generateAdapter = true)
data class Movie_Video(
    val key: String,
    val name: String,
    val site: String,
    val type: String
)