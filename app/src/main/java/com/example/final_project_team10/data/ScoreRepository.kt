package com.example.final_project_team10.data

import androidx.lifecycle.LiveData

class ScoreRepository(
    private val dao: ScoreDao
) {
    suspend fun insertInfo(score: Int, genre: String, gamemode: String) {
        dao.insertScore(Score_Info(score = score, genre = genre, gamemode = gamemode))
    }
    suspend fun clearScores() {
       dao.clearScores()
    }
    fun gettopScores()= dao.getTopScores()
}