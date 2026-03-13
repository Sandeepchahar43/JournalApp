package com.example.journalapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journalapp.databinding.ActivityJournalListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class JournalList : AppCompatActivity() {
    private lateinit var binding: ActivityJournalListBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    lateinit var journalList: MutableList<JournalData>
    lateinit var journalRecyclerAdapter: JournalRecyclerAdapter
    lateinit var noPostText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_journal_list)


        auth = Firebase.auth
        user = auth.currentUser

    }

    override fun onStart() {
        super.onStart()

        journalList = arrayListOf()

        journalRecyclerAdapter = JournalRecyclerAdapter(journalList)

        binding.recyclerItem.setHasFixedSize(true)
        binding.recyclerItem.layoutManager = LinearLayoutManager(this)
        binding.recyclerItem.adapter = journalRecyclerAdapter   // 👈 missing line

        val db = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        db.collection("journals")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->

                journalList.clear()

                for (doc in documents) {

                    val journal = doc.toObject(JournalData::class.java)

                    journalList.add(journal)
                }

                if (journalList.isNotEmpty()) {

                    binding.textPost.visibility = View.GONE
                    binding.recyclerItem.visibility = View.VISIBLE
                }

                journalRecyclerAdapter.notifyDataSetChanged()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.plus_sign -> {
                if (user != null && auth != null) {

                    val intent = Intent(this, AddJournalActivity::class.java)
                    startActivity(intent)

                }
            }

            R.id.action_signOut -> {
                if (user != null && auth != null) {
                    auth.signOut()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }

            }


        }
        return super.onOptionsItemSelected(item)
    }
}



