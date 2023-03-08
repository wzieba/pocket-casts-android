package au.com.shiftyjelly.pocketcasts.a8ctv.usecase

import android.net.Uri
import au.com.shiftyjelly.pocketcasts.a8ctv.server.WpComA8cTvServerManager
import au.com.shiftyjelly.pocketcasts.models.db.AppDatabase
import au.com.shiftyjelly.pocketcasts.models.entity.Episode
import javax.inject.Inject

class PrepareA8cTvEpisode @Inject internal constructor(
    appDatabase: AppDatabase,
    private val wpComA8cTvServerManager: WpComA8cTvServerManager
) {

    private val episodeDao = appDatabase.episodeDao()

    operator fun invoke(uuid: String): Episode {
        return episodeDao.findByUuid(uuid).let { originalEpisode ->
            val downloadUri = Uri.parse(originalEpisode!!.downloadUrl)

            val videoGuid = downloadUri.getQueryParameter("guid") ?: downloadUri.pathSegments[0].orEmpty()

            val videoDetails = wpComA8cTvServerManager.getVideoDetails(videoGuid).blockingGet()
            val getMetaToken = wpComA8cTvServerManager.getMetaToken(videoGuid).blockingGet()

            val updatedEpisode = originalEpisode.copy(
                downloadUrl = "${videoDetails.original}?metadata_token=${getMetaToken.token}"
            )
            episodeDao.update(updatedEpisode)
            updatedEpisode
        }
    }
}