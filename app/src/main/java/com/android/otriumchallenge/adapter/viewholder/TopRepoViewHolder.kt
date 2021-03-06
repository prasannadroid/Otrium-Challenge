package com.android.otriumchallenge.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.android.otriumchallenge.R

/**
 * Top repo view holder for Top recycle view items.
 *
 * @constructor
 *
 * @param view
 */
@SuppressLint("NonConstantResourceId")
class TopRepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.topViewRecycleView)
    lateinit var recyclerView: RecyclerView

    init {
        ButterKnife.bind(this, view)
    }
}