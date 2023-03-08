package au.com.shiftyjelly.pocketcasts.a8ctv.repository

import android.content.SharedPreferences
import au.com.shiftyjelly.pocketcasts.preferences.di.PrivateSharedPreferences
import au.com.shiftyjelly.pocketcasts.utils.extensions.getString
import javax.inject.Inject

internal class WpComAuthTokenRepository @Inject constructor(
    @PrivateSharedPreferences private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val WP_COM_ACCESS_TOKEN_KEY = "wp_com_access_token"
    }

    fun saveToken(accessToken: String) {
        sharedPreferences.edit().putString(WP_COM_ACCESS_TOKEN_KEY, accessToken).apply()
    }

    fun getToken(): String {
        return sharedPreferences.getString(WP_COM_ACCESS_TOKEN_KEY).orEmpty()
    }
}