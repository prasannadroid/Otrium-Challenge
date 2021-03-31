package com.android.otriumchallenge.presenter

import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.api.response.UserResponse
import com.android.otriumchallenge.model.*
import com.android.otriumchallenge.util.AppUtil
import com.android.otriumchallenge.util.Constant
import com.android.otriumchallenge.view.ProfileView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

open class ProfilePresenter(private val appUtil: AppUtil, val profileView: ProfileView) {

    private val storageManager = appUtil.storageManager
    private val endpointService = appUtil.endPoint

    open fun fetchUserProfile() {

        // show progress
        profileView.showProgress()

       // Json object will pass as QUERY from retrofit
        endpointService.fetchUserFromGit(Constant.QUERY).enqueue(object :
            Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                profileView.hideProgress()
                when (response.code()) {
                    Constant.SUCCESS -> { // status code 200
                        response.body()?.let {
                            profileView.onProfileResponse(it.data?.viewer)
                        }

                        if (response.body() == null || response.body()?.data == null) {
                            profileView.invalidQuery()
                        }
                    }
                    Constant.BAD_REQUEST -> profileView.invalidQuery() // status code 400
                    Constant.AUTH_ERROR -> profileView.authenticationError() // status code 401
                }
                call.cancel()
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                profileView.hideProgress()
                profileView.handleApiError(t)
                call.cancel()
            }

        })


    }

    /**
     * this method will setup all 3 repositories as one
     * repository array list
     */
    fun setUpRepositoryList(viewer: Viewer?) {

        if (viewer == null) {
            profileView.unknownError()
            return
        }

        val repositoryList = kotlin.collections.ArrayList<Repository>()

        // adding heading label to show a label
        repositoryList.add(HeaderLabel())
        // Pinned repose iterate and set node
        viewer.starredRepository?.nodes?.forEach { pinnedRepo ->

            //TODO here Starred repositories taking as Pinned Repositories

            // Pinned Repositories are not in the GitHub account and this is for a Demo purpose

            val pinnedRepository = PinnedRepository().apply {
                this.node = pinnedRepo
            }
            repositoryList.add(pinnedRepository)
        }

        val topRepository = TopRepository().apply {
            this.nodes = viewer.topRepository?.nodes
            viewer.pinnedRepository?.nodes = viewer.topRepository?.nodes
        }

        val starredRepository = StarredRepository().apply {
            this.nodes = viewer.starredRepository?.nodes
        }

        // adding repository type object for recycle view
        repositoryList.add(topRepository)

        // adding repository type object for recycle view
        repositoryList.add(starredRepository)

        // pass adaptor for UI
        profileView.setAdaptor(repositoryList)

        // show welcome message in UI
        profileView.welcomeMessage()
    }

    // save user name to shared pref
    fun saveUserName(userName: String?) =
        (userName != null && storageManager.getUserName()
            .isNullOrBlank()).takeIf { storageManager.saveUerName(userName) }


    // save user image url to shared pref
    fun saveAvatarUrl(avatarUrl: String?) =
        (avatarUrl != null && storageManager.getUserImageUrl()
            .isNullOrBlank()).takeIf { storageManager.saveUserImageUrl(avatarUrl) }


    // save cashing data for a one day
    open fun saveCashData(viewer: Viewer?) {

        viewer?.let {

            // check last cashed time expired
            // TODO System.currentTimeMillis() will change according to the time zone so best practice is use Server Time
            if (getLastSavedDate() < System.currentTimeMillis()) {

                // add extra 24 hours(one day) to current time mills and save as nextCashSavingTime
                val nextCashSavingTime =
                    System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)

                // save next cash saving milliseconds
                appUtil.storageManager.saveCashedTimeMills(nextCashSavingTime)

                //save cash as json
                appUtil.storageManager.saveCashedData(viewer)

                // update UI
                profileView.onSaveCashedData()

            }
        }
    }

    // get saved date as milliseconds
    open fun getLastSavedDate() = appUtil.storageManager.getCashedTimeMills()

}