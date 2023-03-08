package au.com.shiftyjelly.pocketcasts.a8ctv.usecase

import android.net.Uri
import androidx.core.text.HtmlCompat
import au.com.shiftyjelly.pocketcasts.a8ctv.server.WpComA8cTvServerManager
import au.com.shiftyjelly.pocketcasts.a8ctv.utils.A8C_TV_PODCAST_UUID
import au.com.shiftyjelly.pocketcasts.models.entity.Episode
import au.com.shiftyjelly.pocketcasts.models.entity.Podcast
import au.com.shiftyjelly.pocketcasts.servers.bumpstats.WpComServerManager
import au.com.shiftyjelly.pocketcasts.utils.extensions.parseIsoDate
import io.reactivex.Single
import java.time.Instant
import java.util.*
import javax.inject.Inject

class PrepareA8cTvPodcast @Inject internal constructor(
    private val wpComA8cTvServerManager: WpComA8cTvServerManager
) {

    operator fun invoke(): Single<Podcast> {
        return wpComA8cTvServerManager.getWpComPosts().map {
            Podcast().copy(
                uuid = A8C_TV_PODCAST_UUID,
                title = "Automattic.tv | Lights, Camera, Action!",
                podcastCategory = "news",
                podcastDescription = "Looking for a new way to connect with your coworkers and learn from their experiences? With Automattic.tv, you can watch your colleagues' videos and get inspired by their creativity.",
                podcastLanguage = "english",
                author = "Automattic"
            ).apply {
                episodes += it.posts.map { wpComPost ->
                    val embeddedVideoUrl = Uri.parse(
                        wpComPost.content
                            .split(" ")
                            .first { it.startsWith("src=") }
                            .removePrefix("src='")
                            .removeSuffix("'")
                    )

                    val guid = embeddedVideoUrl.lastPathSegment.orEmpty()

                    Episode(
                        episodeDescription = wpComPost.content,
                        title = HtmlCompat.fromHtml(
                            wpComPost.title,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        ).toString(),
                        podcastUuid = "a8c.tv",
                        fileType = "video/mp4",
                        downloadUrl = "a8c.tv?guid=${guid}",
                        uuid = "a8c.tv_${wpComPost.id}",
                        publishedDate = wpComPost.date.parseIsoDate() ?: Date.from(Instant.now())
                    )

                }
            }
        }
    }
}