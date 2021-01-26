package app.mulipati_agent.ui.profile

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentProfileBinding
import app.mulipati_agent.util.Constants
import com.bumptech.glide.Glide
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var profileBinding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        profileBinding.lifecycleOwner = this

        return profileBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigate()
        bindUser()
    }

    private fun navigate(){

        profileBinding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileImageFragment)
        }

        profileBinding.notifictions.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_notificationsFragment)
        }

        profileBinding.toSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        profileBinding.toSubscription.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_subscriptionsFragment)
        }

        profileBinding.toTerms.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_termsFragment)
        }

        profileBinding.toSupport.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_supportFragment)
        }

        profileBinding.toPersonal.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_personalFragment)
        }
    }

    private fun bindUser(){

        val user = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        profileBinding.userName.text = user?.getString("name", "")
        profileBinding.userPhone.text = user?.getString("phone", "")

        Glide
            .with(requireContext())
            .load(Constants.PROFILE_URL+user?.getString("photo", ""))
            .centerCrop()
            .into(profileBinding.userAvatar)

    }

}