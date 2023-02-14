package com.berkkanb.composemarkdown

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import com.berkkanb.composemarkdown.ui.theme.ComposeMarkdownTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val text = "Bu bir **bold yazı** denemesidir bu da **bold** olsun *bu olmasın* tamamdır"

            ComposeMarkdownTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MarkdownText(text)
                }
            }
        }
    }
}
@Composable
fun MarkdownText(
    text:String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    color: Color = Color.Unspecified,
    background: Color = Color.Unspecified,
    textDecoration: TextDecoration? = null

    ){
    val mrkText = MarkdownHandler().createTransformedText(text)
    var lastIndex = 0
    val spanStyle = SpanStyle(fontSize = fontSize, fontFamily = fontFamily, letterSpacing = letterSpacing, color = color, background = background, textDecoration = textDecoration)

    val annotatedString = buildAnnotatedString {
        mrkText.forEach {
            val tokenLength = it.tokenTag.tokenCharLength
            val tokenSpanStyle = getSpanStyle(it.tokenTag,fontFamily,fontSize,letterSpacing,color,background,textDecoration)
            withStyle(spanStyle){
                append(text.substring(lastIndex,it.start))
            }
            withStyle(tokenSpanStyle){
                append(text.substring(it.start+tokenLength,it.end+1-tokenLength))
            }
            lastIndex = it.end+1
        }
        withStyle(spanStyle){
            append(text.substring(lastIndex))
        }
    }
    Text(text = annotatedString, modifier = modifier)
}

fun getSpanStyle(
    tokenTag:MarkdownTag,
    fontFamily: FontFamily?,
    fontSize: TextUnit,
    letterSpacing: TextUnit,
    color: Color,
    background: Color,
    textDecoration: TextDecoration?
) : SpanStyle{
    when(tokenTag) {
        MarkdownTag.BOLD -> {
            return SpanStyle(
                fontWeight = FontWeight.ExtraBold,
                fontFamily = fontFamily,
                fontSize = fontSize,
                letterSpacing = letterSpacing,
                color = color,
                background = background,
                textDecoration = textDecoration
            )
        }
    }
}