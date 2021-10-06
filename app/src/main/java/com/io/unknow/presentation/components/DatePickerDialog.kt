package com.io.unknow.presentation.components

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.io.unknow.R
import com.io.unknow.util.extends.getDateString
import java.util.*

fun DatePickerDialog(
    context: Context,
    onChangeDate: (String) -> Unit
){
    val currentDateTime = Calendar.getInstance()
    val startYear = currentDateTime.get(Calendar.YEAR)
    val startMonth = currentDateTime.get(Calendar.MONTH)
    val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, R.style.DialogTheme, { _, year, month, day ->
        onChangeDate("${day.getDateString()}/${month.getDateString()}/$year")
    }, startYear, startMonth, startDay).show()
}