package com.music4all.app.data.api

import com.google.gson.annotations.SerializedName

/**
 * Response model for the Odesli (Songlink) API.
 * Endpoint: GET https://api.song.link/v1-alpha.1/links?url={encoded_url}
 */
data class OdesliResponse(
    @SerializedName("entityUniqueId")
    val entityUniqueId: String?,

    @SerializedName("userCountry")
    val userCountry: String?,

    @SerializedName("pageUrl")
    val pageUrl: String?,

    @SerializedName("linksByPlatform")
    val linksByPlatform: Map<String, PlatformLink>?
)

data class PlatformLink(
    @SerializedName("url")
    val url: String,

    @SerializedName("entityUniqueId")
    val entityUniqueId: String?
)
