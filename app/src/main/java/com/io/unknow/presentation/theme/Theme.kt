package com.io.unknow.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = SteelGray,
    background = EbonyClay,
    surface = Nevada,
    onPrimary = DarkPink,
    onBackground = MoreDarkPink,
    onSurface = DarkPink
)

        /* OtherDarkColorPalette default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */

@Composable
fun UnknowAppTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
            colors = DarkColorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}