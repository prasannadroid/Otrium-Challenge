package com.android.otriumchallenge.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.otriumchallenge.adapter.viewholder.RepoViewHolder
import com.android.otriumchallenge.model.TopRepository
import com.android.otriumchallenge.presenter.adaptorpresenter.TopAdaptorPresenter
import com.android.otriumchallenge.util.AppUtil
import com.android.otriumchallenge.view.adaptorview.TopAdapterView

class TopRepoAdaptor(
    appUtil: AppUtil,
    private val topRepository: TopRepository, private val adaptorSize: Int
) :
    RecyclerView.Adapter<RepoViewHolder>(), TopAdapterView {

    var topAdapterPresenter = TopAdaptorPresenter(appUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return topAdapterPresenter.onCreateViewHolder(parent)

    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        topAdapterPresenter.onBindViewHolder(holder, position, topRepository.nodes)
    }

    override fun getItemCount() = adaptorSize

    override fun showProgress() {
    }

    override fun hideProgress() {
    }


}