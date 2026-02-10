# Music4All

A Universal Music Link Opener for Android.

Share or tap a music link from Spotify, Apple Music, YouTube Music, Tidal, or Deezer —
Music4All automatically redirects it to your preferred service.

## Setup

1. Open this project in **Android Studio**.
2. Let Gradle sync complete.
3. Build & run on a device or emulator (API 26+).
4. Select your preferred music service on the settings screen.
5. **Android 12+**: Go to *Settings → Apps → Music4All → Open by default* and enable supported links.

## Architecture

- **Language**: Kotlin
- **Architecture**: MVVM + Clean Architecture layers
- **UI**: Jetpack Compose (Material 3)
- **Networking**: Retrofit + OkHttp → Odesli (Songlink) API
- **Storage**: Jetpack DataStore (Preferences)
- **Security**: Strict URL allowlist validation to prevent Open Redirect attacks
