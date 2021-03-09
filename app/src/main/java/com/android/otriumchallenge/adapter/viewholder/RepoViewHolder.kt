package com.android.otriumchallenge.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.android.otriumchallenge.R

@SuppressLint("NonConstantResourceId")
class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @BindView(R.id.avatarImageView)
    lateinit var avatarImage: ImageView

    @BindView(R.id.userNameTextView)
    lateinit var userNameTxt: TextView

    @BindView(R.id.reponNameTextView)
    lateinit var repoNameText: TextView

    @BindView(R.id.repoDesctiptionTextView)
    lateinit var repoDescription: TextView

    @BindView(R.id.staredTextView)
    lateinit var staredTxt: TextView

    @BindView(R.id.repoLangText)
    lateinit var repoLangTxt: TextView

    @BindView(R.id.langCardView)
    lateinit var cardView: CardView

    init {
        ButterKnife.bind(this, view)
    }
}