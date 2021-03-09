package com.android.otriumchallenge.presenter

import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.model.*
import com.android.otriumchallenge.util.Constant
import com.android.otriumchallenge.view.ProfileView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import org.json.JSONObject
import java.util.Objects.isNull
import java.util.concurrent.TimeUnit

class ProfilePresenter(private val profileView: ProfileView) : BasePresenter(profileView) {

    fun fetchUserProfile(query: String) {

        // show progress
        profileView.showProgress()

        // Json object will pass as a body from retrofit
        val paramObject = JSONObject()

        // set the GraphQL query string to the json object
        paramObject.put(Constant.QUERY_KEY, query)

        // send retrofit call
        getApi().fetchUserFromGit(paramObject.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = { response ->

                // hide progress
                profileView.hideProgress()
                if (response?.data != null) {
                    profileView.onProfileResponse(response?.data?.viewer)
                } else {
                    profileView.invalidQuery()
                }

            }, onError = {
                handleError(it)
            })
    }

    /**
     * this method will setup all 3 repositories as one
     * repository array list
     */
    fun setUpRepositoryList(viewer: Viewer?) {

        if (viewer == null) {
            return
        }

        var repositoryList = ArrayList<Repository>()

        // adding heading label to show a label
        repositoryList.add(HeaderLabel())
        // Pinned repose iterate and set node
        viewer?.starredRepository?.nodes?.forEach { pinnedRepo ->

            //TODO here Starred repositories taking as Pinned Repositories

            // Pinned Repositories are not in the GitHub account and this is for a Demo purpose

            val pinnedRepository = PinnedRepository().apply {
                this.node = pinnedRepo
            }
            repositoryList.add(pinnedRepository)
        }

        val topRepository = TopRepository().apply {
            this.nodes = viewer?.topRepository?.nodes
            viewer?.pinnedRepository?.nodes = viewer?.topRepository?.nodes
        }

        val starredRepository = StarredRepository().apply {
            this.nodes = viewer?.starredRepository?.nodes
        }

        // adding repository type object for recycle view
        repositoryList.add(topRepository)

        // adding repository type object for recycle view
        repositoryList.add(starredRepository)

        profileView.setAdaptor(repositoryList)
    }

    // save user name to shared pref
    fun saveUserName(userName: String) = getStoreManager().saveUerName(userName)

    // save user image url to shared pref
    fun saveAvatarUrl(avatarUrl: String) = getStoreManager().saveUserImageUrl(avatarUrl)

    // save cashing data for a one day
    fun saveCashData(viewer: Viewer?) {

        // check last cashed time expired
        if (getLastSavedDate() < System.currentTimeMillis()) {
            // addition extra 25 hours(one day) to current time mills and save as nextCashSavingTime
            val nextCashSavingTime =
                System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)

            // save next cash saving milliseconds
            getStoreManager().saveCashedTimeMills(nextCashSavingTime)

            //save cash as json
            getStoreManager().saveCashedData(viewer)

            profileView.onSaveCashedData()


        }
    }

    // ger saved last time mills
    private fun getLastSavedDate() = getStoreManager().getCashedTimeMills()
}