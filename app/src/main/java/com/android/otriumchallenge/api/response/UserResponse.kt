package com.android.otriumchallenge.api.response

import com.android.otriumchallenge.api.model.Data

/**
 * User response class will get the HTTP serialized response from retrofit.
 *
 * @constructor Create empty User response
 */
open class UserResponse {
    var data: Data? = null
}