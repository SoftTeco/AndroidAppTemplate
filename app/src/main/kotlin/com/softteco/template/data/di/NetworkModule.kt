package com.softteco.template.data.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.softteco.template.BuildConfig
import com.softteco.template.data.RestCountriesApi
import com.softteco.template.data.TemplateApi
import com.softteco.template.utils.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val json = Json { ignoreUnknownKeys = true }

    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return interceptor
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideTemplateApi(okHttpClient: OkHttpClient): TemplateApi {
        val retrofit = buildRetrofit(okHttpClient, BuildConfig.BASE_URL)
        return retrofit.create(TemplateApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRestCountriesApi(okHttpClient: OkHttpClient): RestCountriesApi {
        val retrofit = buildRetrofit(okHttpClient, RestCountriesApi.BASE_URL)
        return retrofit.create(RestCountriesApi::class.java)
    }

    @Suppress("SameParameterValue")
    private fun buildRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        val converterFactory = json.asConverterFactory("application/json".toMediaType())

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAppDispatchers(): AppDispatchers = AppDispatchers(
        ui = Dispatchers.Main,
        default = Dispatchers.Default,
        io = Dispatchers.IO,
        unconfined = Dispatchers.Unconfined
    )
}
