package com.example.zpam

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter(val userId: String, val entryId: String) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList: List<Doctor> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view, userId, entryId)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setUserList(userList: List<Doctor>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View, private val userId: String, private val entryId: String) : RecyclerView.ViewHolder(itemView) {
        private val userImageView: ImageView = itemView.findViewById(R.id.userImageView)
        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val specializationTextView: TextView = itemView.findViewById(R.id.specializationTextView)
        private val doctorAddressTextView: TextView = itemView.findViewById(R.id.doctor_address)
        private val doctorPhoneTextView: TextView = itemView.findViewById(R.id.doctor_phone)

        fun bind(user: Doctor) {
            /*Glide.with(itemView.context)
                .load(user.imageUrl)
                .placeholder(R.drawable.no_foto)
                .into(userImageView)*/
            usernameTextView.text = user.name
            specializationTextView.text = user.Surname
            doctorAddressTextView.text = user.Address
            doctorPhoneTextView.text = user.Mail


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, WybranyLekarzActivity::class.java)
                intent.putExtra("userId", userId) // przekazuje userId
                intent.putExtra("entryId", entryId) // przekazuje entryId
                intent.putExtra("doctorId", user.id) // przekazuje doctorId
                itemView.context.startActivity(intent)
            }
        }
    }
}
