package com.example.githubapi.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubapi.R
import com.example.githubapi.data.User
import com.example.githubapi.viewmodel.DetailViewModel
import com.example.githubapi.databinding.ActivityDetailBinding
import com.example.githubapi.viewmodel.DetailViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detViewModel: DetailViewModel

    companion object {
        const val EXTRA = "extra"
        private val TAB_TITLES = intArrayOf(
                com.example.githubapi.R.string.followers_tab,
                com.example.githubapi.R.string.following_tab
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.det_page)
        showLoading(true)

        val user = intent.getParcelableExtra<User>(EXTRA)

        val factory = DetailViewModelFactory()
        detViewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)
        observeData(user?.username.toString())
        managePagerAdapter(user?.username.toString())
    }

    private fun managePagerAdapter(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun observeData(username: String) {
        val emsg = detViewModel.loadDetailProfile(username)
        if(emsg.isBlank()){
            detViewModel.getDetailProfile().observe(this, { userDetail ->
                with(binding) {
                    tvUsernameDetail.text = userDetail.username
                    tvNameDetail.text = if (userDetail.name == "null") resources.getString(R.string.name) else userDetail.name
                    tvFollowers.text = resources.getQuantityString(
                            R.plurals.numOfFollower,
                            userDetail.followers,
                            userDetail.followers
                    )
                    tvFollowing.text =
                            resources.getString(R.string.numOfFollowing, userDetail.following)
                    tvCity.text =
                            if (userDetail.city == "null") resources.getString(R.string.city) else userDetail.city
                    tvCompany.text =
                            if (userDetail.company == "null") resources.getString(com.example.githubapi.R.string.company) else userDetail.company
                    Glide.with(root.context)
                            .load(userDetail.photo)
                            .into(profilePic)
                    showLoading(false)
                }
            })
        }
        else{
            showToast(emsg)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            binding.progressBarDetail.visibility = View.INVISIBLE
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this@DetailActivity, msg, Toast.LENGTH_SHORT).show()
    }
}