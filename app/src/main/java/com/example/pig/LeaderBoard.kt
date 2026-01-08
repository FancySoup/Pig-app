package com.example.pig

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

const val  LEADERBOARD_FILE = "leaderboard.txt"

class LeaderBoard : AppCompatActivity() {
    var LeaderList = ArrayList<LeaderBoardItem>()
    private lateinit var leaderBoardListAdapter : LeaderBoardAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leader_board)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = findViewById(R.id.recycleList)
        leaderBoardListAdapter = LeaderBoardAdaptor(LeaderList)

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = leaderBoardListAdapter

        val extras = intent.extras

        if (extras != null){
            val date = extras.getString("DATE")
            val winner = extras.getString("WINNER")
            val score = extras.getString("SCORE")


            // write to a text file
            val fileOutputStream: FileOutputStream = openFileOutput(LEADERBOARD_FILE, MODE_APPEND)
            val leaderBoardFile = OutputStreamWriter(fileOutputStream)
            leaderBoardFile.write("$winner,$score,$date\n")
            leaderBoardFile.close()
        }

        readLeaderBoardData()

    }

    fun readLeaderBoardData() {
        val file = File(filesDir, LEADERBOARD_FILE)

        if (file.exists()) {
            val lines = file.readLines()

            // Clear the list before pulling in fresh data
            LeaderList.clear()

            // Read from bottom UP
            for (line in lines.reversed()) {
                val parts = line.split(",")
                if (parts.size == 3) {
                    LeaderList.add(LeaderBoardItem(parts[0], parts[1], parts[2]))
                }
            }

            leaderBoardListAdapter.notifyDataSetChanged()
        }
    }

    fun returnGame(v : View){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}