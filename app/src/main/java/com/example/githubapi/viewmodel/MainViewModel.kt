package com.example.githubapi.viewmodel

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapi.R
import com.example.githubapi.data.User
import com.example.githubapi.main.MainActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {
    private val listFilterUsers = MutableLiveData<ArrayList<User>>()
    private val listUsers = MutableLiveData<ArrayList<User>>()
    private var TAG = MainActivity::class.java.simpleName

    fun getListUser(): LiveData<ArrayList<User>> = listUsers
    fun getListFilterUser(): LiveData<ArrayList<User>> = listFilterUsers

    fun loadUsers():String {
        var emsg = ""
        var listItem = ArrayList<User>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
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
                        listItem.add(user)
                    }
                    listUsers.postValue(listItem)
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
        return emsg
    }

    fun setListFilterUser(text: String):String {
        var listItem = ArrayList<User>()
        var emsg = ""
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=${text}"
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
                    val responseObj = JSONObject(result)
                    val dataArray = responseObj.getJSONArray("items")

                    for (i in 0..dataArray.length() - 1) {
                        val aUser = dataArray.getJSONObject(i)
                        val user = User()
                        user.username = aUser.getString("login")
                        user.photo = aUser.getString("avatar_url")

                        listItem.add(user)
                    }
                    listFilterUsers.postValue(listItem)
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
        return emsg
    }
}