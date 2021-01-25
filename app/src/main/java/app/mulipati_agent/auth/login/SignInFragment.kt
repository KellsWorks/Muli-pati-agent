package app.mulipati_agent.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.MainActivity
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var signInBinding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signInBinding = FragmentSignInBinding.inflate(inflater, container, false)
        signInBinding.lifecycleOwner = this

        return signInBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigate()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val tokenEdit = requireActivity()
            .getSharedPreferences("remember_token", Context.MODE_PRIVATE)


        if (tokenEdit.getString("token", "")?.isNotEmpty()!!){

            findNavController().navigate(R.id.action_signInFragment_to_subscribeFragment)

            Toast.makeText(
                requireContext(),
                "You are already registered...",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun navigate(){
        signInBinding.forgotPassword.setOnClickListener {
            findNavController().navigate(
                R.id.action_signInFragment_to_forgotPasswordFragment
            )
        }

        signInBinding.SignInButton.setOnClickListener {
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

        signInBinding.createAccount.setOnClickListener {
            findNavController().navigate(
                R.id.action_signInFragment_to_registerFragment
            )
        }
    }
}