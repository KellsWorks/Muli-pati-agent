package app.mulipati_agent.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mulipati.util.Resource
import app.mulipati_agent.db.repositories.BookingsRepository
import app.mulipati_agent.db.repositories.TripsRepository
import app.mulipati_agent.network.responses.bookings.Booking
import app.mulipati_agent.network.responses.trips.Trip

class BookingsViewModel @ViewModelInject constructor(
    repository: BookingsRepository
) : ViewModel() {

    val bookings: LiveData<Resource<List<Booking>>> = repository.getBookings()

}