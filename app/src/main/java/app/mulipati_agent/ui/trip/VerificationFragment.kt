package app.mulipati_agent.ui.trip

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.mulipati_agent.databinding.FragmentVerificationBinding
import app.mulipati_agent.shared_preferences.SharedPreferences
import com.google.android.material.snackbar.Snackbar


class VerificationFragment : Fragment() {

    private lateinit var verificationBinding: FragmentVerificationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        verificationBinding = FragmentVerificationBinding.inflate(inflater, container, false)
        verificationBinding.lifecycleOwner = this

        return verificationBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (verificationBinding.getUserCred.isChecked){
            val id = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
            SharedPreferences(requireContext()).addTripPrefs("user_id", id.toString())
        }else{
            Snackbar.make(requireView(), "Credentials are required", Snackbar.LENGTH_SHORT).show()
        }

    }

}