package com.io.unknow.presentation.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = SteelGray,
    background = EbonyClay,
    surface = Nevada,
    onPrimary = DarkPink,
    onBackground = MoreDarkPink,
    onSurface = MoreNevada,
    onSecondary = Color.White
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