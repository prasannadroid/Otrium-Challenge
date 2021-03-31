package com.android.otriumchallenge.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.otriumchallenge.model.Repository
import com.android.otriumchallenge.presenter.adaptorpresenter.PinnedAdaptorPresenter
import com.android.otriumchallenge.util.AppUtil
import com.android.otriumchallenge.view.adaptorview.PinnedAdaptorView


class PinnedRepoAdaptor(
    appUtil: AppUtil,
    private val repositoryList: ArrayList<Repository>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), PinnedAdaptorView {

    // position will determine the current position of a child view
    private var position = -1

    var pinnedRepoPresenter = PinnedAdaptorPresenter(appUtil, this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        position++
        return pinnedRepoPresenter.onCreateViewHolder(parent, repositoryList, position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        pinnedRepoPresenter.onBindViewHolder(holder, position, repositoryList)

    override fun getItemCount() = repositoryList.size

    // Setup Top Repo Horizontal list view
    override fun setupTopRepoAdaptor(topRepoAdaptor: TopRepoAdaptor, recyclerView: RecyclerView) {
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = topRepoAdaptor
    }

    // Setup Starred Repo Horizontal list view
    override fun setupStarredRepoAdaptor(
        starredRepoAdaptor: StarredRepoAdaptor,
        recyclerView: RecyclerView
    ) {
        recyclerView.layoutManager =
            LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = starredRepoAdaptor
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }
}