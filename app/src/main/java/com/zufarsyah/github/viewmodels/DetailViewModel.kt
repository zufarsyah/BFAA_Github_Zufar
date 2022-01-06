package com.zufarsyah.github.viewmodels

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zufarsyah.github.model.DetailResponse
import com.zufarsyah.github.model.api.ApiConfig
import com.zufarsyah.github.model.database.FavoriteUser
import com.zufarsyah.github.model.database.FavoriteUserDao
import com.zufarsyah.github.model.database.FavoriteUserRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    val user = MutableLiveData<DetailResponse>()
    private val mFavoriteUserDao: FavoriteUserDao

    init {
        val database = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = database.favoriteUserDao()
    }

    fun addFavorite(username: String, id: Int, avatar_url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                id,
                avatar_url,
            )
            mFavoriteUserDao.addFavorite((user))
        }
    }

    suspend fun checkUser(id: Int) = mFavoriteUserDao.checkUser(id)

    fun deleteFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            mFavoriteUserDao.deleteFavorite(id)
        }
    }

    fun setUserDetail(username: String) {
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    user.postValue(response.body())
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserDetail(): LiveData<DetailResponse> {
        return user
    }
}