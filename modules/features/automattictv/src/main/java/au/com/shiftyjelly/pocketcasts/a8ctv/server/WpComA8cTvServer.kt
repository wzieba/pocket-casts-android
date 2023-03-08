package au.com.shiftyjelly.pocketcasts.a8ctv.server

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WpComA8cTvServer {
    @GET("/rest/v1.1/sites/a8c.tv/posts")
    fun getWpComPosts(
        @Header("Authorization") authHeader: String,
        @Query("number") number: Int = 100,
    ): Single<WpComPostsResponse>

    @GET("/rest/v1.1/videos/{video_guid}")
    fun getVideoDetails(
        @Header("Authorization") authHeader: String,
        @Path("video_guid") videoGuid: String
    ): Single<VideoResponse>

    @POST("/wpcom/v2/sites/a8c.tv/media/videopress-playback-jwt/{video_guid}")
    fun getMetaToken(
        @Header("Authorization") authHeader: String,
        @Path("video_guid") videoGuid: String
    ): Single<TokenResponse>
}

@JsonClass(generateAdapter = true)
data class WpComPostsResponse(
    @field:Json(name = "posts") val posts: List<WpComPostResponse>,
)

@JsonClass(generateAdapter = true)
data class WpComPostResponse(
    @field:Json(name = "content") val content: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "ID") val id: String,
    @field:Json(name = "date") val date: String
)

@JsonClass(generateAdapter = true)
data class VideoResponse(
    @field:Json(name = "title") val title: String,
    @field:Json(name = "original") val original: String,
)

@JsonClass(generateAdapter = true)
data class VideoFileResponse(
    @field:Json(name = "mp4") val mp4: String
)

@JsonClass(generateAdapter = true)
data class TokenResponse(
    @field:Json(name = "metadata_token") val token: String
)