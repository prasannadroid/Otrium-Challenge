package com.android.otriumchallenge.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.android.otriumchallenge.R

class StarredRepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.starredRecycleView)
    lateinit var recyclerView: RecyclerView

    init {
        ButterKnife.bind(this, view)
    }
}