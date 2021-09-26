package com.example.githubapi.viewmodel

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapi.R
import com.example.githubapi.data.User
import com.example.githubapi.detail.DetailActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class DetailViewModel : ViewModel() {
    private var listFollowers = MutableLiveData<ArrayList<User>>()
    private var listFollowing = MutableLiveData<ArrayList<User>>()
    private var DetailProfile = MutableLiveData<User>()
    private var TAG = DetailActivity::class.java.simpleName

    fun getDetailProfile(): LiveData<User> = DetailProfile

    fun getListFollow(position: Int): LiveData<ArrayList<User>> {
        when (position) {
            0 -> return listFollowers
            else -> return listFollowing
        }
    }

    fun loadDetailProfile(username: String): String {
        var emsg = ""
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${username}"
        client.addHeader("Authorization", "token 5c01c7724875622a6d67ed0df652ae74cec81caf")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(DetailActivity::class.java.simpleName, result)
                try {
                    val aUser = JSONObject(result)
                    val user = User()
                    user.username = aUser.getString("login")
                    user.photo = aUser.getString("avatar_url")
                    user.name = aUser.getString("name")
                    user.city = aUser.getString("location")
                    user.company = aUser.getString("company")
                    user.followers = aUser.getInt("followers")
                    user.following = aUser.getInt("following")
                    DetailProfile.postValue(user)
                } catch (e: Exception) {
                    e.printStackTrace()
                    emsg = e.message?: stringResource(R.string.error_loading)
                    Log.e(TAG, e.message?: R.string.error_loading.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                emsg = error?.message?: stringResource(R.string.error_connect)
                Log.e(TAG, error?.message?: stringResource(R.string.error_connect))
            }
        })
        return emsg
    }

    fun loadFollow(username: String):String {
        var emsg = ""
        var listUsers = ArrayList<User>()
        val client = AsyncHttpClient()
        var url: String = ""
        for (position in 0..1) {
            when (position) {
                0 -> url = "https://api.github.com/users/${username}/followers"
                1 -> url = "https://api.github.com/users/${username}/following"
            }
            client.addHeader("Authorization", "token 5c01c7724875622a6d67ed0df652ae74cec81caf")
            client.addHeader("User-Agent", "request")
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
                ) {
                    val result = String(responseBody)
                    try {
                        val responseObj = JSONArray(result)

                        for (i in 0..responseObj.length() - 1) {
                            val aUser = responseObj.getJSONObject(i)
                            val user = User()
                            user.username = aUser.getString("login")
                            user.photo = aUser.getString("avatar_url")
                            listUsers.add(user)
                        }

                        when (position) {
                            0 -> listFollowers.postValue(listUsers)
                            1 -> listFollowing.postValue(listUsers)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        emsg = e.message?: stringResource(R.string.error_loading)
                        Log.e(TAG, e.message?: stringResource(R.string.error_loading))
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    emsg = error?.message?: stringResource(R.string.error_connect)
                    Log.e(TAG, error?.message?: stringResource(R.string.error_connect))
                }
            })
        }
        return emsg
    }
}