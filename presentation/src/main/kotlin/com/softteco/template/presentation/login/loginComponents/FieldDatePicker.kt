package com.softteco.template.presentation.login.loginComponents

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.softteco.template.presentation.R
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FieldDatePicker(
    fieldNameErrorState: MutableState<Boolean>,
    fieldNameStr: Int
) {

    val mContext = LocalContext.current

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDate = remember { mutableStateOf("") }

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )
    val source = remember {
        MutableInteractionSource()
    }

    OutlinedTextField(
        value = mDate.value,
        onValueChange = {
            if (fieldNameErrorState.value) {
                fieldNameErrorState.value = false
            }
            mDate.value = it

        },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        isError = fieldNameErrorState.value,
        label = {
            Text(text = stringResource(id = fieldNameStr))
        },
        interactionSource = source
    )
    if (fieldNameErrorState.value) {
        Text(text = stringResource(id = R.string.required), color = Color.Red)
    }
    if (source.collectIsPressedAsState().value) {
        mDatePickerDialog.show()

    }
}


