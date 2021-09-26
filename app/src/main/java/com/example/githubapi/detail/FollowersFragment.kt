package com.example.githubapi.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.viewmodel.DetailViewModel
import com.example.githubapi.databinding.FragmentFollowersBinding
import com.example.githubapi.viewmodel.DetailViewModelFactory

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private lateinit var detViewModel: DetailViewModel
    val follAdapter = FollowersAdapter()

    companion object {
        const val USERNAME_PROFILE = "username"
        const val POSITION = "position"

        @JvmStatic
        fun newInstance(position: Int, username: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                    putString(USERNAME_PROFILE, username)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersBinding.inflate(layoutInflater)
        showLoading(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = DetailViewModelFactory()
        detViewModel = ViewModelProvider(
            this, factory
        ).get(DetailViewModel::class.java)

        val pos = arguments?.getInt(POSITION, 0) ?: 0
        val argUsername = arguments?.getString(USERNAME_PROFILE).toString()
        initialiseAdapter()
        observeData(pos, argUsername)
    }

    private fun initialiseAdapter() {
        binding.rvFollowers.setHasFixedSize(true)
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)

        follAdapter.notifyDataSetChanged()
        binding.rvFollowers.adapter = follAdapter
    }

    private fun observeData(position: Int, username: String) {
        val emsg = detViewModel.loadFollow(username)
        if(emsg.isBlank()) {
            detViewModel.getListFollow(position).observe(activity as DetailActivity, { user ->
                follAdapter.setData(user)
                follAdapter.notifyDataSetChanged()
                showLoading(false)
            })
        }
        else{
            showToast(emsg)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.INVISIBLE
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}