package ru.tech.imageresizershrinker.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp


val md_theme_light_primary = Color(0xFF3D6A00)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFAAF855)
val md_theme_light_onPrimaryContainer = Color(0xFF0F2000)
val md_theme_light_secondary = Color(0xFF57624A)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFDBE7C8)
val md_theme_light_onSecondaryContainer = Color(0xFF151E0B)
val md_theme_light_tertiary = Color(0xFF386663)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFBBECE8)
val md_theme_light_onTertiaryContainer = Color(0xFF00201F)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFDFCF5)
val md_theme_light_onBackground = Color(0xFF1B1C18)
val md_theme_light_surface = Color(0xFFFDFCF5)
val md_theme_light_onSurface = Color(0xFF1B1C18)
val md_theme_light_surfaceVariant = Color(0xFFE1E4D5)
val md_theme_light_onSurfaceVariant = Color(0xFF44483D)
val md_theme_light_outline = Color(0xFF75796C)
val md_theme_light_inverseOnSurface = Color(0xFFF2F1EA)
val md_theme_light_inverseSurface = Color(0xFF30312C)
val md_theme_light_inversePrimary = Color(0xFF8FDB3A)
val md_theme_light_surfaceTint = Color(0xFF3D6A00)
val md_theme_light_outlineVariant = Color(0xFFC4C8BA)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFF8FDB3A)
val md_theme_dark_onPrimary = Color(0xFF1D3700)
val md_theme_dark_primaryContainer = Color(0xFF2C5000)
val md_theme_dark_onPrimaryContainer = Color(0xFFAAF855)
val md_theme_dark_secondary = Color(0xFFBFCBAD)
val md_theme_dark_onSecondary = Color(0xFF2A331F)
val md_theme_dark_secondaryContainer = Color(0xFF404A33)
val md_theme_dark_onSecondaryContainer = Color(0xFFDBE7C8)
val md_theme_dark_tertiary = Color(0xFFA0CFCC)
val md_theme_dark_onTertiary = Color(0xFF003735)
val md_theme_dark_tertiaryContainer = Color(0xFF1F4E4C)
val md_theme_dark_onTertiaryContainer = Color(0xFFBBECE8)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF1B1C18)
val md_theme_dark_onBackground = Color(0xFFE3E3DB)
val md_theme_dark_surface = Color(0xFF1B1C18)
val md_theme_dark_onSurface = Color(0xFFE3E3DB)
val md_theme_dark_surfaceVariant = Color(0xFF44483D)
val md_theme_dark_onSurfaceVariant = Color(0xFFC4C8BA)
val md_theme_dark_outline = Color(0xFF8E9285)
val md_theme_dark_inverseOnSurface = Color(0xFF1B1C18)
val md_theme_dark_inverseSurface = Color(0xFFE3E3DB)
val md_theme_dark_inversePrimary = Color(0xFF3D6A00)
val md_theme_dark_surfaceTint = Color(0xFF8FDB3A)
val md_theme_dark_outlineVariant = Color(0xFF44483D)
val md_theme_dark_scrim = Color(0xFF000000)

fun ColorScheme.outlineVariant(
    luminance: Float = 0.3f,
    onTopOf: Color = surfaceColorAtElevation(3.dp)
) = onSecondaryContainer
    .copy(alpha = luminance)
    .compositeOver(onTopOf)


fun ColorScheme.suggestContainerColorBy(color: Color) = when (color) {
    onPrimary -> primary
    onSecondary -> secondary
    onTertiary -> tertiary
    onPrimaryContainer -> primaryContainer
    onSecondaryContainer -> secondaryContainer
    onTertiaryContainer -> tertiaryContainer
    onError -> error
    onErrorContainer -> errorContainer
    onSurfaceVariant -> surfaceVariant
    inverseOnSurface -> inverseSurface
    else -> surface
}