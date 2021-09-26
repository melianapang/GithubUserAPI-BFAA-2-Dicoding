package com.example.githubapi.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapi.data.User
import com.example.githubapi.databinding.DetailRvBinding


class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    private val listUsers = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        listUsers.clear()
        listUsers.addAll(items)
        notifyDataSetChanged()
    }

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = DetailRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int = listUsers.size

    inner class UsersViewHolder(private val binding: DetailRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                Glide.with(root.context)
                    .load(user.photo)
                    .into(imgPhoto)
                tvUsername.text = user.username
            }
            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

}


