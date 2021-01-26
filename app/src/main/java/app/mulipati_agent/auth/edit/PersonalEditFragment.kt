@file:Suppress("DEPRECATION")

package app.mulipati_agent.auth.edit

import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.network.responses.AccountUpdateResponse
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentPersonalEditBinding
import app.mulipati_agent.network.ApiClient
import app.mulipati_agent.network.Routes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalEditFragment : Fragment() {

    private lateinit var profileEditBinding: FragmentPersonalEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileEditBinding = FragmentPersonalEditBinding.inflate(inflater, container, false)
        profileEditBinding.lifecycleOwner = this

        return profileEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileEditBinding.personalEditBack.setOnClickListener {
            findNavController().navigateUp()
        }
        profileEditBinding.skipEditPersonal.setOnClickListener {
            findNavController().navigate(R.id.action_personalEditFragment_to_profileFragment)
        }
        profileEditBinding.editPersonal.setOnClickListener {
            if (validate()){
                updateAccount(
                    profileEditBinding.editName.text.toString(),
                    profileEditBinding.editEmail.text.toString(),
                    profileEditBinding.editPhoneNumber.text.toString()
                )
            }

        }
    }

    private fun validate(): Boolean{

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        if (profileEditBinding.editName.text.toString().isEmpty()){
            Toast.makeText(
                requireContext(), "This field is required", Toast.LENGTH_SHORT
            ).show()
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }
        if (profileEditBinding.editPhoneNumber.text!!.length != 10){
            Toast.makeText(
                requireContext(), "Enter a 10 digit Malawian number", Toast.LENGTH_SHORT
            ).show()
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }
        if (profileEditBinding.editEmail.text.isNullOrEmpty() ){
            Toast.makeText(
                requireContext(), "This field is required", Toast.LENGTH_SHORT
            ).show()

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }
        return true
    }

    private fun updateAccount(
        name: String,
        email: String,
        phone: String
    ) {
        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Updating...")
        dialog.setCancelable(false)

        dialog.show()

        val user = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = user?.getInt("profile_id", 0)


        val api = ApiClient.client!!.create(Routes::class.java)

        val account: Call<AccountUpdateResponse?>? = api.accountUpdate(id, name, email, phone)
        account?.enqueue(object : Callback<AccountUpdateResponse?> {
            override fun onFailure(call: Call<AccountUpdateResponse?>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "A network error occurred",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

            override fun onResponse(
                call: Call<AccountUpdateResponse?>,
                response: Response<AccountUpdateResponse?>
            ) {

                when (response.code()) {
                    200 -> {

                        val userPref =
                            context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.edit()
                        userPref?.putString("name", response.body()?.user?.name)
                        userPref?.putString("phone", response.body()?.user?.phone)
                        userPref?.putString("email", response.body()?.profile?.email)
                        userPref?.apply()

                        Toast.makeText(
                            requireContext(),
                            "Update success!",
                            Toast.LENGTH_SHORT
                        ).show()

                        dialog.dismiss()

                        findNavController().navigate(R.id.action_personalEditFragment_to_profileFragment)
                    }
                    500 -> {

                        Toast.makeText(
                            requireContext(),
                            "A server error occurred",
                            Toast.LENGTH_SHORT
                        ).show()

                        dialog.dismiss()
                    }
                }

            }

        })
    }

}