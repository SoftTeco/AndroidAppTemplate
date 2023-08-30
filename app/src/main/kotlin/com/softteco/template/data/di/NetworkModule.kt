package com.softteco.template.data.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.softteco.template.BuildConfig
import com.softteco.template.data.TemplateApi
import com.softteco.template.data.UserApi
import com.softteco.template.data.login.LoginRepository
import com.softteco.template.data.login.LoginRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Provides
	fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
		val interceptor = HttpLoggingInterceptor()
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
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

	@Suppress("SameParameterValue")
	private fun buildRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
		val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
		val converterFactory: Converter.Factory = MoshiConverterFactory.create(moshi)

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
	fun provideLoginApi(okHttpClient: OkHttpClient): UserApi {
		val retrofit = buildRetrofit(okHttpClient, "http://209.38.220.2:3148/swagger/api/") //TODO
		return retrofit.create(UserApi::class.java)
	}
}
