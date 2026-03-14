package com.example.final_project_team10.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity (tableName = "scores")
data class Score_Info(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val genre: String = "All",
    val gamemode: String = "Classic",
    val score: Int,
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
)
