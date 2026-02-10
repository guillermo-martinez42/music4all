package com.music4all.app.data.api

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for the Odesli (Songlink) API.
 */
interface OdesliApiService {

    @GET("v1-alpha.1/links")
    suspend fun resolveLink(@Query("url") url: String): OdesliResponse
}
