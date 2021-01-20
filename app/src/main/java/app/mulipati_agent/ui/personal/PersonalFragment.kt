package app.mulipati_agent.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.databinding.FragmentPersonalBinding

class PersonalFragment : Fragment() {

    private lateinit var personalBinding: FragmentPersonalBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        personalBinding = FragmentPersonalBinding.inflate(inflater, container, false)
        personalBinding.lifecycleOwner = this

        return personalBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personalBinding.personalBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onBackPressed(){
        findNavController().navigateUp()
    }
}