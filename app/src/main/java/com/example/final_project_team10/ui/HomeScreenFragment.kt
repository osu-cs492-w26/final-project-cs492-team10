package com.example.final_project_team10.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.final_project_team10.R

class HomeScreenFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scoreboardButton: Button = view.findViewById(R.id.button_scoreboard)
        val startGameButton: Button = view.findViewById(R.id.button_start_game)
        val optionsButton: Button = view.findViewById(R.id.button_options)

        startGameButton.setOnClickListener {
            Log.d("Start Game Button", "Navigating to Game Page")
            val directions = HomeScreenFragmentDirections.navigateToGamePage()
            findNavController().navigate(directions)
        }

        optionsButton.setOnClickListener {
            Log.d("Open settings", "Navigating to Settings Page")
            val directions = HomeScreenFragmentDirections.navigateToSettingsPage()
            findNavController().navigate(directions)
        }

        scoreboardButton.setOnClickListener {
            Log.d("Open scoreboard", "Navigating to Scoreboard Page")
            val directions = HomeScreenFragmentDirections.navigateToScoreboardPage()
            findNavController().navigate(directions)
        }
    }
}