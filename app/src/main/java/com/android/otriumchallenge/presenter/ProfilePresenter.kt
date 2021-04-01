package com.android.otriumchallenge.presenter

import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.api.response.UserResponse
import com.android.otriumchallenge.model.*
import com.android.otriumchallenge.util.AppUtil
import com.android.otriumchallenge.util.Constant
import com.android.otriumchallenge.view.ProfileView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

/**
 * Profile presenter will handle all the business logic related the the profile activity
 * and update UI.
 *
 * @property appUtil will hold all dagger injected objects.
 * @property profileView will be the interface for UI.
 * @constructor Create empty Profile presenter.
 */
open class ProfilePresenter(private val appUtil: AppUtil, val profileView: ProfileView) {

    private val storageManager = appUtil.storageManager
    private val endpointService = appUtil.endPoint

    /**
     * Fetch user profile method will be send RX call to the graphQL API via retrofit
     *  get user profile and repository related data.
     *
     */
    fun fetchUserProfile() {

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
     * this method will setup all 3 sub repositories as one
     * main repository array list.
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

    /**
     * Save user name method will pass user name to save shared preferences.
     *
     * @param userName user name string from profile activity.
     */
     fun saveUserName(userName: String?) =
        (userName != null && storageManager.getUserName()
            .isNullOrBlank()).takeIf {
            storageManager.saveUerName(userName) }


    /**
     * Save avatar url method will pass user image url to save shared preferences.
     *
     * @param avatarUrl user url form profile activity.
     */
     fun saveAvatarUrl(avatarUrl: String?) =
        (avatarUrl != null && storageManager.getUserImageUrl()
            .isNullOrBlank()).takeIf { storageManager.saveUserImageUrl(avatarUrl) }


    /**
     * Save cash data method will check last cashed date as milliseconds and save
     * to shared preferences.
     *
     * @param viewer grapQL user response object
     */
    suspend fun saveCashData(viewer: Viewer?) {

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

    /**
     * Get last saved date method will get last saving date time as milliseconds.
     *
     */
    fun getLastSavedDate() = appUtil.storageManager.getCashedTimeMills()

}