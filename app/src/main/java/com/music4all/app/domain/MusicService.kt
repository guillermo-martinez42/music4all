package com.music4all.app.domain

/**
 * Enum representing the supported music services.
 *
 * @property odesliKey   The key used in the Odesli API's `linksByPlatform` map.
 * @property displayName Human-readable name for UI display.
 * @property emoji       Emoji icon for visual identification in the settings screen.
 */
enum class MusicService(
    val odesliKey: String,
    val displayName: String,
    val emoji: String
) {
    SPOTIFY(
        odesliKey = "spotify",
        displayName = "Spotify",
        emoji = "🟢"
    ),
    APPLE_MUSIC(
        odesliKey = "appleMusic",
        displayName = "Apple Music",
        emoji = "🍎"
    ),
    YOUTUBE_MUSIC(
        odesliKey = "youtubeMusic",
        displayName = "YouTube Music",
        emoji = "🔴"
    ),
    TIDAL(
        odesliKey = "tidal",
        displayName = "Tidal",
        emoji = "🌊"
    ),
    DEEZER(
        odesliKey = "deezer",
        displayName = "Deezer",
        emoji = "💜"
    );
}
