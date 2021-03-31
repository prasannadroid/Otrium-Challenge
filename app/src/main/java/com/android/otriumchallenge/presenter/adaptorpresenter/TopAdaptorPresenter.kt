package com.android.otriumchallenge.presenter.adaptorpresenter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.otriumchallenge.R
import com.android.otriumchallenge.adapter.viewholder.RepoViewHolder
import com.android.otriumchallenge.model.RepositoryNode
import com.android.otriumchallenge.util.AppUtil
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

/**
 * Top adaptor presenter to manage business logic from Top Adaptor and update UI.
 *
 * @constructor
 *
 * @param appUtil
 */
class TopAdaptorPresenter(appUtil: AppUtil) {

    private val storageManager = appUtil.storageManager
    private val appContext = appUtil.context
    
    fun onCreateViewHolder(parent: ViewGroup): RepoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.raw_top_repo, parent, false)
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
                    storageManager.getUserName() ?: appContext.getString(R.string.empty)

                // Node object will be represent Repository
                holder.repoNameText.text = repository.name ?: appContext.getString(R.string.empty)

                // Set primary language details like language name, color
                repository.primaryLanguage?.let { primaryLang ->

                    holder.repoLangTxt.text =
                        primaryLang.name ?: appContext.getString(R.string.empty)

                    primaryLang.color?.let { colorStr ->
                        // set card view language color bubble
                        holder.cardView.apply {
                            setCardBackgroundColor(Color.parseColor(colorStr))
                            // by default card view is invisible
                            visibility = View.VISIBLE
                            elevation = 0f
                        }
                    }
                    // Set the description and set empty value if description null
                    holder.repoDescription.text =
                        repository.description ?: appContext.getString(R.string.empty)
                }

                repository.starGazer?.let { starGazer ->
                    // Default count will be 1
                    holder.staredTxt.text = starGazer.totalCount.toString()

                }

                // set user image
                storageManager.getUserImageUrl()?.let { url ->
                    Picasso.get().load(url).transform(CropCircleTransformation())
                        .into(holder.avatarImage)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}