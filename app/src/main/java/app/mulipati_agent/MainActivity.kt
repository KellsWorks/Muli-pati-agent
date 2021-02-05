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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpNavigation()

        val id = getSharedPreferences("user", Context.MODE_PRIVATE).getInt("fcmId", 0)

        getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            BackgroundServices().sendTokenToServer(instanceIdResult.token, id)
            Timber.e(instanceIdResult.token)
        }

        newDay()

    }

    private fun setUpNavigation(){

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomMenu.setupWithNavController(navController)

    }

    private fun newDay(){

        GlobalScope.launch(Dispatchers.IO) {

            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)

            if (hour == 0){
                getSharedPreferences("tripsCount", Context.MODE_PRIVATE)?.edit()?.clear()?.apply()
                getSharedPreferences("tripsCount", Context.MODE_PRIVATE)?.edit()?.putInt("count", 0)?.apply()
            }

        }
    }
}