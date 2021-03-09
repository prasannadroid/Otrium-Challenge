package com.android.otriumchallenge.storage

import android.content.Context
import android.content.SharedPreferences
import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.util.Constant
import com.google.gson.Gson

class StorageManager(context: Context) {

   private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        Constant.SHARED_PREF, Context.MODE_PRIVATE
    )

    fun saveUerName(userName: String) =
        sharedPreferences.edit().putString(Constant.USER_NAME, userName).apply()

    fun saveUserImageUrl(userImageUrl: String) =
        sharedPreferences.edit().putString(Constant.USER_IMAGE_URL, userImageUrl).apply()

    // this method will save the last saved date as milliseconds
    fun saveCashedTimeMills(timeMills: Long) =
        sharedPreferences.edit().putLong(Constant.CASHED_TIME, timeMills).apply()

    fun saveCashedData(viewer: Viewer?) {
        viewer?.let {
            Gson().toJson(viewer).toString()
            sharedPreferences.edit()
                .putString(Constant.CASHED_DATA, Gson().toJson(viewer).toString()).apply()
        }

    }

    fun getUserName() = sharedPreferences.getString(Constant.USER_NAME, null)

    fun getUserImageUrl() = sharedPreferences.getString(Constant.USER_IMAGE_URL, null)

    fun getCashedTimeMills() = sharedPreferences.getLong(Constant.CASHED_TIME, 0)

}