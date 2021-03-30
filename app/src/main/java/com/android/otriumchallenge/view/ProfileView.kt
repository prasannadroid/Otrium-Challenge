package com.android.otriumchallenge.view

import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.model.Repository

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