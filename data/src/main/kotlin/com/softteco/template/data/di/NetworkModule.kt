package com.softteco.template.data.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.softteco.template.data.Config
import com.softteco.template.data.repository.user.UserRepositoryImpl
import com.softteco.template.data.source.remote.PublicApi
import com.softteco.template.data.source.remote.UserApiService
import com.softteco.template.domain.repository.AccountRepository
import com.softteco.template.domain.repository.user.UserRepository
import com.softteco.template.domain.usecase.account.RegistrationDb
import com.softteco.template.domain.usecase.account.UseCasesDb
import com.softteco.template.domain.usecase.user.*
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
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
    @Named("PublicApi")
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder().build()
        val converterFactory: Converter.Factory = MoshiConverterFactory.create(moshi)

        return Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }
    @Provides
    @Named("UserApi")
    fun provideRetrofitUser(okHttpClient: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder().build()
        val converterFactory: Converter.Factory = MoshiConverterFactory.create(moshi)

        return Retrofit.Builder()
            .baseUrl(Config.USER_URL)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideApiService(@Named("PublicApi") retrofit: Retrofit): PublicApi {
        return retrofit.create(PublicApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginService(@Named("UserApi") retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    fun provideUserRepository(
        apiService: UserApiService
    ): UserRepository = UserRepositoryImpl(apiService)

    @Provides
    fun provideUseCases(
        repo: UserRepository
    ) = UseCases(
        login = Login(repo),
        register = Registration(repo),
        restorePassword = RestorePassword(repo),
        resetPassword = ResetPassword(repo)
    )

    @Provides
    fun provideUseCasesDb(
        repo: AccountRepository
    ) = UseCasesDb(
        register = RegistrationDb(repo),
    )
}
