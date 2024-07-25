package com.bk.sequenceapp.sequence

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bk.sequenceapp.R
import com.bk.sequenceapp.utils.GameSteps
import com.bk.sequenceapp.utils.title

class ButtonsRecyclerAdapter constructor(private val listOfSteps: List<GameSteps>,
                                         private val onItemPressed: (Int) -> Unit)
    : RecyclerView.Adapter<ButtonsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.step_button_layout,parent,false)
        return ViewHolder(view) {index ->
            onItemPressed(index)
        }
    }

    override fun getItemCount(): Int {
        return listOfSteps.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfSteps[position]
        holder.bindDataToView(item)
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View, private val onItemPressed: (Int) -> Unit) :
        RecyclerView.ViewHolder(ItemView) {

        val productImageView: AppCompatButton = itemView.findViewById(R.id.step_button)

        fun bindDataToView(step: GameSteps){
            productImageView.text = step.title()
            productImageView.setOnClickListener {
                onItemPressed(adapterPosition)
            }
        }

    }


}