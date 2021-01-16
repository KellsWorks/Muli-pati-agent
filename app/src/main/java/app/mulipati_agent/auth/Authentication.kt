package app.mulipati_agent.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.mulipati_agent.R
import app.mulipati_agent.databinding.ActivityAuthenticationBinding

class Authentication : AppCompatActivity() {

    private lateinit var authenticationBinding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authenticationBinding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)
    }
}