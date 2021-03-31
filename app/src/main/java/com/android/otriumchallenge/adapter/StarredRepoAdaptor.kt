package com.android.otriumchallenge.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.otriumchallenge.adapter.viewholder.RepoViewHolder
import com.android.otriumchallenge.model.StarredRepository
import com.android.otriumchallenge.presenter.adaptorpresenter.StarredAdaptorPresenter
import com.android.otriumchallenge.util.AppUtil
import com.android.otriumchallenge.view.adaptorview.StarredAdaptorView

/**
 * Starred repo adaptor will handle the Starred repository and provide adaptor to RecycleView.
 *
 * @property topRepository
 * @property adaptorSize
 * @constructor
 *
 * @param appUtil
 */
class StarredRepoAdaptor(
    appUtil: AppUtil,
    private val topRepository: StarredRepository,
    private val adaptorSize: Int
) :
    RecyclerView.Adapter<RepoViewHolder>(), StarredAdaptorView {

    private var staredPresenter = StarredAdaptorPresenter(appUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return staredPresenter.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        staredPresenter.onBindViewHolder(holder, position, topRepository.nodes)
    }

    override fun getItemCount() = adaptorSize

    override fun showProgress() {
    }

    override fun hideProgress() {
    }
}