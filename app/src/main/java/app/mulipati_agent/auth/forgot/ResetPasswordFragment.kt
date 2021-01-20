package app.mulipati_agent.auth.forgot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.MainActivity
import app.mulipati_agent.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {

    private lateinit var resetPasswordBinding: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        resetPasswordBinding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        resetPasswordBinding.lifecycleOwner = this

        return resetPasswordBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetPasswordBinding.passwordToSign.setOnClickListener {
            requireActivity()
                .startActivity(
                    Intent(
                        requireActivity(), MainActivity::class.java
                    )
                )
            requireActivity()
                .overridePendingTransition(
                    android.R.anim.slide_out_right, android.R.anim.slide_in_left
                )
        }

        resetPasswordBinding.resetPasswordBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}