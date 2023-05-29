package com.softteco.template.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.softteco.template.data.getDummyApiList
import com.softteco.template.data.source.remote.ApisRemoteDataSource
import com.softteco.template.data.source.remote.PublicApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.exceptions.base.MockitoException
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ApisRemoteDataSourceTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var retrofit: Retrofit

    @Mock
    lateinit var profService: PublicApi

    private lateinit var apisRemoteDataSource: ApisRemoteDataSource

    @Before
    fun setUp() {
        apisRemoteDataSource = ApisRemoteDataSource(retrofit, profService)
    }

    @Test
    fun `Given Apis When fetchApis returns Success`() = runBlocking {
        // GIVEN
        val givenApis = getDummyApiList()
        Mockito.`when`(profService.getAllEntries()).thenReturn(Response.success(givenApis))
        // WHEN
        val fetchedApis = apisRemoteDataSource.fetchEntries()
        // THEN
        assert(fetchedApis.data?.entries?.size == givenApis.entries.size)
    }

    @Test
    fun `Given Apis When fetchApis returns Error`() = runBlocking {
        // GIVEN
        val mockitoException = MockitoException("Unknown Error")
        Mockito.`when`(profService.getAllEntries()).thenThrow(mockitoException)
        // WHEN
        val fetchedApis = apisRemoteDataSource.fetchEntries()
        // THEN
        assert(fetchedApis.message == "Unknown Error")
    }

    @Test
    fun `Given Apis When fetchApis returns Server Error`() = runBlocking {
        // GIVEN
        Mockito.`when`(profService.getAllEntries())
            .thenReturn(Response.error(500, "".toResponseBody()))
        // WHEN
        val fetchedApis = apisRemoteDataSource.fetchEntries()
        // THEN
        assert(fetchedApis.message == "Unknown Error")
    }
}
