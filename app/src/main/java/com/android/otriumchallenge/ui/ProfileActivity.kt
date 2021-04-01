package com.android.otriumchallenge.ui

import android.annotation.SuppressLint
import android.net.Network
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import com.android.otriumchallenge.R
import com.android.otriumchallenge.adapter.PinnedRepoAdaptor
import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.network.ConnectionStateMonitor
import com.android.otriumchallenge.network.NetworkStatus
import com.android.otriumchallenge.interfaces.OnNetworkAvailableListener
import com.android.otriumchallenge.model.Repository
import com.android.otriumchallenge.presenter.ProfilePresenter
import com.android.otriumchallenge.view.ProfileView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * Profile activity will display the profile and its related all views as one screen.
 *
 * @constructor Create empty Profile activity
 */
@SuppressLint("NonConstantResourceId")
class ProfileActivity : BaseActivity(), ProfileView, SwipeRefreshLayout.OnRefreshListener,
    OnNetworkAvailableListener {

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

    private lateinit var snackBar: Snackbar

    private lateinit var connectionStateMonitor: ConnectionStateMonitor

    private var isFetchDataRxCallStart = false

    /**
     * Setup view will call at the first time of the activity.
     *
     * @param savedInstanceState
     */
    override fun setupView(savedInstanceState: Bundle?) {

        //initialize profile presenter
        profilePresenter = ProfilePresenter(appUtil, this)

        // initialize recycle view
        setupRecycleView()

        // initialize snack bar
        setupSnackBar()

        // set OnRefreshListener to profile activity
        swipeRefreshLayout.setOnRefreshListener(this)

        // fetch user profile from graphQL via HTTP
        profilePresenter.fetchUserProfile()

        isFetchDataRxCallStart = true

        // register broadcast receiver
        registerNetworkBroadCastReceiver()

    }

    /**
     * set layout to the base activity content view.
     */
    override fun getLayoutResID() = R.layout.activity_profile

    // this method will call after the successful API call for use profile
    override fun onProfileResponse(viewer: Viewer?) {
        viewer?.let {
            setProfileData(viewer)
            profilePresenter.setUpRepositoryList(viewer)
        }
    }

    /**
     * Setup snack bar will show when device doesn't have Network connection.
     *
     */
    private fun setupSnackBar() {
        snackBar = Snackbar.make(
            swipeRefreshLayout,
            getString(R.string.no_network_connection),
            Snackbar.LENGTH_INDEFINITE
        )
        var params = snackBar.view.layoutParams as FrameLayout.LayoutParams
        params = params.apply {
            gravity = Gravity.TOP // will display in the top
        }

        // set parent layout params with gravity top
        snackBar.view.layoutParams = params
        snackBar.dismiss() // make initially dismiss to make isShows False
    }

    /** this method will setup recycle view */
    private fun setupRecycleView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = (layoutManager)
        recyclerView.addItemDecoration(DividerItemDecoration(this, 0))
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(false)
    }

    /** this method will set adaptor to recycle view */
    override fun setAdaptor(repositoryList: ArrayList<Repository>) {

        // visible all the labels when result is ready to display
        profileTxt.visibility = View.VISIBLE
        followerLabel.visibility = View.VISIBLE
        followingLabel.visibility = View.VISIBLE

        managePullToRefresh(false)
        recyclerView.adapter = PinnedRepoAdaptor(appUtil, repositoryList) // set the adaptor
    }

    /** show progress when api call. */
    override fun showProgress() = super.showProgressDialog()

    /** hide progress when api result. */
    override fun hideProgress() {
        super.hideProgressDialog()
        isFetchDataRxCallStart = false
    }

    /**
     * Set profile data will set all the profile related information including
     * user name,user image, email and followers.
     *
     * @param viewer will hold user information from graphQL object.
     */
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

        // save user avatar url
        saveAvatarUrl(viewer?.avatarUrl)

    }

    /**
     * Save name will passe user name to the presenter layer.
     *
     * @param userName user name of github user
     */
    private fun saveName(userName: String?) =
        profilePresenter.saveUserName(userName)


    /**
     * Save avatar url method will pass user avatar url to the presenter layer.
     *
     * @param imageUrl user image url of github user
     */
    private fun saveAvatarUrl(imageUrl: String?) =

        //save avatar will run in the background
        profilePresenter.saveAvatarUrl(imageUrl)

    /**
     * On refresh method will call when user pull to refresh the profile.
     *
     */
    override fun onRefresh() {
        managePullToRefresh(true)
        profilePresenter.fetchUserProfile()

        // save cashed data in background
        lifecycleScope.launch {
            profilePresenter.saveCashData(viewer)
        }.start()
    }

    /**
     * Welcome message method will call only one time after user navigate to the profile.
     *
     */
    override fun welcomeMessage() {
        if (!isWelcomeMessageShown) {
            Toast.makeText(this, getString(R.string.welcome_message), Toast.LENGTH_SHORT).show()
            isWelcomeMessageShown = true
        }
    }

    /**
     * Invalid query method will call from the view when graphQA is wrong.
     *
     */
    override fun invalidQuery() {
        Toast.makeText(this, getString(R.string.invalid_query), Toast.LENGTH_SHORT).show()
        managePullToRefresh(false)
    }

    /**
     * Authentication method error will be call when HTTP code is 401.
     *
     */
    override fun authenticationError() {
        Toast.makeText(this, getString(R.string.authentication_error), Toast.LENGTH_SHORT).show()
        managePullToRefresh(false)
    }

    /**
     * On save cashed data method will save user profile data for every 24 hours if user pull and refresh the
     * profile.
     *
     */
    override fun onSaveCashedData() =
        Toast.makeText(this, getString(R.string.cashed_for_a_day), Toast.LENGTH_SHORT).show()

    /**
     * Unknown error method call when RX call error beyond HTTP codes.
     *
     */
    override fun unknownError() =
        Toast.makeText(this, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()

    /**
     * Handle api error method will call when error in the retrofit call.
     *
     *
     * @param it throwable
     */
    override fun handleApiError(it: Throwable) {
        managePullToRefresh(false)
        handleApiError(this, it)
    }

    /**
     * Set up network broad cast receiver method will register ConnectionStateMonitor.
     *
     */
    private fun registerNetworkBroadCastReceiver() {
        connectionStateMonitor = ConnectionStateMonitor(this)
        connectionStateMonitor.enable(this)
    }

    /**
     * On network available method will call when after user enable WIFI or Mobile Data.
     *
     * @param network
     */
    override fun onNetworkAvailable(network: Network) {
        println("///// snackBar.isShown "+snackBar.isShown)
        if (snackBar.isShown) {
            snackBar.dismiss()

        }
        managePullToRefresh(false)

    }

    /**
     * On network disable method will call after user disable WIFI or Mobile Data.
     *
     * @param network
     */
    override fun onNetworkDisable(network: Network) {
        if (!NetworkStatus.isInternetAvailable(this)) {
            snackBar.show()
        }
        managePullToRefresh(false)
    }

    /**
     * Manage pull to refresh method will call from main thread and background thread both and
     * this method will able to update refresh listener in Main thread
     *
     * @param isRefreshing manage the refresh UI
     */
    private fun managePullToRefresh(isRefreshing: Boolean) {
        CoroutineScope(Dispatchers.Main).async {
            swipeRefreshLayout.isRefreshing = isRefreshing
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionStateMonitor.unregister()
    }
}