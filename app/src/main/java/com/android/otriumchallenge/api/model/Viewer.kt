package com.android.otriumchallenge.api.model

import com.android.otriumchallenge.model.*
import com.google.gson.annotations.SerializedName
import javax.inject.Inject

class Viewer @Inject constructor() {

    var name: String? = null

    var login: String? = null

    var email: String? = null

    var avatarUrl: String? = null

    @SerializedName("followers")
    var follower: Follower? = null

    var following: Following? = null

    @SerializedName("starredRepositories")
    var starredRepository: StarredRepository? = null

    @SerializedName("topRepositories")
    var topRepository: TopRepository? = null

    @SerializedName("pinnedItems")
    var pinnedRepository: PinnedRepository? = null

}