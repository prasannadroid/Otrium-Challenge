package com.android.otriumchallenge.storage

import android.content.SharedPreferences
import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.util.Constant
import com.google.gson.Gson

/**
 * Storage manager will handle all the persistence data with android shared preferences.
 *
 *
 * @property sharedPreferences initialized share preferences from Dagger DataStore module.
 * @constructor Create empty Storage manager
 */
open class StorageManager(private val sharedPreferences: SharedPreferences) {

    // persist user name
    fun saveUerName(userName: String?) =
        sharedPreferences.edit().putString(Constant.USER_NAME, userName).commit()

    // persist avatar/image url
    fun saveUserImageUrl(userImageUrl: String?) =
        sharedPreferences.edit().putString(Constant.USER_IMAGE_URL, userImageUrl).commit()

    // this method will save the last saved date as milliseconds
    fun saveCashedTimeMills(timeMills: Long) =
        sharedPreferences.edit().putLong(Constant.CASHED_TIME, timeMills).commit()

    // persist github response as Json string
    fun saveCashedData(viewer: Viewer?) =
        sharedPreferences.edit()
            .putString(Constant.CASHED_DATA, Gson().toJson(viewer).toString()).commit()

    // get saved user name
    fun getUserName() = sharedPreferences.getString(Constant.USER_NAME, null)

    // get saved avatar/image url
    fun getUserImageUrl() = sharedPreferences.getString(Constant.USER_IMAGE_URL, null)

    // get saved cashed time as millisecond
    fun getCashedTimeMills() = sharedPreferences.getLong(Constant.CASHED_TIME, 0)

}