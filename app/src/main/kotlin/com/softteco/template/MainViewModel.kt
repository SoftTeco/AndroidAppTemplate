package com.softteco.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.data.base.error.Result
import com.softteco.template.data.profile.ProfileRepository
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import io.shipbook.shipbooksdk.ShipBook
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val appDispatchers: AppDispatchers
) : ViewModel() {
    fun registerUserInShipBook() {
        viewModelScope.launch(appDispatchers.io) {
            when (val profile = repository.getUser()) {
                is Result.Success -> {
                    val profileData = profile.data
                    ShipBook.registerUser(
                        profileData.id.toString(),
                        profileData.firstName,
                        profileData.fullName(),
                        profileData.email,
                        null,
                        null
                    )
                }
                is Result.Error -> {
                    Timber.e("Error get user data")
                }
            }
        }
    }
}
