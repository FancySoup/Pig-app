package com.example.pig

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.net.URI

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun imgViewOnClick ( vw: View){
val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)


    }
    fun btnSupportOnClick(vw: View) {
        val uri = Uri.parse("https://tituseggers.great-site.net/support.php")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun btnAboutOnClick ( vw: View){
        val uri = Uri.parse("https://tituseggers.great-site.net")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


}