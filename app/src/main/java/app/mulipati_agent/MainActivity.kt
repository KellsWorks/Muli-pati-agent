@file:Suppress("DEPRECATION")

package app.mulipati_agent

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import app.mulipati_agent.databinding.ActivityMainBinding
import app.mulipati_agent.network.BackgroundServices
import com.google.firebase.iid.FirebaseInstanceId.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpNavigation()

        val id = getSharedPreferences("user", Context.MODE_PRIVATE).getInt("id", 0)

        getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            BackgroundServices().sendTokenToServer(instanceIdResult.token, id)
            Timber.e(instanceIdResult.token)
        }

    }

    private fun setUpNavigation(){

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomMenu.setupWithNavController(navController)

    }
}