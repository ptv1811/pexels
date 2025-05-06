package com.vanluong.network

import com.vanluong.network.service.PexelsService
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.jvm.Throws

/**
 * Created by van.luong
 * on 06,May,2025
 */
class PexelsServiceTest : ApiAbstract<PexelsService>() {
    private lateinit var service: PexelsService

    @Before
    fun initService() {
        service = createService(PexelsService::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun fetchPexelsPhotoListFromNetworkTest() = runTest {
        enqueueResponse("/PhotoListResponse.json")
        val response = service.searchImage("nature", 3)
        val networkResult = response.getOrNull()
        assertThat(response.isSuccess, `is`(true))

        assertNotNull(networkResult)

        assertThat(networkResult!!.page, `is`(1))
        assertThat(networkResult.perPage, `is`(3))
        assertThat(networkResult.photos.size, `is`(3))
        assertThat(networkResult.totalResults, `is`(8000))
        assertThat(
            networkResult.nextPage,
            `is`("https://api.pexels.com/v1/search?page=2&per_page=3&query=nature")
        )

        val firstPhoto = networkResult.photos[0]
        assertThat(firstPhoto.id, `is`(2325447))
        assertThat(firstPhoto.width, `is`(5184))
        assertThat(firstPhoto.height, `is`(3456))
        assertThat(firstPhoto.photographer, `is`("Francesco Ungaro"))
        assertThat(
            firstPhoto.url,
            `is`("https://www.pexels.com/photo/hot-air-balloon-2325447/")
        )
    }
}