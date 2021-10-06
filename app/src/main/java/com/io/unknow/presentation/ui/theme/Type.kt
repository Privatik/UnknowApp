package com.io.unknow.presentation.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.io.unknow.R

val quicksand = FontFamily(
        Font(R.font.quicksand_light, FontWeight.Light),
        Font(R.font.quicksand_regular, FontWeight.Normal),
        Font(R.font.quicksand_medium, FontWeight.Medium),
        Font(R.font.quicksand_semibold, FontWeight.SemiBold),
        Font(R.font.quicksand_bold, FontWeight.Bold),
)

val Typography = Typography(
        body1 = TextStyle(
                fontFamily = quicksand,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = DarkPink
        ),
        body2 = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = DarkPink
        ),
        h1 = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = White
        ),
        h2 = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = White
        ),
        h3 = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = White
        )
)