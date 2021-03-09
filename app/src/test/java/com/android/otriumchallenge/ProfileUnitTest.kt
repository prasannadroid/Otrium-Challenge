package com.android.otriumchallenge

import android.os.storage.StorageManager
import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.api.retrofit.RestApi
import com.android.otriumchallenge.presenter.BasePresenter
import com.android.otriumchallenge.presenter.ProfilePresenter
import com.android.otriumchallenge.view.ProfileView
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import javax.inject.Inject

class ProfileUnitTest {

    @Mock
    lateinit var profileView: ProfileView

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun test_Invalid_Query() {

    }

    @Test
    fun test_Network_Error() {

    }

    @Test
    fun test_Cash_Saving() {

    }


}