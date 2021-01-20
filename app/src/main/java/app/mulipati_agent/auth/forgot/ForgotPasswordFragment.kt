package app.mulipati_agent.auth.forgot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentForgotPasswordBinding


class ForgotPasswordFragment : Fragment() {

    private lateinit var forgotPasswordBinding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        forgotPasswordBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        forgotPasswordBinding.lifecycleOwner = this

        return forgotPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forgotPasswordBinding.forgotPasswordBack.setOnClickListener {
            findNavController().navigateUp()
        }

        forgotPasswordBinding.sendSMS.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_forgotPasswordResetFragment)
        }
    }
}