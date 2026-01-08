package com.example.pig

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    lateinit var txtPlayerWon: TextView
    lateinit var txtPlayerScore: TextView
    lateinit var txtPlayerTurn: TextView
    lateinit var txtComWon: TextView
    lateinit var txtComScore: TextView
    lateinit var txtComTurn: TextView
    lateinit var txtDiceTotal: TextView
    lateinit var btnRoll: Button
    lateinit var btnHold: Button
    lateinit var imgDiceOne: ImageView
    lateinit var imgDiceTwo: ImageView
    lateinit var imgWin: ImageView
    lateinit var imgLose: ImageView

    lateinit var txtPlayer: TextView
    lateinit var txtComputer: TextView
    lateinit var  btnLeaderBoardLayout: Button


    var playerTurnTotal = 0
    var playerTotalScore = 0
    var comTurnTotal = 0
    var comTotalScore = 0

    var playerWins = 0
    var computerWins = 0

    var computerJustWent = false

    var debugDice = false // Dice can't roll a 1, computer always rolls 5 or 6
    var minRoll = 1

    var computerRolledOne = false

    var winner= ""
    var finalScore = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initApplication()
        btnHold.isEnabled = false
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        txtPlayer.setBackgroundColor(Color.CYAN)

    }

    override fun onSaveInstanceState(savedState: Bundle) {
        super.onSaveInstanceState(savedState)
        savedState.putInt("PLAYER_TOTAL_SCORE", playerTotalScore)
        savedState.putInt("PLAYER_TURN_TOTAL", playerTurnTotal)
        savedState.putInt("COM_TOTAL_SCORE", comTotalScore)
        savedState.putInt("COM_TURN_TOTAL", comTurnTotal)
        savedState.putInt("PLAYER_WINS", playerWins)
        savedState.putInt("COMPUTER_WINS", computerWins)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        playerTotalScore = savedInstanceState.getInt("PLAYER_TOTAL_SCORE", 0)
        playerTurnTotal = savedInstanceState.getInt("PLAYER_TURN_TOTAL", 0)
        // If player rotates while computer is going, it forces a hold from the computer
        comTotalScore = savedInstanceState.getInt("COM_TOTAL_SCORE", 0) + savedInstanceState.getInt("COM_TURN_TOTAL", 0)
        playerWins = savedInstanceState.getInt("PLAYER_WINS", 0)
        computerWins = savedInstanceState.getInt("COMPUTER_WINS", 0)

        txtPlayerScore.text = playerTotalScore.toString()
        txtPlayerTurn.text = playerTurnTotal.toString()
        txtComScore.text = comTotalScore.toString()
        txtComTurn.text = comTurnTotal.toString()
        txtPlayerWon.text = playerWins.toString()
        txtComWon.text = computerWins.toString()

    }



    fun initApplication() {
        txtPlayerWon = findViewById(R.id.txtPlayerWon)
        txtPlayerScore = findViewById(R.id.txtPlayerScore)
        txtPlayerTurn = findViewById(R.id.txtPlayerTurn)
        txtComWon = findViewById(R.id.txtComWon)
        txtComScore = findViewById(R.id.txtComScore)
        txtComTurn = findViewById(R.id.txtComTurn)
        txtDiceTotal = findViewById(R.id.txtDiceTotal)
        btnRoll = findViewById(R.id.btnRoll)
        btnHold = findViewById(R.id.btnHold)
        imgDiceOne = findViewById(R.id.imgDiceOne)
        imgDiceTwo = findViewById(R.id.imgDiceTwo)
        txtPlayer = findViewById(R.id.txtPlayer)
        txtComputer = findViewById(R.id.txtComputer)
        imgWin = findViewById(R.id.imgWin)
        imgLose = findViewById(R.id.imgLose)
        btnLeaderBoardLayout = findViewById(R.id.btnLeaderBoard)

    }



    fun diceImages(rollOne : Int, rollTwo : Int){
        // Image for dice 1
        val diceOneResource = when (rollOne) { // Image for dice 1
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        // Image for dice 2
        val diceTwoResource = when (rollTwo) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        imgDiceOne.setImageResource(diceOneResource)
        imgDiceTwo.setImageResource(diceTwoResource)
    }

    fun playersTurn (){
        var rolledOne = false
        if (debugDice) {
            minRoll = 2
        }
        val diceOneRoll = (minRoll..6).random()
        val diceTwoRoll = (minRoll..6).random()
        diceImages(diceOneRoll, diceTwoRoll)

        if (diceOneRoll == 1 && diceTwoRoll == 1) {
            // THE PLAYER ROLLED 2 NUMBER 1s
            playerTotalScore = 0 // Reset
            playerTurnTotal = 0 // Reset

            txtPlayerTurn.text = applicationContext.getString(R.string.zero)
            txtPlayerScore.text = applicationContext.getString(R.string.zero)
            txtDiceTotal.text = applicationContext.getString(R.string.rolled_both_1s)
            Toast.makeText(this, applicationContext.getString(R.string.rolled_both_1s), Toast.LENGTH_SHORT).show()

            rolledOne = true

        } else if ((diceOneRoll == 1 && diceTwoRoll != 1) || (diceOneRoll != 1 && diceTwoRoll == 1)) {

            // THE PLAYER ROLLED 1 NUMBER 1
            playerTurnTotal = 0
            txtPlayerTurn.text = applicationContext.getString(R.string.zero)
            txtDiceTotal.text = applicationContext.getString(R.string.rolled_a_1)
            rolledOne = true

            Toast.makeText(this, applicationContext.getString(R.string.rolled_a_1), Toast.LENGTH_SHORT).show()




        } else {
            // THE PLAYER DIDN't ROLL A 1
            btnHold.isEnabled = true

            playerTurnTotal = playerTurnTotal + diceOneRoll + diceTwoRoll
            txtPlayerTurn.text = playerTurnTotal.toString()
            txtDiceTotal.text = applicationContext.getString(R.string.you_rolled, diceOneRoll + diceTwoRoll)
        }
        if (rolledOne){
            btnHold.isEnabled = false
            computerJustWent = false
            onHoldClick(btnHold)
        }
        computerJustWent = false


    }

    fun computersTurn (){
        btnHold.isEnabled = false
        computerJustWent = true

        var rolledOne = false
        if (debugDice) {
            minRoll = 5
        }



        val diceOneRoll = (minRoll..6).random()
        val diceTwoRoll = (minRoll..6).random()
        diceImages(diceOneRoll, diceTwoRoll)

        if (diceOneRoll == 1 && diceTwoRoll == 1) {
            // THE Computer ROLLED 2 NUMBER 1s
            comTotalScore = 0 // Reset
            comTurnTotal = 0 // Reset

            txtComTurn.text = applicationContext.getString(R.string.zero)
            txtComScore.text = applicationContext.getString(R.string.zero)
            txtDiceTotal.text = applicationContext.getString(R.string.computer_both_dice)
            rolledOne = true

        } else if ((diceOneRoll == 1 && diceTwoRoll != 1) || (diceOneRoll != 1 && diceTwoRoll == 1)) {

            // THE Computer ROLLED 1 NUMBER 1
            comTurnTotal = 0
            txtComTurn.text = applicationContext.getString(R.string.zero)
            txtDiceTotal.text = applicationContext.getString(R.string.computer_rolled_one)
            rolledOne = true
        } else {
            // THE Computer DIDN't ROLL A 1
            comTurnTotal = comTurnTotal + diceOneRoll + diceTwoRoll
            txtComTurn.text = comTurnTotal.toString()
            txtDiceTotal.text = applicationContext.getString(R.string.computer_rolled, diceOneRoll + diceTwoRoll)
        }
        if (rolledOne){
            computerRolledOne = true
        }


    }

    fun onRollClick(v: View) { // Probably didn't need this function
        playersTurn()
    }


    fun onHoldClick(v : View) {
        var playerJustWon = false


        playerTotalScore = playerTotalScore + playerTurnTotal
        playerTurnTotal = 0
        txtPlayerTurn.text = applicationContext.getString(R.string.zero)
        txtPlayerScore.text = playerTotalScore.toString()
        txtDiceTotal.text = applicationContext.getString(R.string.hold_text)
        btnHold.isEnabled = true
        btnRoll.isEnabled = true

        playerJustWon = checkWin()

        if (!computerJustWent && !playerJustWon) {
            btnHold.isEnabled = false
            btnRoll.isEnabled = false

            // How fast the computer rolls
            object : CountDownTimer(2550, 850) {
                override fun onTick(millisUntilFinished: Long) {
                    txtPlayer.setBackgroundColor(Color.WHITE)
                    txtComputer.setBackgroundColor(Color.CYAN)

                    if (!computerRolledOne) {
                        computersTurn()
                    }


                }

                override fun onFinish() {
                    txtPlayer.setBackgroundColor(Color.CYAN)
                    txtComputer.setBackgroundColor(Color.WHITE)

                    comTotalScore = comTotalScore + comTurnTotal
                    comTurnTotal = 0
                    txtComTurn.text = applicationContext.getString(R.string.zero)
                    txtComScore.text = comTotalScore.toString()

                    checkWin()

                    btnRoll.isEnabled = true
                    computerRolledOne = false

                }
            }.start()


        }



    }



    fun checkWin() : Boolean {
        when {
            playerTotalScore >= 100 -> {
                Toast.makeText(this, applicationContext.getString(R.string.you_win), Toast.LENGTH_LONG).show()
                winner = applicationContext.getString(R.string.you)
                finalScore = playerTotalScore.toString()
                imgWin.isVisible = true
                btnRoll.isEnabled = false
                btnHold.isEnabled = false
                playerWins++
                txtPlayerWon.text = playerWins.toString()
                return true
            }
            comTotalScore >= 100 -> {
                Toast.makeText(this, applicationContext.getString(R.string.computer_wins), Toast.LENGTH_LONG).show()
                winner = applicationContext.getString(R.string.computer)
                finalScore = comTotalScore.toString()
                imgLose.isVisible = true
                btnRoll.isEnabled = false
                btnHold.isEnabled = false
                computerWins++
                txtComWon.text = computerWins.toString()
                return true
            }
        }
        return  false
    }

    fun endingImg (v : View) {
        var intent = Intent(this, LeaderBoard::class.java)
        val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
        val formattedDate= LocalDateTime.now().format(formatter)

        intent.putExtra("WINNER", winner)
        intent.putExtra("SCORE", finalScore)
        intent.putExtra("DATE", formattedDate)

        startActivity(intent)


        startActivity(intent)
        imgWin.isVisible = false
        imgLose.isVisible = false
        playerTotalScore = 0
        comTotalScore = 0
        txtPlayerScore.text = applicationContext.getString(R.string.zero)
        txtComScore.text = applicationContext.getString(R.string.zero)
        txtDiceTotal.text = applicationContext.getString(R.string.zero)
        txtPlayerTurn.text = applicationContext.getString(R.string.zero)
        txtComTurn.text = applicationContext.getString(R.string.zero)
        btnRoll.isEnabled = true
    }

    fun leaderBoard(v : View){
        var intent = Intent(this, LeaderBoard::class.java)
        startActivity(intent)
    }


}