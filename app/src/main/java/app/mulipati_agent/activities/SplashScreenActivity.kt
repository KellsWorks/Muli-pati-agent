package app.mulipati_agent.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.mulipati_agent.MainActivity
import app.mulipati_agent.onboard.OnBoarding

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("token", "")

        if (token != ""){
            startActivity(Intent(
                this, MainActivity::class.java
            ))
            overridePendingTransition(
                android.R.anim.slide_out_right, android.R.anim.slide_in_left
            )
        }else{
            startActivity(Intent(this, OnBoarding::class.java))

        }
    }
}