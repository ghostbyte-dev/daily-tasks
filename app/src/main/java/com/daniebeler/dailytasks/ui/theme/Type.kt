package com.daniebeler.dailytasks.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.daniebeler.dailytasks.R

@OptIn(ExperimentalTextApi::class)
val MyVariableFont = FontFamily(
    Font(
        resId = R.font.inter, // Your file name here
        // Set the default weight for the font
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400)
        )
    )
)


val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = MyVariableFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = MyVariableFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

