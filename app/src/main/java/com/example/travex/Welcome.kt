package com.example.travex

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class Welcome : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000 // Splash screen duration in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Delay for the splash screen and then start the main activity
        Handler().postDelayed({
            val homeIntent = Intent(this@Welcome, MainActivity::class.java)
            startActivity(homeIntent)
            finish() // Close the splash activity so that it's not shown again
        }, SPLASH_TIME_OUT)
    }
}
