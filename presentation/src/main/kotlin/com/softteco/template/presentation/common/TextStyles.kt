package com.softteco.template.presentation.common

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object TextStyles {
    val headlineLarge = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
    )

    val headlineMedium = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    )

    val overlineLarge = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    )

    val screenTitle = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 22.sp,
    )

    val headlineSmall = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    )

    val titleMedium = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    )

    val titleSmall = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )

    val bodyLarge = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    )
    val bodyMedium = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    )

    val bodySmall = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )

    val buttonPrimary = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )

    val buttonSecondary = TextStyle(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )
}
