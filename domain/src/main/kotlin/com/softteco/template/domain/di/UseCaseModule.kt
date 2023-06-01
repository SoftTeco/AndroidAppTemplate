package com.softteco.template.domain.di

import com.softteco.template.domain.usecase.apientry.FetchApiEntriesUseCase
import com.softteco.template.domain.usecase.apientry.FetchApiEntriesUseCaseImpl
import com.softteco.template.domain.usecase.apientry.GetAllApiEntriesUseCase
import com.softteco.template.domain.usecase.apientry.GetAllApiEntriesUseCaseImpl
import com.softteco.template.domain.usecase.apientry.GetApiEntryByNameUseCase
import com.softteco.template.domain.usecase.apientry.ToggleFavoritesUseCase
import com.softteco.template.domain.usecase.apientry.ToggleFavoritesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.softteco.template.domain.usecase.apientry.GetApiEntryByNameUseCaseImpl as GetApiEntryByNameUseCaseImpl1

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    internal abstract fun bindApisUseCase(useCaseImpl: FetchApiEntriesUseCaseImpl): FetchApiEntriesUseCase

    @Binds
    @Singleton
    internal abstract fun bindToggleFavoritesUseCase(useCaseImpl: ToggleFavoritesUseCaseImpl): ToggleFavoritesUseCase

    @Binds
    @Singleton
    internal abstract fun bindGetAllApiEntriesUseCase(useCaseImpl: GetAllApiEntriesUseCaseImpl): GetAllApiEntriesUseCase

    @Binds
    @Singleton
    internal abstract fun bindGetApiEntryByNameUseCase(
        useCaseImpl: GetApiEntryByNameUseCaseImpl1
    ): GetApiEntryByNameUseCase

}
