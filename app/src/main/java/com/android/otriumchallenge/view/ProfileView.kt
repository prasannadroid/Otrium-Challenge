package com.android.otriumchallenge.view

import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.model.Repository

/**
 * Profile view will handle all the UI related functins and
 * update the profile activity
 *
 * @constructor Create empty Profile view
 */
interface ProfileView : BaseView {

    fun onProfileResponse(viewer: Viewer?)

    fun setAdaptor(repositoryList: ArrayList<Repository>)

    fun welcomeMessage()

    fun invalidQuery()

    fun authenticationError()

    fun onSaveCashedData()

    fun unknownError()

    fun handleApiError(it: Throwable)

}