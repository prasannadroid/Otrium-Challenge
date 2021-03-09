package com.android.otriumchallenge.util

class Constant {

    companion object {
        const val STATUS_SUCCESS: Int = 200
        const val TWENTY_SECONDS: Long = 20
        const val THIRTY_SECONDS: Long = 30
        const val SHARED_PREF = "Otrium data store"
        const val DATA_STORE_NAME = "Otrium data store"
        const val HEADER_AUTHORIZATION = "Authorization"
        const val BASE_URL = "https://api.github.com/"
        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val HEADER_APPLICATION_JSON = "application/json"
        const val BEARER_TOKEN = "bearer d42fde4722812fe3256ade4c9593ee9ff8cf1196"
        const val QUERY_KEY = "query"
        const val USER_NAME = "user_name"
        const val USER_IMAGE_URL = "user_image_url"
        const val CASHED_DATA = "cashed_data"
        const val CASHED_TIME = "cashed_time"

        const val QUERY = "query { " +
                "  viewer{" +
                "    name" +
                "    login" +
                "    avatarUrl " +
                "    followers(first: 10) {" +
                "      totalCount" +
                "    }" +
                "    following(first: 10) {" +
                "      totalCount" +
                "    }" +
                "    starredRepositories(first:10){" +
                "      nodes {" +
                "        ... on Repository {" +
                "          name" +
                "          stargazers{" +
                "            totalCount" +
                "          }" +
                "          description" +
                "          primaryLanguage{" +
                "            name" +
                "            color" +
                "          }" +
                "        }" +
                "      }" +
                "    }" +
                "    " +
                "    topRepositories(first:10, orderBy:{field:CREATED_AT ,direction: ASC}){" +
                "      nodes {" +
                "        ... on Repository {" +
                "          name" +
                "          stargazers{" +
                "            totalCount" +
                "          }" +
                "           description" +
                "          primaryLanguage{" +
                "            name" +
                "            color" +
                "          }" +
                "        }" +
                "      }" +
                "    }" +
                "    " +
                "    pinnedItems(first: 3, types: REPOSITORY) {" +
                "      nodes {" +
                "        ... on Repository {" +
                "          name" +
                "          stargazers{" +
                "            totalCount" +
                "          }" +
                "          description" +
                "          primaryLanguage{" +
                "            name" +
                "            color" +
                "          }" +
                "        }" +
                "      }" +
                "    }" +
                "  }" +
                "}"

    }
}