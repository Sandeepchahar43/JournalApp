package com.example.journalapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.journalapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
         binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val db = Firebase.firestore
        auth = FirebaseAuth.getInstance()



        binding.btnLogin.setOnClickListener{
            callLoginFun()
        }

        binding.accAccount.setOnClickListener{
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser





        if (currentUser != null) {
            var intent = Intent(this,JournalList::class.java)
            startActivity(intent)
        }
    }

    private fun callLoginFun() {
        val email = binding.emailId.text.toString()
        val password = binding.edtPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        // Login success
                        val intent = Intent(this, JournalList::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        Toast.makeText(
                            this,
                            "Login failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        } else {

            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
        }
    }


}