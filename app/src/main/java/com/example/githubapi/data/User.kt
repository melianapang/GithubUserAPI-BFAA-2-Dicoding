package com.example.githubapi.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    var username: String = "",
    var name: String = "",
    var followers: Int = 0,
    var following: Int = 0,
    var photo: String = "",
    var city: String = "",
    var company: String = ""
) : Parcelable