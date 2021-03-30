package com.android.otriumchallenge.api.retrofit

import com.android.otriumchallenge.api.response.UserResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST

interface EndpointService {
    @Headers(
        "Content-Type: application/json"
    )
    @POST("graphql")
    fun fetchUserFromGit(@Body body: String): Call<UserResponse>
}