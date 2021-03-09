package com.android.otriumchallenge.presenter.adaptorpresenter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.otriumchallenge.R
import com.android.otriumchallenge.adapter.viewholder.RepoViewHolder
import com.android.otriumchallenge.model.RepositoryNode
import com.android.otriumchallenge.presenter.BasePresenter
import com.android.otriumchallenge.view.adaptorview.StarredAdaptorView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.lang.Exception

class StarredAdaptorPresenter(starredAdaptorView: StarredAdaptorView) :
    BasePresenter(starredAdaptorView) {

    fun onCreateViewHolder(parent: ViewGroup): RepoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.raw_sttared_repo, parent, false)
        return RepoViewHolder(view)

    }

    fun onBindViewHolder(
        holder: RepoViewHolder,
        position: Int,
        repositoryList: ArrayList<RepositoryNode>?
    ) {
        try {

            repositoryList?.let {
                val repository = it[position]

                // set user name
                holder.userNameTxt.text =
                    getStoreManager().getUserName() ?: getApp().getString(R.string.empty)

                // Node object will be represent Repository
                holder.repoNameText.text = repository.name ?: getApp().getString(R.string.empty)

                // Set primary language details like language name, color
                repository.primaryLanguage?.let { primaryLang ->

                    holder.repoLangTxt.text =
                        primaryLang.name ?: getApp().getString(R.string.empty)

                    primaryLang.color?.let { colorStr ->
                        holder.cardView.setCardBackgroundColor(Color.parseColor(colorStr))
                        holder.cardView.visibility = View.VISIBLE
                    }

                    // Set the description and set empty value if description null
                    holder.repoDescription.text =
                        repository.description ?: getApp().getString(R.string.empty)
                }

                repository.starGazer?.let { starGazer ->
                    // Default count will be 1
                    holder.staredTxt.text = starGazer.totalCount.toString()

                }

                // set user image
                getStoreManager().getUserImageUrl()?.let { url ->
                    Picasso.get().load(url).transform(CropCircleTransformation())
                        .into(holder.avatarImage)
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}