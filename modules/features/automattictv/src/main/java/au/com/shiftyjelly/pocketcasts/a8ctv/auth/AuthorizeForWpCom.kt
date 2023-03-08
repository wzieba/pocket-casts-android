package au.com.shiftyjelly.pocketcasts.a8ctv.auth

import android.content.Context
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import au.com.shiftyjelly.pocketcasts.a8ctv.repository.WpComAuthTokenRepository
import javax.inject.Inject

class AuthorizeForWpCom @Inject internal constructor(
    private val wpComAuthTokenRepository: WpComAuthTokenRepository
) {

    operator fun invoke(
        context: Context,
        onSuccess: () -> Unit,
    ) {
        var dialog: AlertDialog? = null
        val webView = WebView(context).apply {
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest
                ): Boolean {

                    val accessToken =
                        Uri.parse(request.url.toString().replace("#", "?"))
                            .getQueryParameter("access_token").orEmpty()

                    return if (accessToken.isNotEmpty()) {
                        wpComAuthTokenRepository.saveToken(accessToken)
                        onSuccess.invoke()
                        dialog?.dismiss()
                        true
                    } else {
                        false
                    }

                }
            }
            settings.javaScriptEnabled = true
            loadUrl("https://public-api.wordpress.com/oauth2/authorize?client_id=85091&redirect_uri=https://pocketcasts.com/&response_type=token&blog=14140874")
        }
        dialog = AlertDialog.Builder(context)
            .setView(webView)
            .show()
    }
}