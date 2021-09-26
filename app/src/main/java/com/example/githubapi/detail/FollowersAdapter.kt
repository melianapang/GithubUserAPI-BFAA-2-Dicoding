package com.example.githubapi.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapi.data.User
import com.example.githubapi.databinding.DetailFollowersBinding

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {
    private val listFollowers = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        listFollowers.clear()
        listFollowers.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowersViewHolder {
        val binding =
            DetailFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(listFollowers[position])
    }

    override fun getItemCount(): Int = listFollowers.size

    inner class FollowersViewHolder(private val binding: DetailFollowersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                Glide.with(root.context)
                    .load(user.photo)
                    .into(imgProfilePicFollowers)
                tvNameFollowers.text = user.username
            }
        }
    }

}