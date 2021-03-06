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
import com.android.otriumchallenge.util.AppUtil
import com.android.otriumchallenge.view.adaptorview.PinnedAdaptorView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

/**
 * Pinned adaptor presenter for manage Pinned adaptor recycle view business
 * logic and update adaptor UI.
 *
 * @property appUtil
 * @property pinnedAdaptorView
 * @constructor Create empty Pinned adaptor presenter
 */
class PinnedAdaptorPresenter(
    private val appUtil: AppUtil,
    private val pinnedAdaptorView: PinnedAdaptorView,
) {

    private val storageManager = appUtil.storageManager
    private val appContext = appUtil.context

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

            // making pinned repository view and view holder
            is PinnedRepository -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.raw_pinned_repo, parent, false)
                PinnedRepoViewHolder(view)
            }

            // making top repository view and view holder
            is TopRepository -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.raw_top_recycle_view, parent, false)
                TopRepoViewHolder(view)
            }

            // making stared repository view and view holder
            is StarredRepository -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.raw_starred_recycle_view, parent, false)
                StarredRepoViewHolder(view)
            }

            // default will be a pinned view
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
                        holderCast.userNameTxt.text =
                            storageManager.getUserName() ?: appContext.getString(R.string.empty)

                        holderCast.repoNameText.text =
                            it.name ?: appContext.getString(R.string.empty)

                        // Set primary language details like language name, color
                        it.primaryLanguage?.let { primaryLang ->

                            holderCast.repoLangTxt.text =
                                primaryLang.name ?: appContext.getString(R.string.empty)

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
                                it.description ?: appContext.getString(R.string.empty)
                        }

                        it.starGazer?.let { starGazer ->
                            // Default count will be 1
                            holderCast.staredTxt.text = starGazer.totalCount.toString()

                        }

                        // set user image
                        storageManager.getUserImageUrl()?.let { url ->
                            Picasso.get().load(url).transform(CropCircleTransformation())
                                .into(holderCast.avatarImage)
                        }

                    }

                }
                is TopRepository -> {
                    //send Top Repository Adapter and Recycle View for setup
                    repository.nodes?.let {
                        val holderCast = holder as TopRepoViewHolder
                        pinnedAdaptorView.setupTopRepoAdaptor(
                            TopRepoAdaptor(appUtil, repository, it.size),
                            holderCast.recyclerView
                        )
                    }


                }
                is StarredRepository -> {
                    //send Top Starred Adapter and Recycle View for setup
                    repository.nodes?.let {
                        val holderCast = holder as StarredRepoViewHolder
                        pinnedAdaptorView.setupStarredRepoAdaptor(
                            StarredRepoAdaptor(appUtil, repository, it.size),
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