package app.mulipati_agent.ui.notifications

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.mulipati.data.Notifications
import app.mulipati.util.Resource
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentNotificationsBinding
import app.mulipati_agent.epoxy.notification.NotificationsEpoxyController
import app.mulipati_agent.network.responses.notifications.Notification
import app.mulipati_agent.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var notificationsBinding: FragmentNotificationsBinding by  autoCleared()

    private lateinit var controller: NotificationsEpoxyController

    private val notificationsViewModel: NotificationsViewModel by viewModels()

    private lateinit var notificationsList: ArrayList<Notifications>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        notificationsBinding = FragmentNotificationsBinding.inflate(inflater, container, false)
        notificationsBinding.lifecycleOwner = this

        return notificationsBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationsBinding.notificationsBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        val old = ArrayList<Notifications>()

        old.add(Notifications(R.drawable.ic_bell_sleep, "Booking successful!", "You have booked a trip on 2 February 2021 at 4pm", "4 Feb, 2021 11:45AM"))
        old.add(Notifications(R.drawable.ic_bell_sleep, "Booking successful!", "You have booked a trip on 2 February 2021 at 4pm", "4 Feb, 2021 11:45AM"))

        val controllerOne = NotificationsEpoxyController()
        controllerOne.setData(true, old)

        notificationsBinding.viewedNotificationsRecycler
            .setController(controllerOne)
    }

    private fun setupObservers() {
        val getId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        notificationsViewModel.notications.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    try {
                        if (it.data?.get(0)?.user_id ==  2){
                            notificationsList = ArrayList()
                            for (notify in it.data){
                                if (notify.id == getId && notify.status == "unmarked"
                                ) {

                                    notificationsList.add(
                                        Notifications(
                                            R.drawable.ic_bell_ring, notify.title, notify.content, notify.created_at
                                        )
                                    )

                                }
                                else{
                                    Timber.e("No notifications")
                                }
                            }

                            setUpRecycler(notificationsList)
                        }
                    }catch (e: IndexOutOfBoundsException){
                        Timber.e(e)
                    }
                }

                Resource.Status.ERROR ->
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING -> {

                }
            }
        })
    }

    private fun setUpRecycler(data: List<Notifications>) {

        controller = NotificationsEpoxyController()
        controller.setData(true, data)

        notificationsBinding.recentNotificationsRecycler
            .setController(controller)

    }

}