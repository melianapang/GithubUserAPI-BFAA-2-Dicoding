package com.example.githubapi.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity, username: String = "") :
    FragmentStateAdapter(activity) {
    val uname: String = username

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return FollowersFragment.newInstance(position, uname)
    }
}