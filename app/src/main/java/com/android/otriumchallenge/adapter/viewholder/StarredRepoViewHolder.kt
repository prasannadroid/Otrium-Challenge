package com.android.otriumchallenge.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.android.otriumchallenge.R

/**
 * Starred repo view holder for Starred repo recycle view items.
 *
 * @constructor
 *
 * @param view
 */
class StarredRepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.starredRecycleView)
    lateinit var recyclerView: RecyclerView

    init {
        ButterKnife.bind(this, view)
    }
}