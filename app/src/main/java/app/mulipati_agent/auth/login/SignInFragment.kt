@file:Suppress("DEPRECATION")

package app.mulipati_agent.auth.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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
import app.mulipati_agent.MainActivity
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentSignInBinding
import app.mulipati_agent.network.ApiClient
import app.mulipati_agent.network.Routes
import app.mulipati_agent.network.responses.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val token = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("token", "")

        if (token!!.isNotEmpty()){
            findNavController().navigate(R.id.action_signInFragment_to_subscribeFragment)
        }

        navigate()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        signInBinding.SignInButton.setOnClickListener {
            if (validate()){
                loginUser(
                    signInBinding.signInPhone.text.toString(),
                    signInBinding.signInPassword.text.toString()
                )
            }
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

    private fun loginUser(
        user_phone: String,
        user_pass: String
    ){
        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Signing in...")
        dialog.setCancelable(false)

        dialog.show()

        val api = ApiClient.client!!.create(Routes::class.java)

        val register: Call<User?>? = api.login(user_phone, user_pass)
        register?.enqueue(object : Callback<User?> {
            override fun onFailure(call: Call<User?>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

            override fun onResponse(call: Call<User?>, response: Response<User?>) {

                if (response.code() == 422 ){
                    Toast.makeText(
                        requireContext(),
                        "Error logging in",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }else if (response.code() == 200){
                    Toast.makeText(
                        requireContext(),
                        "Success",
                        Toast.LENGTH_SHORT
                    ).show()

                    dialog.dismiss()

                    val userPreferences = context?.getSharedPreferences(
                        "user", Context.MODE_PRIVATE
                    )?.edit()

                    response.body()?.id?.let { userPreferences?.putInt("id", it) }
                    response.body()?.profile?.get(0)?.id.let {
                        if (it != null) {
                            userPreferences?.putInt("profile_id", it)
                        }
                    }
                    userPreferences?.putString("token", response.body()?.token)
                    userPreferences?.putString("name", response.body()?.name)
                    userPreferences?.putString("phone", response.body()?.phone)
                    userPreferences?.putString("photo", response.body()?.profile?.get(0)?.photo)
                    userPreferences?.putString("email", response.body()?.profile?.get(0)?.email)
                    userPreferences?.putString("role", response.body()?.profile?.get(0)?.role)
                    userPreferences?.putString("location", response.body()?.profile?.get(0)?.location)
                    userPreferences?.putString("membership", response.body()?.membership)

                    userPreferences?.apply()

                    findNavController().navigate(R.id.action_signInFragment_to_subscribeFragment)

                    dialog.dismiss()
                }


            }

        })
    }

    private fun validate(): Boolean{

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        if (signInBinding.signInPhone.text.toString().isEmpty()){
            signInBinding.signInPhoneLayout.isErrorEnabled = true
            signInBinding.signInPhoneLayout.error = "This field is required"

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }

        if (signInBinding.signInPassword.text.toString().isEmpty()){
            signInBinding.signInPasswordLayout.isErrorEnabled = true
            signInBinding.signInPasswordLayout.error = "Enter your password"

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }

        return true
    }
}