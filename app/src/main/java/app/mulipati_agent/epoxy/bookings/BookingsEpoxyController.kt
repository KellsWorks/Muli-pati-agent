package app.mulipati_agent.epoxy.bookings

import app.mulipati_agent.data.Bookings
import com.airbnb.epoxy.Typed2EpoxyController

class BookingsEpoxyController: Typed2EpoxyController<Boolean, List<Bookings>>() {
    override fun buildModels(data1: Boolean?, bookings: List<Bookings>?) {
        if (bookings != null) {
            for (book in bookings){
                BookingsEpoxyModel_()
                        .id(book.id)
                        .data(book)
                        .addTo(this)
            }
        }
    }
}