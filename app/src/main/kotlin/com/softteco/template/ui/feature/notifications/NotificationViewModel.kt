package com.softteco.template.ui.feature.notifications

import android.content.Intent
import androidx.core.app.RemoteInput
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softteco.template.Constants.NOTIFICATION_REPLY
import com.softteco.template.utils.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val appDispatchers: AppDispatchers) :
    ViewModel() {
    private val _enteredText = MutableStateFlow("")
    val enteredText = _enteredText.asStateFlow()
    fun processRemoteInput(remoteInput: Intent) {
        viewModelScope.launch(appDispatchers.default) {
            val enteredText =
                RemoteInput.getResultsFromIntent(remoteInput)?.getCharSequence(NOTIFICATION_REPLY)
                    ?.toString() ?: ""
            _enteredText.value = enteredText
        }
    }
}
