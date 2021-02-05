package app.mulipati_agent.epoxy.bookings

import android.content.Context
import androidx.navigation.findNavController
import app.mulipati_agent.R
import app.mulipati_agent.data.Bookings
import com.airbnb.epoxy.Typed2EpoxyController

class BookingsEpoxyController: Typed2EpoxyController<Boolean, List<Bookings>>() {
    override fun buildModels(data1: Boolean?, bookings: List<Bookings>?) {
        if (bookings != null) {
            for (book in bookings){
                BookingsEpoxyModel_()
                        .id(book.name)
                        .data(book)
                        .click { _, parentView, _, _ ->

                                parentView.bookerName?.findNavController()?.navigate(R.id.action_bookingsFragment_to_tripChatFragment)
                                val chatID = parentView.bookerName?.context?.getSharedPreferences("chatID", Context.MODE_PRIVATE)?.edit()
                                chatID?.putString("title", parentView.bookerName!!.text.toString())
                                chatID?.putInt("id", parentView.bookerName!!.text!!.toString().toInt())?.apply()

                        }
                    .addTo(this)
            }
        }
    }
}