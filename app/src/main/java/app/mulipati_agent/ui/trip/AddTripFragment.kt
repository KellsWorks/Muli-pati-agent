package app.mulipati_agent.ui.trip

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import app.mulipati_agent.R
import app.mulipati_agent.adapters.PagerAdapter
import app.mulipati_agent.databinding.FragmentAddTripBinding
import app.mulipati_agent.shared_preferences.SharedPreferences
import timber.log.Timber


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
        adapter.addFragment(PricingFragment() , " three ")
        adapter.addFragment(RideFragment() , " four ")

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
                when(position){
                    0 ->{

                        addTripBinding.nextPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 1
                        }

                    }
                    1-> {
                        addTripBinding.nextPage.text = getString(R.string.next)
                        addTripBinding.nextPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 2
                        }
                        addTripBinding.previousPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 0
                        }
                    }
                    2-> {
                        addTripBinding.nextPage.text = getString(R.string.next)
                        addTripBinding.nextPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 3
                        }
                        addTripBinding.previousPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 1
                        }
                    }
                    3->{

                        addTripBinding.nextPage.text = getString(R.string.finish)
                        addTripBinding.nextPage.setOnClickListener {
                            finishAdd()
                        }
                        addTripBinding.previousPage.setOnClickListener {
                            addTripBinding.addTripPager.currentItem = 2
                        }

                    }else->{
                    addTripBinding.previousPage.visibility = View.VISIBLE
                    addTripBinding.nextPage.visibility = View.VISIBLE
                }
                }
            }

            override fun onPageSelected(position: Int) {

            }
        })
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun finishAdd(){

        val dialog = Dialog(requireContext())
        val view = dialog.layoutInflater.inflate(R.layout.dialog_trip_summary, null)

        val price = view.findViewById<TextView>(R.id.summaryPrice)
        val route = view.findViewById<TextView>(R.id.tripRoute)
        val startTime = view.findViewById<TextView>(R.id.tripDate)

        val priceGet = SharedPreferences(requireContext()).getTripsPrefs("price", "")
        val routeGet = SharedPreferences(requireContext()).getTripsPrefs("start", "") + " to " + SharedPreferences(requireContext()).getTripsPrefs("destination", "")
        val startTimeGet = SharedPreferences(requireContext()).getTripsPrefs("start_time", "")

        Timber.e(routeGet)

        price.text = "MK$priceGet"
        route.text = routeGet
        startTime.text = startTimeGet

        dialog.setContentView(view)
        dialog.show()

        val finish = view.findViewById<Button>(R.id.addTripFinish)
        finish.setOnClickListener {
            dialog.dismiss()
        }
    }
}