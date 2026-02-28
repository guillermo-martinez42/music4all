package com.music4all.app.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.music4all.app.data.api.RetrofitInstance
import com.music4all.app.data.preferences.UserPreferencesRepository
import com.music4all.app.domain.UrlValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Transparent "trampoline" activity (Theme.NoDisplay).
 *
 * Flow:
 * 1. Extracts the URL from the incoming intent.
 * 2. Validates the URL against the security allowlist.
 * 3. Reads the user's preferred music service from DataStore.
 * 4. Calls the Odesli API to resolve the entity across platforms.
 * 5. Launches the preferred service's URL, or falls back to a Chrome Custom Tab.
 * 6. Calls finish() immediately to remain invisible.
 */
class LinkResolverActivity : Activity() {

    companion object {
        private const val TAG = "LinkResolver"
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val incomingUrl = intent?.data?.toString()

        // ── Security: validate the incoming URL ──────────────────────────
        if (incomingUrl == null || !UrlValidator.isAllowedMusicUrl(incomingUrl)) {
            Log.w(TAG, "Blocked invalid or missing URL: $incomingUrl")
            finish()
            return
        }

        Log.d(TAG, "Intercepted URL: $incomingUrl")

        scope.launch {
            try {
                resolveAndOpen(incomingUrl)
            } catch (e: Exception) {
                Log.e(TAG, "Resolution failed, falling back to Custom Tab", e)
                openInCustomTab(incomingUrl)
            } finally {
                // Ensure the trampoline activity always finishes
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    /**
     * Calls the Odesli API and opens the resolved URL for the user's preferred service.
     */
    private suspend fun resolveAndOpen(originalUrl: String) {
        val preferencesRepository = UserPreferencesRepository(applicationContext)
        val preferredService = preferencesRepository.getPreferredService()

        Log.d(TAG, "Preferred service: ${preferredService.displayName} (key=${preferredService.odesliKey})")

        // ── Network call on IO dispatcher ────────────────────────────────
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.api.resolveLink(originalUrl)
        }

        val targetUrl = response.linksByPlatform
            ?.get(preferredService.odesliKey)
            ?.url

        if (targetUrl != null && UrlValidator.isAllowedTargetUrl(targetUrl)) {
            Log.d(TAG, "Resolved to: $targetUrl")
            launchUrl(targetUrl)
        } else {
            if (targetUrl != null) {
                Log.w(TAG, "Resolved URL was blocked by validator: $targetUrl")
            } else {
                Log.d(TAG, "No ${preferredService.displayName} link found")
            }
            openInCustomTab(originalUrl)
        }
    }

    /**
     * Launches the given URL via ACTION_VIEW, which will typically open the target
     * music app if it's installed, or the browser otherwise.
     */
    private fun launchUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to launch URL: $url", e)
            openInCustomTab(url)
        }
    }

    /**
     * Opens the URL in a Chrome Custom Tab as the ultimate fallback.
     */
    private fun openInCustomTab(url: String) {
        try {
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
            customTabsIntent.launchUrl(this, Uri.parse(url))
        } catch (e: Exception) {
            Log.e(TAG, "Custom Tab launch failed, opening in browser", e)
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            } catch (e2: Exception) {
                Log.e(TAG, "All launch methods failed", e2)
                Toast.makeText(this, "Could not open link", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
