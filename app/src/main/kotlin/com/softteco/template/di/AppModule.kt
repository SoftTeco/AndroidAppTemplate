package com.softteco.template.di

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.softteco.template.ui.components.snackbar.SnackbarController
import com.softteco.template.utils.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSnackbarHostState(): SnackbarHostState = SnackbarHostState()

    @Provides
    fun provideSnackbarController(
        @ApplicationContext context: Context,
        snackbarHostState: SnackbarHostState,
        appDispatchers: AppDispatchers,
    ): SnackbarController {
        return SnackbarController(
            snackbarHostState,
            CoroutineScope(appDispatchers.io),
            context,
        )
    }
}
