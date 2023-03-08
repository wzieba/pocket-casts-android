package au.com.shiftyjelly.pocketcasts.a8ctv.utils

import au.com.shiftyjelly.pocketcasts.models.entity.Playable

const val A8C_TV_PODCAST_UUID = "a8c.tv"

fun Playable.isA8cTv() = uuid.contains(A8C_TV_PODCAST_UUID)

