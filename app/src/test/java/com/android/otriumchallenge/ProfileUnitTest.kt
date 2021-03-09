package com.android.otriumchallenge

import android.app.Application
import android.os.storage.StorageManager
import com.android.otriumchallenge.api.retrofit.RestApi
import com.android.otriumchallenge.presenter.BasePresenter
import com.android.otriumchallenge.presenter.ProfilePresenter
import com.android.otriumchallenge.util.Constant
import com.android.otriumchallenge.view.ProfileView
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import javax.inject.Inject

class ProfileUnitTest {

    lateinit var profilePresenter: ProfilePresenter

    @Mock
    lateinit var profileView: ProfileView


    @Inject
    lateinit var restApi: RestApi


    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)

        Mockito.mock(JSONObject::class.java)
        Mockito.mock(BasePresenter::class.java)
        Mockito.mock(App::class.java)
        Mockito.mock(RestApi::class.java)
        Mockito.mock(StorageManager::class.java)
        profilePresenter = ProfilePresenter(profileView)
    }

    @Test
    fun run_Invalid_Query() {
        profilePresenter.fetchUserProfile("")
        verify(profileView, never()).invalidQuery()
    }
}