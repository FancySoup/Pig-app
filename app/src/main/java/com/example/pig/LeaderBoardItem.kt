package com.example.pig

import android.content.Context

class LeaderBoardItem(
    var winner: String,
    var score: String,
    var date: String
) {

    fun toCSV(): String {
        return "$winner,$score,$date\n"
    }

    // Format leaderboard line with aligned colon + aligned dates
    override fun toString(): String {
        val padding = if (winner == "You") {
            "You             "
        } else if (winner == "Computer") {
            "Computer  "
        } else { // If another language
            winner
        }

        return "$padding: $score     $date"
    }

    fun computerWon(context: Context): Boolean {
        return winner == context.getString(R.string.computer)
    }
}
