package app.mulipati_agent.auth.forgot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentForgotPasswordResetBinding


class ForgotPasswordResetFragment : Fragment() {

    private lateinit var forgotPasswordResetBinding: FragmentForgotPasswordResetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        forgotPasswordResetBinding = FragmentForgotPasswordResetBinding.inflate(inflater, container, false)
        forgotPasswordResetBinding.lifecycleOwner = this

        return forgotPasswordResetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forgotPasswordResetBinding.forgotPasswordReset.setOnClickListener {
            findNavController().navigateUp()
        }

        forgotPasswordResetBinding.confirmSMS.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordResetFragment_to_resetPasswordFragment)
        }
    }
}