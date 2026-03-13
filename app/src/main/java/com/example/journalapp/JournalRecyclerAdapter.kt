package com.example.journalapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.journalapp.databinding.JournalListItemBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class JournalRecyclerAdapter(var journalList: List<JournalData>):
    RecyclerView.Adapter<JournalRecyclerAdapter.MyViewHolder>() {



    class MyViewHolder(val binding: JournalListItemBinding) :
    RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding = JournalListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return journalList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val journal = journalList[position]
        holder.binding.journal = journal

         holder.binding.shareIcon.setOnClickListener{

             val shareText = "${journal.title}\n\n${journal.thoughts}\n\n${journal.imageUrl}"
             val intent = Intent(Intent.ACTION_SEND)

             intent.type = "text/plain"

             intent.putExtra(Intent.EXTRA_TEXT, shareText)

             holder.itemView.context.startActivity(
                 Intent.createChooser(intent, "Share via")
             )


         }
    }



}


