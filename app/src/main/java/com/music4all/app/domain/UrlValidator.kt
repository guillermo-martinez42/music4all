package com.music4all.app.domain

import android.net.Uri

/**
 * Validates intercepted URLs against an allowlist of known music domains.
 * Prevents Open Redirect attacks by ensuring only URLs from trusted music
 * service domains are forwarded to the Odesli API.
 */
object UrlValidator {

    private val ALLOWED_HOSTS = setOf(
        "open.spotify.com",
        "music.apple.com",
        "tidal.com",
        "listen.tidal.com",
        "youtu.be",
        "youtube.com",
        "www.youtube.com",
        "music.youtube.com",
        "www.deezer.com",
        "deezer.page.link"
    )

    /**
     * Returns `true` if [url] is a valid HTTPS URL from one of the allowed music domains.
     */
    fun isAllowedMusicUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false

        return try {
            val uri = Uri.parse(url)
            val scheme = uri.scheme?.lowercase()
            val host = uri.host?.lowercase()

            scheme == "https" && host != null && host in ALLOWED_HOSTS
        } catch (_: Exception) {
            false
        }
    }
}
