package com.softteco.template.ui.feature.profile

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softteco.template.R
import com.softteco.template.data.profile.entity.Profile
import com.softteco.template.ui.components.Avatar
import com.softteco.template.ui.components.CustomTopAppBar
import com.softteco.template.ui.components.EditTextDialog
import com.softteco.template.ui.components.SecondaryButton
import com.softteco.template.ui.components.skeletonBackground
import com.softteco.template.ui.components.snackBar.SnackBarState
import com.softteco.template.ui.components.snackBar.SnackbarHandler
import com.softteco.template.ui.theme.AppTheme
import com.softteco.template.ui.theme.Dimens.PaddingDefault
import com.softteco.template.ui.theme.Dimens.PaddingLarge
import com.softteco.template.ui.theme.Dimens.PaddingNormal
import com.softteco.template.ui.theme.Dimens.PaddingSmall
import com.softteco.template.utils.Analytics
import com.softteco.template.utils.DateUtils
import java.util.Locale

@Composable
fun ProfileScreen(
    onBackClicked: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    Analytics.profileOpened()

    LaunchedEffect(state.profileState) {
        if (state.profileState is ProfileViewModel.GetProfileState.Logout) {
            onLogout()
        } else if (state.profileState == ProfileViewModel.GetProfileState.Error) onBackClicked()
    }

    SnackbarHandler(
        snackbarState = state.snackbar,
        onDismissSnackbar = state.dismissSnackBar
    )

    val pickMediaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val profile = (state.profileState as? ProfileViewModel.GetProfileState.Success)?.profile
        if (uri != null && profile != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, flag)
            state.onProfileChanged(profile.copy(avatar = uri.toString()))
        }
    }

    fun launchMediaPicker() {
        pickMediaLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    ScreenContent(
        state = state,
        onAvatarClicked = ::launchMediaPicker,
        modifier = modifier
    )
}

@Composable
private fun ScreenContent(
    state: ProfileViewModel.State,
    modifier: Modifier = Modifier,
    onAvatarClicked: () -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CustomTopAppBar(
            stringResource(id = R.string.profile),
            modifier = Modifier.fillMaxWidth(),
        )
        if (state.profileState is ProfileViewModel.GetProfileState.Success) {
            Profile(
                state,
                onAvatarClicked,
            )
        } else {
            Skeleton()
        }
    }
}

@Composable
private fun Profile(
    state: ProfileViewModel.State,
    onAvatarClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .padding(horizontal = PaddingNormal)
            .verticalScroll(rememberScrollState()),
    ) {
        (state.profileState as? ProfileViewModel.GetProfileState.Success)?.profile?.run {
            Spacer(Modifier.height(PaddingDefault))
            ProfileHeader(
                username = username,
                email = email,
                avatar = avatar,
                onAvatarClicked = onAvatarClicked,
            )
            ProfileData(
                state,
                Modifier.padding(top = PaddingDefault)
            )
            SecondaryButton(
                stringResource(R.string.logout),
                loading = false,
                Modifier.padding(top = PaddingNormal),
                onClick = { state.onLogoutClicked() }
            )
            Spacer(Modifier.height(PaddingNormal))
        }
    }
}

@Composable
private fun ProfileHeader(
    username: String,
    email: String,
    avatar: String?,
    onAvatarClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier,
        shape = ShapeDefaults.Small,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(PaddingDefault),
            horizontalArrangement = Arrangement.spacedBy(PaddingDefault),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                avatar,
                Modifier
                    .size(80.dp)
                    .clickable(onClick = onAvatarClicked),
                true
            )
            Column(Modifier, verticalArrangement = Arrangement.spacedBy(PaddingSmall)) {
                Text(username, style = MaterialTheme.typography.headlineMedium)
                Text(email)
            }
        }
    }
}

@Composable
private fun ProfileData(
    state: ProfileViewModel.State,
    modifier: Modifier = Modifier,
) {
    (state.profileState as? ProfileViewModel.GetProfileState.Success)?.run {
        ElevatedCard(
            modifier,
            shape = ShapeDefaults.Small,
        ) {
            Column(
                Modifier.padding(PaddingDefault),
                verticalArrangement = Arrangement.spacedBy(PaddingDefault)
            ) {
                val focusManager = LocalFocusManager.current

                CompositionLocalProvider(
                    LocalTextInputService provides null,
                ) {
                    NameField(profile, state.onProfileChanged, focusManager)

                    BirthDateField(profile, state.onProfileChanged, focusManager)

                    CountryField(
                        profile,
                        state.countries,
                        state.onCountryChanged,
                        state.onProfileChanged,
                        focusManager,
                    )
                }
            }
        }
    }
}

