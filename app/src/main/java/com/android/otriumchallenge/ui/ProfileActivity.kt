package com.android.otriumchallenge.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
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

    @BindView(R.id.profileTextView)
    lateinit var profileTxt: TextView

    @BindView(R.id.followingLabel)
    lateinit var followingLabel: TextView

    @BindView(R.id.followersLabel)
    lateinit var followerLabel: TextView

    @BindView(R.id.avatarImageView)
    lateinit var avatarImage: ImageView

    @BindView(R.id.swipeRefreshLayout)
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var viewer: Viewer? = null

    private var isWelcomeMessageShown = false

    lateinit var profilePresenter: ProfilePresenter

    // this method will call in the first place
    override fun setupView(savedInstanceState: Bundle?) {
        profilePresenter = ProfilePresenter(appUtil, this)
        profilePresenter.fetchUserProfile()
        setupRecycleView()
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    // this method will set the layout
    override fun getLayoutResID() = R.layout.activity_profile

    // this method will call after the successful API call for use profile
    override fun onProfileResponse(viewer: Viewer?) {
        viewer?.let {
            profilePresenter.setUpRepositoryList(viewer)
            setProfileData(viewer)
        }
    }

    // this method will setup recycle view
    private fun setupRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = (layoutManager)
        recyclerView.addItemDecoration(DividerItemDecoration(this, 0))
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(false)
    }

    // this method will set adaptor to recycle view
    override fun setAdaptor(repositoryList: ArrayList<Repository>) {
        profileTxt.visibility = View.VISIBLE
        followerLabel.visibility = View.VISIBLE
        followingLabel.visibility = View.VISIBLE

        swipeRefreshLayout.isRefreshing = false
        recyclerView.adapter = PinnedRepoAdaptor(appUtil, repositoryList) // set the adaptor
    }

    // show progress when api call
    override fun showProgress() {
        super.showProgressDialog()
    }

    // hide progress when api result
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
            ?: getString(R.string.sample_email) // sample email will return if email is empty

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
        saveName(viewer?.name)

        saveAvatarUrl(viewer?.avatarUrl)

    }

    // this method will run in background and save user name in data store pref
    private fun saveName(userName: String?) =
        profilePresenter.saveUserName(userName)


    // this method will run in background and save user image url in data store pref
    private fun saveAvatarUrl(imageUrl: String?) =
        profilePresenter.saveAvatarUrl(imageUrl)

    // this method will call after pull refresh and re fetch user profile
    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        profilePresenter.fetchUserProfile()
        profilePresenter.saveCashData(viewer)
    }

    // this method will show welcome message only once
    override fun welcomeMessage() {
        if (!isWelcomeMessageShown) {
            Toast.makeText(this, getString(R.string.welcome_message), Toast.LENGTH_SHORT).show()
            isWelcomeMessageShown = true
        }
    }

    // error message for invalid GraphQL query
    override fun invalidQuery() {
        Toast.makeText(this, getString(R.string.invalid_query), Toast.LENGTH_SHORT).show()
        swipeRefreshLayout.isRefreshing = false

    }

    // error message for user authentication
    override fun authenticationError() {
        Toast.makeText(this, getString(R.string.authentication_error), Toast.LENGTH_SHORT).show()
        swipeRefreshLayout.isRefreshing = false
    }

    // this method will call after saving profile
    override fun onSaveCashedData() =
        Toast.makeText(this, getString(R.string.cashed_for_a_day), Toast.LENGTH_SHORT).show()

    // error for api call generate unknown
    override fun unknownError() =
        Toast.makeText(this, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()

    // error handling in BaseActivity when api call throws an error
    override fun handleApiError(it: Throwable) = handleApiError(this, it)
}