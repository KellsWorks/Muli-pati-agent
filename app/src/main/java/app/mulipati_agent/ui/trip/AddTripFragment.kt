package app.mulipati_agent.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import app.mulipati_agent.adapters.PagerAdapter
import app.mulipati_agent.databinding.FragmentAddTripBinding


class AddTripFragment : Fragment() {

    private lateinit var addTripBinding: FragmentAddTripBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        addTripBinding = FragmentAddTripBinding.inflate(inflater, container, false)
        addTripBinding.lifecycleOwner = this

        return addTripBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addTripBinding.addTripBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setUpPagers()
    }

    private fun setUpPagers(){

        val adapter = PagerAdapter(requireActivity().supportFragmentManager)

        adapter.addFragment(VerificationFragment() , " One ")
        adapter.addFragment(TripDetailsFragment() , " two ")

        addTripBinding.addTripPager.adapter = adapter

        addTripBinding.addTripsDots.setViewPager(addTripBinding.addTripPager)

        addTripBinding.addTripPager.addOnPageChangeListener(object :
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