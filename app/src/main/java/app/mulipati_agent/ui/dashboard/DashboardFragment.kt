package app.mulipati_agent.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.mulipati_agent.R
import app.mulipati_agent.data.Trips
import app.mulipati_agent.databinding.FragmentDashboardBinding
import app.mulipati_agent.epoxy.TripsEpoxyController


class DashboardFragment : Fragment() {

    private lateinit var dashboardBinding: FragmentDashboardBinding
    private lateinit var controller: TripsEpoxyController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        dashboardBinding.lifecycleOwner = this

        return dashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trips = ArrayList<Trips>()

        trips.add(Trips(R.drawable.mazda_demio, "Jerome's trip", "Chinyonga - Lunzu", "MK300"))
        trips.add(Trips(R.drawable.mazda_demio, "Jerome's trip", "Chinyonga - Lunzu", "MK300"))
        trips.add(Trips(R.drawable.mazda_demio, "Jerome's trip", "Chinyonga - Lunzu", "MK300"))
        trips.add(Trips(R.drawable.mazda_demio, "Jerome's trip", "Chinyonga - Lunzu", "MK300"))

        controller = TripsEpoxyController()
        controller.setData(false, trips)

        dashboardBinding.tripsRecycler
                .setController(controller)
    }

}