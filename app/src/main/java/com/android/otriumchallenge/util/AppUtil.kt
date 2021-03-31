package com.android.otriumchallenge.util

import android.content.Context
import com.android.otriumchallenge.api.retrofit.EndpointService
import com.android.otriumchallenge.storage.StorageManager
import javax.inject.Inject


open class AppUtil @Inject constructor(
    var context: Context,
    var endPoint: EndpointService,
    var storageManager: StorageManager
)