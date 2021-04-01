package com.android.otriumchallenge

import android.content.SharedPreferences
import com.android.otriumchallenge.api.model.Viewer
import com.android.otriumchallenge.presenter.ProfilePresenter
import com.android.otriumchallenge.storage.StorageManager
import com.android.otriumchallenge.util.AppUtil
import com.android.otriumchallenge.view.ProfileView
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(StorageManager::class)
class ProfileTest {

    @Mock
    lateinit var profileView: ProfileView

    @Mock
    lateinit var appUtil: AppUtil

    @Mock
    lateinit var viewer: Viewer

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var profilePresenter: ProfilePresenter

    private val extraTimeMills = 1000L

    private lateinit var storageManager: StorageManager


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        storageManager =
            PowerMockito.mock(
                StorageManager::class.java,
                withSettings().useConstructor(sharedPreferences)
            )
        appUtil.storageManager = storageManager
        profilePresenter = ProfilePresenter(appUtil, profileView)


    }

    @Test
    fun testUserName_WithNull() {
        profilePresenter.saveUserName(null)
        verify(storageManager, never()).saveUerName(anyString())
    }

    @Test
    fun testUserName_WithNotNull() {
        val sampleStr = "sample str"
        profilePresenter.saveUserName(sampleStr)
        verify(storageManager).saveUerName(anyString())
    }

    @Test
    fun testUserImage_WithNull() {
        profilePresenter.saveAvatarUrl(null)
        verify(storageManager, never()).saveUserImageUrl(anyString())
    }

    @Test
    fun testUserImage_WithNotNull() {
        val sampleStr = "sample str"
        profilePresenter.saveAvatarUrl(sampleStr)
        verify(storageManager).saveUserImageUrl(anyString())
    }


    @Test
    fun testRepositoryList_WithNull() {
        profilePresenter.setUpRepositoryList(null)
        verify(profileView, never()).welcomeMessage()
    }

    @Test
    fun testRepositoryList_WithViewer() {
        profilePresenter.setUpRepositoryList(viewer)
        verify(profileView).welcomeMessage()
    }

    @Test
    fun testCashedData_WithNull() {
        CoroutineScope(Dispatchers.Main).async {
            profilePresenter.saveCashData(null)
            verify(profileView, never()).onSaveCashedData()
        }.cancel()
    }

    @Test
    fun testCashedData_WithNoDateExceeded() {
        CoroutineScope(Dispatchers.Main).async {

            // add extra milliseconds to make last saved date grater than current date (time mills)
            `when`(profilePresenter.getLastSavedDate()).thenReturn(System.currentTimeMillis() + extraTimeMills)

            profilePresenter.saveCashData(viewer)
            verify(profileView, never()).onSaveCashedData()
        }.cancel()
    }

    @Test
    fun testCashedData_WithDateExceeded() {
        CoroutineScope(Dispatchers.Main).async {

            // reduce extra milliseconds to make last saved date less than current date (time mills)
            `when`(profilePresenter.getLastSavedDate())
                .thenReturn(System.currentTimeMillis() - extraTimeMills)

            `when`(storageManager.saveCashedTimeMills(anyLong())).thenReturn(true)
            `when`(storageManager.saveCashedData(viewer)).thenReturn(true)

            profilePresenter.saveCashData(viewer)
            verify(profileView).onSaveCashedData()
        }.cancel()
    }

}