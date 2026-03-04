package com.example.final_project_team10.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import kotlin.String

@JsonClass(generateAdapter = true)
data class Movie_Info(
    val title: String,
    val overview: String,
    val release_date: String,
    val vote_average: Double
)
