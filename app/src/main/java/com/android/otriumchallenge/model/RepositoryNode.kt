package com.android.otriumchallenge.model

import com.google.gson.annotations.SerializedName
import javax.inject.Inject

class RepositoryNode {

    var name: String? = null

    var description: String? = null

    var primaryLanguage: PrimaryLanguage? = null

    @SerializedName("stargazers")
    var starGazer: StarGazer? = null

}