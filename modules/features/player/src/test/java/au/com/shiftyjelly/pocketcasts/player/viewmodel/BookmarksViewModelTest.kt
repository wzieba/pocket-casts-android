package au.com.shiftyjelly.pocketcasts.player.viewmodel

import au.com.shiftyjelly.pocketcasts.models.entity.BaseEpisode
import au.com.shiftyjelly.pocketcasts.models.to.SignInState
import au.com.shiftyjelly.pocketcasts.player.util.MainCoroutineRule
import au.com.shiftyjelly.pocketcasts.repositories.bookmark.BookmarkManager
import au.com.shiftyjelly.pocketcasts.repositories.podcast.EpisodeManager
import au.com.shiftyjelly.pocketcasts.repositories.user.UserManager
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.rx2.asFlowable
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.UUID

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BookmarksViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var bookmarkManager: BookmarkManager

    @Mock
    private lateinit var episodeManager: EpisodeManager

    @Mock
    private lateinit var userManager: UserManager

    @Mock
    private lateinit var episode: BaseEpisode

    @Mock
    private lateinit var signInState: SignInState

    private lateinit var bookmarksViewModel: BookmarksViewModel
    private val episodeUuid = UUID.randomUUID().toString()

    @Before
    fun setUp() = runTest {
        whenever(signInState.isSignedInAsPlusOrPatron).thenReturn(true)
        whenever(userManager.getSignInState()).thenReturn(flowOf(signInState).asFlowable())

        whenever(episodeManager.findEpisodeByUuid(episodeUuid)).thenReturn(episode)

        bookmarksViewModel = BookmarksViewModel(
            bookmarkManager = bookmarkManager,
            episodeManager = episodeManager,
            userManager = userManager,
            ioDispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `given no bookmarks, when bookmarks loaded, then Empty state shown`() = runTest {
        whenever(bookmarkManager.findEpisodeBookmarks(episode)).thenReturn(flowOf(emptyList()))

        bookmarksViewModel.loadBookmarks(episodeUuid)

        assertTrue(bookmarksViewModel.uiState.value is BookmarksViewModel.UiState.Empty)
    }

    @Test
    fun `given bookmarks present, when bookmarks loaded, then Loaded state shown`() = runTest {
        whenever(bookmarkManager.findEpisodeBookmarks(episode)).thenReturn(flowOf(listOf(mock())))

        bookmarksViewModel.loadBookmarks(episodeUuid)

        assertTrue(bookmarksViewModel.uiState.value is BookmarksViewModel.UiState.Loaded)
    }

    @Test
    fun `given free account, when bookmarks loaded, then PlusUpsell state shown`() = runTest {
        whenever(signInState.isSignedInAsPlusOrPatron).thenReturn(false)

        bookmarksViewModel.loadBookmarks(episodeUuid)

        assertTrue(bookmarksViewModel.uiState.value is BookmarksViewModel.UiState.PlusUpsell)
    }

    @Test
    fun `given plus or patron account, when bookmarks loaded, then PlusUpsell state not shown`() = runTest {
        whenever(signInState.isSignedInAsPlusOrPatron).thenReturn(true)

        bookmarksViewModel.loadBookmarks(episodeUuid)

        assertFalse(bookmarksViewModel.uiState.value is BookmarksViewModel.UiState.PlusUpsell)
    }
}