package com.android.otriumchallenge.view.adaptorview

import androidx.recyclerview.widget.RecyclerView
import com.android.otriumchallenge.adapter.StarredRepoAdaptor
import com.android.otriumchallenge.adapter.TopRepoAdaptor
import com.android.otriumchallenge.view.BaseView

interface PinnedAdaptorView : AdaptorBaseView {

    fun setupTopRepoAdaptor(topRepoAdaptor: TopRepoAdaptor, recyclerView: RecyclerView)

    fun setupStarredRepoAdaptor(starredRepoAdaptor: StarredRepoAdaptor, recyclerView: RecyclerView)

}