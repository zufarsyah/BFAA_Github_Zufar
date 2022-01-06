package com.zufarsyah.github.model.api

import com.zufarsyah.github.model.DetailResponse
import com.zufarsyah.github.model.ItemsItem
import com.zufarsyah.github.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>
}

