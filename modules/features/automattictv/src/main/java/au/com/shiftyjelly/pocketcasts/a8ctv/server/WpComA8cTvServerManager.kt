package au.com.shiftyjelly.pocketcasts.a8ctv.server

import au.com.shiftyjelly.pocketcasts.a8ctv.repository.WpComAuthTokenRepository
import au.com.shiftyjelly.pocketcasts.servers.bumpstats.WpComServer
import au.com.shiftyjelly.pocketcasts.servers.di.WpComServerRetrofit
import io.reactivex.Single
import javax.inject.Inject
import retrofit2.Retrofit

internal class WpComA8cTvServerManager @Inject constructor(
    @WpComServerRetrofit retrofit: Retrofit,
    private val wpComAuthTokenRepository: WpComAuthTokenRepository
) {

    private val server: WpComA8cTvServer = retrofit.create(WpComA8cTvServer::class.java)

    private val authHeader
        get() = "Bearer ${wpComAuthTokenRepository.getToken()}"

    fun getWpComPosts(): Single<WpComPostsResponse> {
        return server.getWpComPosts(authHeader)
    }

    fun getVideoDetails(videoGuid: String): Single<VideoResponse> {
        return server.getVideoDetails(
            authHeader = authHeader,
            videoGuid = videoGuid
        )
    }

    fun getMetaToken(videoGuid: String): Single<TokenResponse> {
        return server.getMetaToken(
            authHeader = authHeader,
            videoGuid = videoGuid
        )
    }
}
