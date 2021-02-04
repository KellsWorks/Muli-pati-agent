package app.mulipati_agent.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mulipati.util.Resource
import app.mulipati_agent.db.repositories.TripsRepository
import app.mulipati_agent.network.responses.trips.Trip

class TripsViewModel @ViewModelInject constructor(
    repository: TripsRepository
) : ViewModel() {

    val trips: LiveData<Resource<List<Trip>>> = repository.getTrips()

}