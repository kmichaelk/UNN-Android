package io.github.kmichaelk.unnandroid.ui.composables

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.Html.TagHandler
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.TextViewCompat
import coil.Coil
import coil.ImageLoader
import coil.request.ImageRequest
import coil.size.Scale
import io.github.kmichaelk.unnandroid.ui.html.ExtendedHtmlTagHandler


// https://stackoverflow.com/questions/77704540/android-compose-how-to-display-html-text-with-image-tag-inside-text

@Composable
fun HtmlText(
    html: String,
    modifier: Modifier = Modifier,
    imageSourceModifier: ((source: String) -> String)? = null,
    htmlTagHandler: TagHandler? = ExtendedHtmlTagHandler(),
    @StyleRes style: Int = android.R.style.TextAppearance_Material_Body1,
    typeface: Typeface? = null,
    onClicked: ((String) -> Unit)? = null
) {
    // This is used to determine the bounds of Drawable inside htmlText
    // which is handling by CoilImageGetter.
    var componentWidth by remember { mutableIntStateOf(0) }

    AndroidView(
        factory = {
            TextView(it)
        },
        modifier
            .onGloballyPositioned {
                componentWidth = it.size.width
            }
            .testTag("html-text"),
        update = { textView ->
            textView.text = Html.fromHtml(
                html,
                Html.FROM_HTML_MODE_LEGACY,
                CoilImageGetter(
                    textView,
                    sourceModifier = imageSourceModifier,
                    fixedImageWidth = componentWidth
                ),
                htmlTagHandler,
            )

            textView.gravity = Gravity.START
            textView.setTextIsSelectable(true)
            TextViewCompat.setTextAppearance(textView, style)
            textView.typeface = typeface
            textView.handleUrlClicks(onClicked)

            val theme = textView.context.theme
            val value = TypedValue()

            theme.resolveAttribute(android.R.attr.textColor, value, true)
            textView.setTextColor(value.data)

            theme.resolveAttribute(android.R.attr.colorAccent, value, true)
            textView.setLinkTextColor(value.data)
        },
    )
}

private fun TextView.handleUrlClicks(onClicked: ((String) -> Unit)? = null) {
    linksClickable = true
    text = SpannableStringBuilder.valueOf(text).apply {
        getSpans(0, length, URLSpan::class.java).forEach {
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onClicked?.invoke(it.url.trim())
                    }
                },
                getSpanStart(it),
                getSpanEnd(it),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            removeSpan(it)
        }
    }
    movementMethod = LinkMovementMethod.getInstance()
}

class CoilImageGetter(
    private val textView: TextView,
    private val imageLoader: ImageLoader = Coil.imageLoader(textView.context),
    private val sourceModifier: ((source: String) -> String)? = null,
    private val fixedImageWidth: Int,
) : Html.ImageGetter {

    override fun getDrawable(source: String): Drawable {
        val finalSource = sourceModifier?.invoke(source) ?: source
        val placeholder = ColorDrawable(Color.LTGRAY).toBitmap(32, 32)
        val drawablePlaceholder = DrawablePlaceHolder(textView.resources, placeholder)
        imageLoader.enqueue(
            ImageRequest.Builder(textView.context).data(finalSource).apply {
                target { drawable ->
                    drawablePlaceholder.updateDrawable(48, drawable)
                    // invalidating the drawable doesn't seem to be enough...
                    textView.text = textView.text
                }
                scale(Scale.FIT)
            }.build()
        )
        // Since this loads async, we return a "blank" drawable, which we update later
        return drawablePlaceholder
    }

    private class DrawablePlaceHolder(
        resource: Resources,
        bitmap: Bitmap,
    ) : BitmapDrawable(resource, bitmap) {

        private var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.draw(canvas)
        }

        fun updateDrawable(width: Int, drawable: Drawable) {
            this.drawable = drawable
            val aspectRatio = drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth.toFloat()
            val height = (width.toFloat() * aspectRatio).toInt()
            drawable.setBounds(0, 0, width, height)
            setBounds(0, 0, width, height)
        }
    }
}