package com.android.otriumchallenge.presenter.adaptorpresenter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.otriumchallenge.R
import com.android.otriumchallenge.adapter.StarredRepoAdaptor
import com.android.otriumchallenge.adapter.TopRepoAdaptor
import com.android.otriumchallenge.adapter.viewholder.HeaderLabelViewHolder
import com.android.otriumchallenge.adapter.viewholder.PinnedRepoViewHolder
import com.android.otriumchallenge.adapter.viewholder.StarredRepoViewHolder
import com.android.otriumchallenge.adapter.viewholder.TopRepoViewHolder
import com.android.otriumchallenge.model.*
import com.android.otriumchallenge.presenter.BasePresenter
import com.android.otriumchallenge.view.ProfileView
import com.android.otriumchallenge.view.adaptorview.PinnedAdaptorView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.lang.Exception

class PinnedAdaptorPresenter(
    private val pinnedAdaptorView: PinnedAdaptorView,
    profileView: ProfileView
) : BasePresenter(profileView) {

    fun onCreateViewHolder(
        parent: ViewGroup,
        repositoryList: ArrayList<Repository>, position: Int
    ): RecyclerView.ViewHolder {


        // Return different type of ViewHolders according to the repository type
        return when (repositoryList[position]) {
            is HeaderLabel -> {
                // this is the pinned label shows on top
                //  no adaptor need
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.raw_header_label, parent, false)
                HeaderLabelViewHolder(view)
            }
            is PinnedRepository -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.raw_pinned_repo, parent, false)
                PinnedRepoViewHolder(view)
            }
            is TopRepository -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.raw_top_recycle_view, parent, false)
                TopRepoViewHolder(view)
            }
            is StarredRepository -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.raw_starred_recycle_view, parent, false)
                StarredRepoViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.raw_pinned_repo, parent, false)
                PinnedRepoViewHolder(view)

            }
        }

    }

    fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        repositoryList: ArrayList<Repository>
    ) {
        try {
            when (val repository = repositoryList[position]) {
                is PinnedRepository -> {
                    val holderCast = holder as PinnedRepoViewHolder

                    // Node object will be represent Repository
                    repository.node?.let {

                        // set user name
                        holder.userNameTxt.text =
                            getStoreManager().getUserName() ?: getApp().getString(R.string.empty)

                        holderCast.repoNameText.text = it.name ?: getApp().getString(R.string.empty)

                        // Set primary language details like language name, color
                        it.primaryLanguage?.let { primaryLang ->

                            holderCast.repoLangTxt.text =
                                primaryLang.name ?: getApp().getString(R.string.empty)

                            primaryLang.color?.let { colorStr ->
                                // set card view language color bubble
                                holderCast.cardView.apply {
                                    setCardBackgroundColor(Color.parseColor(colorStr))
                                    visibility = View.VISIBLE
                                    elevation = 0f
                                }
                            }

                            // Set the description and set empty value if description null
                            holderCast.repoDescription.text =
                                it.description ?: getApp().getString(R.string.empty)
                        }

                        it.starGazer?.let { starGazer ->
                            // Default count will be 1
                            holderCast.staredTxt.text = starGazer.totalCount.toString()

                        }

                        // set user image
                        getStoreManager().getUserImageUrl()?.let { url ->
                            Picasso.get().load(url).transform(CropCircleTransformation())
                                .into(holder.avatarImage)
                        }

                    }

                }
                is TopRepository -> {
                    //send Top Repository Adapter and Recycle View for setup
                    repository.nodes?.let {
                        val holderCast = holder as TopRepoViewHolder
                        pinnedAdaptorView.setupTopRepoAdaptor(
                            TopRepoAdaptor(repository, it.size),
                            holderCast.recyclerView
                        )
                    }


                }
                is StarredRepository -> {
                    //send Top Starred Adapter and Recycle View for setup
                    repository.nodes?.let {
                        val holderCast = holder as StarredRepoViewHolder
                        pinnedAdaptorView.setupStarredRepoAdaptor(
                            StarredRepoAdaptor(repository, it.size),
                            holderCast.recyclerView
                        )
                    }
                }

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}