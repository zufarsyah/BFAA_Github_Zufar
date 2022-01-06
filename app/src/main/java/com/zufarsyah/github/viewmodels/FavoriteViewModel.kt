package com.zufarsyah.github.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zufarsyah.github.model.database.FavoriteUser
import com.zufarsyah.github.model.database.FavoriteUserDao
import com.zufarsyah.github.model.database.FavoriteUserRoomDatabase

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserDao: FavoriteUserDao

    init {
        val database = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = database.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getFavoriteUser()
}
