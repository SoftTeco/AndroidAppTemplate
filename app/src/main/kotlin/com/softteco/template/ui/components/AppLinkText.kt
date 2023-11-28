package com.softteco.template.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

@Composable
fun AppLinkText(
    text: String,
    linkText: String,
    linkUrl: String,
    openLink: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onSurface
            )
        ) {
            append(text)
        }
        pushStringAnnotation(tag = linkUrl, annotation = linkUrl)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(linkText)
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        modifier = modifier,
        style = TextStyle(textAlign = TextAlign.Center),
        onClick = { offset ->
            annotatedText.getStringAnnotations(offset, offset)
                .firstOrNull()?.let {
                    openLink(it.item)
                }
        }
    )
}
