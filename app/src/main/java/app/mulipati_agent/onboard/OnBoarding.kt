package app.mulipati_agent.onboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import app.mulipati_agent.R
import app.mulipati_agent.adapters.PagerAdapter
import app.mulipati_agent.auth.Authentication
import app.mulipati_agent.databinding.ActivityOnBoardingBinding

class OnBoarding : AppCompatActivity() {

    private lateinit var onBoardingBinding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBoardingBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)

        setupViewPager()

        val preferences = getSharedPreferences("onBoard", Context.MODE_PRIVATE)
        val check: String = preferences.getString("isFirstTime", "").toString()

        if (check == "yes"){
            val intent = Intent(this, Authentication::class.java)
            startActivity(intent)
            finish()
        }else{
            val editor = preferences.edit()
            editor.putString("isFirstTime", "yes")
            editor.apply()
        }

    }


    private fun setupViewPager() {

        val adapter = PagerAdapter(supportFragmentManager)

        adapter.addFragment(FirstSlider() , " One ")
        adapter.addFragment(SecondSlider(), "two")
        adapter.addFragment(ThirdSlider(), "third")


        onBoardingBinding.onBoardPager.adapter = adapter

        onBoardingBinding.dotsIndicator.setViewPager(onBoardingBinding.onBoardPager)

        onBoardingBinding.onBoardPager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

            }
        })


    }
}