package com.example.contactsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.databinding.ItemBinding

class HomeAdapter(var myList: MutableList<ContactItem>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val contact = myList[position]
        holder.binding.tvName.text = contact.name
        holder.binding.tvEmail.text = contact.email
        holder.binding.tvPhoneNumber.text = contact.phone
         holder.binding.imageDelete.setOnClickListener {
             deleteItem(position)
             (holder.itemView.context as HomeActivity).updateUi()

         }



    }

    override fun getItemCount(): Int =  myList.size



    fun addNewItem(item: ContactItem){
        myList.add(item)
    }

    fun deleteItem(position: Int) {
        if (position >= 0 && position < myList.size) {
            myList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, myList.size)
        }
    }

    fun setContactList(list: MutableList<ContactItem>) {
        myList = list
    }



    class ViewHolder(val binding : ItemBinding) : RecyclerView.ViewHolder(binding.root)
}