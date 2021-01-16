package app.mulipati_agent.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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

        signInBinding.createAccount.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_registerFragment)
        }
    }
}