package com.music4all.app.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

// Note: Using Robolectric because UrlValidator depends on android.net.Uri
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UrlValidatorTest {

    @Test
    fun `isAllowedMusicUrl allows valid Spotify link`() {
        assertTrue(UrlValidator.isAllowedMusicUrl("https://open.spotify.com/track/123"))
    }

    @Test
    fun `isAllowedMusicUrl blocks non-HTTPS link`() {
        assertFalse(UrlValidator.isAllowedMusicUrl("http://open.spotify.com/track/123"))
    }

    @Test
    fun `isAllowedMusicUrl blocks untrusted host`() {
        assertFalse(UrlValidator.isAllowedMusicUrl("https://malicious.com/track/123"))
    }

    @Test
    fun `isAllowedMusicUrl blocks null or empty`() {
        assertFalse(UrlValidator.isAllowedMusicUrl(null))
        assertFalse(UrlValidator.isAllowedMusicUrl(""))
    }

    @Test
    fun `isAllowedMusicUrl blocks @ injection`() {
        // android.net.Uri handles this correctly - host will be evil.com
        assertFalse(UrlValidator.isAllowedMusicUrl("https://open.spotify.com@evil.com/track/123"))
    }

    @Test
    fun `isAllowedTargetUrl allows valid Apple Music link`() {
        assertTrue(UrlValidator.isAllowedTargetUrl("https://music.apple.com/album/123"))
    }

    @Test
    fun `isAllowedTargetUrl blocks suspicious target`() {
        assertFalse(UrlValidator.isAllowedTargetUrl("https://evil-music.com/track/123"))
    }
}
