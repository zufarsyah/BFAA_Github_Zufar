package com.zufarsyah.github.model

import com.google.gson.annotations.SerializedName

class UserResponse(
    @field:SerializedName("items")
    val items: List<ItemsItem>
)

data class ItemsItem(
    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("login")
    val login: String,
)