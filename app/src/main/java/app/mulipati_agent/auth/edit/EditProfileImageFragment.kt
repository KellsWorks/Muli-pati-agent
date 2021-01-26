@file:Suppress("DEPRECATION")

package app.mulipati_agent.auth.edit

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import app.mulipati_agent.network.responses.Response
import app.mulipati_agent.R
import app.mulipati_agent.databinding.FragmentEditProfileImageBinding
import app.mulipati_agent.network.ApiClient
import app.mulipati_agent.network.Routes
import app.mulipati_agent.network.responses.account.ImageResponse
import app.mulipati_agent.util.Constants
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.IOException


class EditProfileImageFragment : Fragment() {

    private lateinit var editProfileImageBinding: FragmentEditProfileImageBinding

    private lateinit var bitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        editProfileImageBinding = FragmentEditProfileImageBinding.inflate(inflater, container, false)
        editProfileImageBinding.lifecycleOwner = this

        return editProfileImageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPreferences = context?.getSharedPreferences("user", Context.MODE_PRIVATE)

        update()

        Glide
            .with(requireContext())
            .load(Constants.PROFILE_URL+ userPreferences?.getString("photo", ""))
            .centerCrop()
            .into(editProfileImageBinding.editedIcon)

    }
    private fun update(){

        editProfileImageBinding.selectImage.setOnClickListener {
            uploadImage()
        }
    }

    private fun upload() {
        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Updating...")
        dialog.setCancelable(false)

        Toast.makeText(requireContext(), imageToString(bitmap).toString(), Toast.LENGTH_SHORT)
            .show()

        dialog.show()

        val api = ApiClient.client!!.create(Routes::class.java)
        val userPreferences = context?.getSharedPreferences("user", Context.MODE_PRIVATE)


        val upload: Call<ImageResponse?>? = api.photoUpdate(userPreferences?.getInt("profile_id", 0), imageToString(bitmap).toString())
        upload?.enqueue(object : Callback<ImageResponse?> {
            override fun onFailure(call: Call<ImageResponse?>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()

                Timber.d(t)
                Timber.e(imageToString(bitmap).toString())
                dialog.dismiss()
            }

            override fun onResponse(call: Call<ImageResponse?>, response: retrofit2.Response<ImageResponse?>) {

                Toast.makeText(
                    requireContext(),
                    "Success",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()

                val user = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.edit()

                user?.putString("photo", response.body()?.photo)
                user?.apply()

                findNavController().navigate(R.id.action_editProfileImageFragment_to_personalEditFragment)

                dialog.dismiss()
            }

        })
    }

    private fun uploadImage(){

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, Constants.IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.IMAGE_REQUEST && resultCode == RESULT_OK && data != null){

            val path: Uri? = data.data
            try {
                bitmap = getBitmap(requireContext().contentResolver, path)
                editProfileImageBinding.iconName.text = "${data.data?.path}"

                editProfileImageBinding.editedIcon.setImageBitmap(bitmap)

            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun imageToString(bitmap: Bitmap): Any {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        val imgBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return encodeToString(imgBytes, DEFAULT)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        editProfileImageBinding.toEditPersonal.setOnClickListener {
            upload()
        }

        editProfileImageBinding.iconEditBack.setOnClickListener {
            findNavController().navigateUp()
        }

        editProfileImageBinding.skipEditIcon.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileImageFragment_to_personalEditFragment)
        }
    }
}