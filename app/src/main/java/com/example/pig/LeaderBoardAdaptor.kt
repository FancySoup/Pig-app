package com.example.pig

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

internal class LeaderBoardAdaptor(private var leaderBoardList: List<LeaderBoardItem>) :
    RecyclerView.Adapter<LeaderBoardAdaptor.MyViewHolder>() {

    internal class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var item: TextView = view.findViewById(R.id.leaderBoardItemText)
    }

    @NonNull
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.leader_board_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val leaderBoardItem = leaderBoardList[position]

        // Use your formatted string
        holder.item.text = leaderBoardItem.toString()

        // Determine win color
        val isComputer = leaderBoardItem.computerWon(holder.item.context)

        if (isComputer) {
            holder.item.setTextColor(Color.RED)   // computer wins
        } else {
            holder.item.setTextColor(Color.BLUE)  // you win
        }
    }

    override fun getItemCount(): Int {
        return leaderBoardList.size
    }
}
