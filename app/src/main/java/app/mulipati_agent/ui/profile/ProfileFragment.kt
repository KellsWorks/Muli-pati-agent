package app.mulipati_agent.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentProfileBinding
import app.mulipati_agent.util.Constants
import com.bumptech.glide.Glide
import timber.log.Timber

class ProfileFragment : Fragment() {

    private lateinit var profileBinding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        profileBinding.lifecycleOwner = this

        return profileBinding.root

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun bindUser(){

        val user = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        profileBinding.userName.text = user?.getString("name", "")

        Glide
            .with(requireContext())
            .load(Constants.PROFILE_URL+user?.getString("photo", ""))
            .centerCrop()
            .into(profileBinding.userAvatar)


        val phoneUtil = PhoneNumberUtils.formatNumber(user?.getString("phone", ""), "MW")
        val phone =removeFirstChar(phoneUtil)

        profileBinding.userPhone.text = "+265 $phone"
    }

    private fun removeFirstChar(s: String): String? {
        return s.substring(1)
    }

}