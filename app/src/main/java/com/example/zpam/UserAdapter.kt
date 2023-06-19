package com.example.zpam

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList: List<User> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setUserList(userList: List<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImageView: ImageView = itemView.findViewById(R.id.userImageView)
        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val specializationTextView: TextView = itemView.findViewById(R.id.specializationTextView)
        private val doctorAddressTextView: TextView = itemView.findViewById(R.id.doctor_address)
        private val doctorPhoneTextView: TextView = itemView.findViewById(R.id.doctor_phone)
        private val doctorWybieramButton: Button = itemView.findViewById(R.id.doctor_wybieram)

        init {
            doctorWybieramButton.setOnClickListener {
                val intent = Intent(itemView.context, WybranyLekarzActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }

        fun bind(user: User) {
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
                //intent.putExtra("entryId", user.id)  // Przekazanie id użytkownika do nowej aktywności
                itemView.context.startActivity(intent)
            }
        }
    }
}
