package com.example.journalapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log

import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil

import com.example.journalapp.databinding.ActivityAddJournalBinding
import com.example.journalapp.databinding.BottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import okhttp3.Request


class AddJournalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddJournalBinding
    private lateinit var  bottomSheetBinding: BottomSheetLayoutBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
     private lateinit var userName:String
    var selectedBitmap: Bitmap? = null
    private val CAMERA_REQUEST = 100
    private val GALLERY_REQUEST = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_journal)


        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        var username = sharedPref.getString("username", "")
         userName = username.toString()



        binding.cameraCapture.setOnClickListener {

            bottomSheetDialog = BottomSheetDialog(this)

            bottomSheetBinding = BottomSheetLayoutBinding.inflate(layoutInflater)

            bottomSheetDialog.setContentView(bottomSheetBinding.root)


            bottomSheetDialog.show()


            // camera option
            bottomSheetBinding.cameraOption.setOnClickListener {

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST)

                bottomSheetDialog.dismiss()
            }

            // gallery option
            bottomSheetBinding.galleryOption.setOnClickListener {

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"

                startActivityForResult(intent, GALLERY_REQUEST)

                bottomSheetDialog.dismiss()
            }

            // cancel option
            bottomSheetBinding.cancelOption.setOnClickListener {

                bottomSheetDialog.dismiss()
            }
        }



        // Access a Cloud Firestore instance from your Activity

        binding.saveButton.setOnClickListener {

            val title = binding.edtTitle.text.toString()
            val thoughts = binding.edtThrought.text.toString()

            if (title.isNotEmpty() && thoughts.isNotEmpty() && selectedBitmap != null) {

                binding.progressBar.visibility = View.VISIBLE

                uploadImageToCloudinary()

            } else {

                Toast.makeText(
                    this,
                    "Please enter title, thoughts and select image",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {

            // Camera result
            if (requestCode == CAMERA_REQUEST) {

                selectedBitmap = data.extras?.get("data") as Bitmap
                binding.selectedImage.setImageBitmap(selectedBitmap)

            }

            // Gallery result
            else if (requestCode == GALLERY_REQUEST) {

                val imageUri = data.data

                val inputStream = contentResolver.openInputStream(imageUri!!)
                selectedBitmap = BitmapFactory.decodeStream(inputStream)

                binding.selectedImage.setImageBitmap(selectedBitmap)
            }
        }
    }

    private fun uploadImageToCloudinary() {


            val bitmap = selectedBitmap   // camera or gallery image

            val stream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            val byteArray = stream.toByteArray()

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    "image.jpg",
                    byteArray.toRequestBody("image/*".toMediaTypeOrNull())
                )
                .addFormDataPart("upload_preset", "journal_upload")
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/dwrqzacyu/image/upload")
                .post(requestBody)
                .build()

            OkHttpClient().newCall(request).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {

                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                    }
                }

                override fun onResponse(call: Call, response: Response) {

                    val json = JSONObject(response.body!!.string())

                    val imageUrl = json.getString("secure_url")
                    Log.d("CloudinaryURL", imageUrl.toString())

                    saveToFirestore(imageUrl)
                }
            })

    }

    fun saveToFirestore(imageUrl: String) {

        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser

        val journal = JournalData(
            title = binding.edtTitle.text.toString(),
            thoughts = binding.edtThrought.text.toString(),
            timeAdded = Timestamp.now(),
            imageUrl = imageUrl,
            username = userName ?: "",
            userId = user?.uid ?: ""
        )

        db.collection("journals")
            .add(journal)
            .addOnSuccessListener {

                binding.progressBar.visibility = View.GONE

                Toast.makeText(this, "Journal Added", Toast.LENGTH_SHORT).show()

                var intent = Intent(this,JournalList::class.java)
                startActivity(intent)
            }
    }
}