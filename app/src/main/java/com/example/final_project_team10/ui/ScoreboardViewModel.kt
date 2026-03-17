package com.example.final_project_team10.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.final_project_team10.data.AppDatabase
import com.example.final_project_team10.data.ScoreRepository
import kotlinx.coroutines.launch

class ScoreboardViewModel(application: Application) :
    AndroidViewModel(application)
{
    private val repository = ScoreRepository(
        AppDatabase.getInstance(application).scoreDao()
    )
    val topScores = repository.gettopScores().asLiveData()

    fun insertInfo(score: Int, genre: String, gamemode: String) {
        viewModelScope.launch {
            repository.insertInfo(score, genre, gamemode)
        }
    }
    fun clearScores() {
        viewModelScope.launch {
            repository.clearScores()
        }
    }

    fun getScoresForSharing(): String {
        val scores = topScores.value ?: emptyList()
        if (scores.isEmpty()) {
            return "Check out my high scores on Movie Quiz!\n\nNo scores yet. Play now to get on the board!"
        }

        val sb = StringBuilder()
        sb.append("Check out my high scores on Movie Quiz!\n\n")
        sb.append("Top Scores:\n")
        sb.append("\n")

        scores.forEachIndexed { index, score ->
            sb.append("${index + 1}. Score: ${score.score} | Genre: ${score.genre} | Mode: ${score.gamemode}\n")
            sb.append("Date: ${score.date}\n")
        }

        sb.append("\n")
        sb.append("Can you beat my scores? Play Movie Quiz and find out!")

        return sb.toString()
    }
}