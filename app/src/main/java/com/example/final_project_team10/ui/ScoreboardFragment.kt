package com.example.final_project_team10.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_team10.R
import com.google.android.material.button.MaterialButton

class ScoreboardFragment : Fragment(R.layout.fragment_scoreboard) {
    private val viewModel: ScoreboardViewModel by viewModels()
    private val scoreboardAdapter = ScoreAdapter()
    private lateinit var scoreboardListRV : RecyclerView
    private lateinit var clearButton: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scoreboardListRV = view.findViewById(R.id.scoreboard_list)
        clearButton = view.findViewById(R.id.btn_clear_scores)
        scoreboardListRV.layoutManager = LinearLayoutManager(requireContext())
        scoreboardListRV.adapter = scoreboardAdapter

        viewModel.topScores.observe(viewLifecycleOwner) { scores ->
            scoreboardAdapter.updateScores(scores)
        }

        clearButton.setOnClickListener {
            viewModel.clearScores()
        }
    }
}