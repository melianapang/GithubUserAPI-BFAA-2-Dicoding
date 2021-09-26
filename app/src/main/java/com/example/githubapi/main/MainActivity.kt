package com.example.githubapi.main

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.R
import com.example.githubapi.data.User
import com.example.githubapi.databinding.ActivityMainBinding
import com.example.githubapi.detail.DetailActivity
import com.example.githubapi.viewmodel.MainViewModel
import com.example.githubapi.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val userAdapter = UsersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        val factory = ViewModelFactory()
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        initialiseAdapter()
        searchViewManager()

        userAdapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val dataMoved = Intent(this@MainActivity, DetailActivity::class.java)
                dataMoved.putExtra(DetailActivity.EXTRA, data)
                startActivity(dataMoved)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_localization) {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        return true
    }

    private fun initialiseAdapter() {
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)

        userAdapter.notifyDataSetChanged()
        binding.rvUsers.adapter = userAdapter
        observeData()
    }

    private fun observeData() {
        val emsg = mainViewModel.loadUsers()
        if(emsg.isBlank()) {
            mainViewModel.getListUser().observe(this, { user ->
                userAdapter.setData(user)
                userAdapter.notifyDataSetChanged()
                showLoading(false)
            })
        }
        else{
            showToast(emsg)
        }
    }

    private fun searchViewManager() {
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.searchView.queryHint = resources.getString(R.string.search_hint)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if(query.isEmpty()) observeData() else observeFilterData(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isEmpty()) observeData() else observeFilterData(newText)
                return false
            }
        })
    }

    private fun observeFilterData(text: String) {
        showLoading(true)
        mainViewModel.setListFilterUser(text)
        mainViewModel.getListFilterUser().observe(this@MainActivity, { user ->
            userAdapter.setData(user)
            if (user.isEmpty()){
                binding.tvSearchResult.visibility = View.VISIBLE
            } else {
                binding.tvSearchResult.visibility = View.INVISIBLE
            }
        })
        showLoading(false)
        userAdapter.notifyDataSetChanged()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }
}