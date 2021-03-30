package com.android.otriumchallenge

import android.content.Context
import com.android.otriumchallenge.api.response.UserResponse
import com.android.otriumchallenge.api.retrofit.EndpointService
import com.android.otriumchallenge.presenter.ProfilePresenter
import com.android.otriumchallenge.util.AppUtil
import com.android.otriumchallenge.util.Constant
import com.android.otriumchallenge.view.ProfileView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class ApiTest {

    @Mock
    lateinit var profileView: ProfileView

    @Mock
    lateinit var appUtil: AppUtil

    @Mock
    lateinit var endpointService: EndpointService

    @Mock
    lateinit var callResponse: Call<UserResponse>

    @Mock
    lateinit var response: UserResponse

    @Mock
    lateinit var throwable: Throwable

    private lateinit var profilePresenter: ProfilePresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.openMocks(this)
        appUtil.endPoint = endpointService
        profilePresenter = ProfilePresenter(appUtil, profileView)

    }

    @Test
    fun test_Success_Endpoint_Call() {

        `when`(endpointService.fetchUserFromGit(Constant.QUERY)).thenReturn(callResponse)

        doAnswer {
            val callBack: Callback<UserResponse> = it.getArgument(0)

            callBack.onResponse(callResponse, Response.success(response))
        }.`when`(callResponse).enqueue(any())

        profilePresenter.fetchUserProfile()
        verify(profileView).onProfileResponse(null)
    }

    @Test
    fun test_Failure_Endpoint_Call() {

        `when`(endpointService.fetchUserFromGit(Constant.QUERY)).thenReturn(callResponse)

        doAnswer {
            val callBack: Callback<UserResponse> = it.getArgument(0)

            callBack.onFailure(callResponse, throwable)

        }.`when`(callResponse).enqueue(any())

        profilePresenter.fetchUserProfile()
        verify(profileView).handleApiError(throwable)

    }

}