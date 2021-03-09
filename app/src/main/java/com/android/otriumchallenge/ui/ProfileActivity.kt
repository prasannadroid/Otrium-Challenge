package com.android.otriumchallenge.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import com.android.otriumchallenge.R
import com.android.otriumchallenge.adapter.PinnedRepoAdaptor
import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.model.Repository
import com.android.otriumchallenge.presenter.ProfilePresenter
import com.android.otriumchallenge.util.Constant
import com.android.otriumchallenge.view.ProfileView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation


@SuppressLint("NonConstantResourceId")
class ProfileActivity : BaseActivity(), ProfileView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.userNameTextView)
    lateinit var userNameTxt: TextView

    @BindView(R.id.loginNameTextView)
    lateinit var userLoginTxt: TextView

    @BindView(R.id.emailTextView)
    lateinit var userEmail: TextView

    @BindView(R.id.followerTextView)
    lateinit var followerTxt: TextView

    @BindView(R.id.followingTextView)
    lateinit var following: TextView

    @BindView(R.id.recycleView)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.avatarImageView)
    lateinit var avatarImage: ImageView

    @BindView(R.id.swipeRefreshLayout)
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    var viewer: Viewer? = null

    var profilePresenter = ProfilePresenter(this)

    override fun setupView(savedInstanceState: Bundle?) {
        profilePresenter.fetchUserProfile(Constant.QUERY)
        setupRecycleView()
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun getLayoutResID() = R.layout.activity_profile

    override fun onProfileResponse(viewer: Viewer?) {
        viewer?.let {
            profilePresenter.setUpRepositoryList(viewer)
            setProfileData(viewer)
        }
    }

    // this method will setup recycle view
    private fun setupRecycleView() {
        recyclerView.layoutManager = (LinearLayoutManager(this))
        recyclerView.addItemDecoration(DividerItemDecoration(this, 0))
    }

    // this method will set adaptor to recycle view
    override fun setAdaptor(repositoryList: ArrayList<Repository>) {
        swipeRefreshLayout.isRefreshing = false
        recyclerView.adapter = PinnedRepoAdaptor(this, repositoryList, this)
    }

    override fun showProgress() {
        super.showProgressDialog()
    }

    override fun hideProgress() {
        super.hideProgressDialog()
    }

    // set data to profile
    private fun setProfileData(viewer: Viewer?) {

        this.viewer = viewer

        // set user data and return empty value if data is NULL
        userNameTxt.text = viewer?.name ?: getString(R.string.empty)
        userLoginTxt.text = viewer?.login ?: getString(R.string.empty)
        userEmail.text = viewer?.email
            ?: getString(R.string.sample_email) // sample email will return of email is empty

        viewer?.follower?.let {
            followerTxt.text = it.totalCount.toString()
        }

        viewer?.following?.let {
            following.text = it.totalCount.toString()
        }

        viewer?.avatarUrl?.let {
            Picasso.get().load(it).transform(CropCircleTransformation())
                .into(avatarImage)
        }

        // save user profile data
        viewer?.name?.let {
            saveName(it)
        }
        viewer?.avatarUrl?.let {
            saveAvatarUrl(it)
        }
    }

    // this method will run in background and save user name in data store pref
    private fun saveName(userName: String) = profilePresenter.saveUserName(userName)


    // this method will run in background and save user image url in data store pref
    private fun saveAvatarUrl(imageUrl: String) = profilePresenter.saveAvatarUrl(imageUrl)

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        profilePresenter.fetchUserProfile(Constant.QUERY)
        profilePresenter.saveCashData(viewer)
    }

    override fun invalidQuery() {
        Toast.makeText(this, getString(R.string.invalid_query), Toast.LENGTH_SHORT).show()
    }
}