@Composable
private fun NameField(
    profile: Profile,
    onProfileChanged: (Profile) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
) {
    var isDialogShown by remember { mutableStateOf(false) }
    val fullName = profile.fullName()

    TextField(
        value = fullName,
        onValueChange = { /* NOOP*/ },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { if (it.isFocused) isDialogShown = true },
        label = { Text(stringResource(R.string.full_name_title)) },
        singleLine = true,
        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
    )

    if (isDialogShown) {
        focusManager.clearFocus(true)

        var fieldValue by remember {
            mutableStateOf(
                TextFieldValue(
                    fullName,
                    TextRange(profile.fullName().length)
                )
            )
        }

        val onNameChanged: (String) -> Unit = { name ->
            val (firstName, lastName) = name
                .split(' ')
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map {
                    it.replaceFirstChar { c ->
                        if (c.isLowerCase()) {
                            c.titlecase(Locale.getDefault())
                        } else {
                            c.toString()
                        }
                    }
                }
                .run { getOrNull(0) to getOrNull(1) }

            onProfileChanged(profile.copy(firstName = firstName, lastName = lastName))
        }

        EditTextDialog(
            value = fieldValue,
            onValueChange = { value ->
                if (value.text.all { it.isLetter() || it.isWhitespace() }) {
                    fieldValue = value
                }
            },
            onDismiss = { name: String? ->
                name?.let { onNameChanged(it) }
                isDialogShown = false
            },
            titleRes = R.string.edit_name_dialog_title,
            labelRes = R.string.full_name_title,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthDateField(
    profile: Profile,
    onProfileChanged: (Profile) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
) {
    var isDialogShown by remember { mutableStateOf(false) }
    val onDismissDialog = { isDialogShown = false }

    TextField(
        value = profile.birthDate?.let { DateUtils.isoDateToLocalDate(it) } ?: "",
        onValueChange = { /* NOOP*/ },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { if (it.isFocused) isDialogShown = true },
        label = { Text(stringResource(R.string.birth_date_title)) },
        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
    )
    if (isDialogShown) {
        focusManager.clearFocus(true)

        val initialDate = profile.birthDate?.let { DateUtils.isoDateToMillis(it) }
            ?: System.currentTimeMillis()
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate)

        DatePickerDialog(
            onDismissRequest = onDismissDialog,
            confirmButton = {
                Button(
                    content = { Text(stringResource(R.string.ok)) },
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = DateUtils.millisToIsoDate(millis)
                            onProfileChanged(profile.copy(birthDate = date))
                        }
                        onDismissDialog()
                    },
                    modifier = Modifier.padding(horizontal = PaddingDefault)
                )
            },
            dismissButton = {
                Button(onClick = onDismissDialog) { Text(stringResource(R.string.cancel)) }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            DatePicker(datePickerState)
        }
    }
}

@Composable
private fun CountryField(
    profile: Profile,
    countries: List<String>,
    onCountryChanged: (String) -> Unit,
    onProfileChanged: (Profile) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
) {
    var isDialogShown by remember { mutableStateOf(false) }

    TextField(
        value = profile.country ?: "",
        onValueChange = { /* NOOP*/ },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { if (it.isFocused) isDialogShown = true },
        label = { Text(stringResource(R.string.country_title)) },
        colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
    )

    if (isDialogShown) {
        focusManager.clearFocus(true)

        CountryPickerDialog(
            country = profile.country,
            countries = countries,
            onCountryChanged = onCountryChanged,
            onDismiss = { newCountry ->
                onProfileChanged(profile.copy(country = newCountry))
                isDialogShown = false
            },
            modifier = Modifier
        )
    }
}

@Composable
private fun Skeleton(modifier: Modifier = Modifier) {
    Column(
        modifier.padding(horizontal = PaddingNormal),
        verticalArrangement = Arrangement.spacedBy(PaddingNormal)
    ) {
        val textFieldModifier = Modifier.skeletonBackground(RoundedCornerShape(8.dp))
        Row(
            Modifier.padding(vertical = PaddingLarge),
            horizontalArrangement = Arrangement.spacedBy(PaddingDefault),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(80.dp)
                    .skeletonBackground(CircleShape)
            )
            Column(Modifier, verticalArrangement = Arrangement.spacedBy(PaddingDefault)) {
                Box(modifier = textFieldModifier.size(140.dp, 24.dp))
                Box(modifier = textFieldModifier.size(180.dp, 18.dp))
            }
        }
        Box(
            modifier = textFieldModifier
                .height(48.dp)
                .fillMaxWidth()
        )
        Box(
            modifier = textFieldModifier
                .height(48.dp)
                .fillMaxWidth()
        )
        Box(
            modifier = textFieldModifier
                .height(48.dp)
                .fillMaxWidth()
        )
    }
}

// region Previews

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ScreenContent(
            ProfileViewModel.State(
                ProfileViewModel.GetProfileState.Success(
                    Profile(
                        id = 1,
                        username = "John",
                        firstName = "John",
                        lastName = "Doe",
                        birthDate = "2023-10-30",
                        email = "email@gmail.com",
                        createdAt = "2023-10-30 06:58:31.108922",
                    )
                ),
                snackbar = SnackBarState(
                    textId = 0,
                    show = false
                )
            ),
            onAvatarClicked = {},
        )
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    AppTheme {
        ScreenContent(
            state = ProfileViewModel.State(profileState = ProfileViewModel.GetProfileState.Loading),
            onAvatarClicked = {},
        )
    }
}

// endregion
