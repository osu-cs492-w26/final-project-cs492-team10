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
}