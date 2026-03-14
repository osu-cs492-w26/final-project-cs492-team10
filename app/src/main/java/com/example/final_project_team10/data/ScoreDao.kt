package com.example.final_project_team10.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Insert
    suspend fun insertScore(score: Score_Info)

    @Query("SELECT * FROM scores ORDER BY score DESC LIMIT 10")
    fun getTopScores(): Flow<List<Score_Info>>

    @Query("DELETE FROM scores")
    suspend fun clearScores()
}