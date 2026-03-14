package com.example.final_project_team10.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_team10.R
import com.example.final_project_team10.data.Score_Info

class ScoreAdapter : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {
    private var scores: List<Score_Info> = listOf()

    fun updateScores(newScores: List<Score_Info>) {
        scores = newScores
        notifyDataSetChanged()
    }

    override fun getItemCount() = scores.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scoreboard_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scores[position], position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rankTV: TextView = itemView.findViewById(R.id.tv_rank)
        private val scoreTV: TextView = itemView.findViewById(R.id.tv_score_value)
        private val genreTV: TextView = itemView.findViewById(R.id.tv_genre)
        private val modeTV: TextView = itemView.findViewById(R.id.tv_mode)
        private val dateTV: TextView = itemView.findViewById(R.id.tv_date)

        fun bind(entry: Score_Info, position: Int) {
            val ctx = itemView.context

            rankTV.text = ctx.getString(R.string.scoreboard_rank, position + 1)
            scoreTV.text = ctx.getString(R.string.scoreboard_score, entry.score)
            genreTV.text = ctx.getString(R.string.scoreboard_genre, entry.genre)
            modeTV.text = ctx.getString(R.string.scoreboard_mode, entry.gamemode)
            dateTV.text = ctx.getString(R.string.scoreboard_date, entry.date)
        }
    }
}