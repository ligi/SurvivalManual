package org.ligi.survivalmanual

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.LayoutParams
import android.support.v7.widget.RecyclerView.LayoutParams.WRAP_CONTENT
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.QuoteSpan
import android.text.style.URLSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.rjeschke.txtmark.Processor
import org.ligi.compat.HtmlCompat
import java.io.InputStream

class MarkdownRecyclerAdapter(val text: InputStream, val imageWidth: Int, val onURLClick: (url: String) -> Unit) : RecyclerView.Adapter<TextContentViewHolder>() {

    val list = TextSplitter.split(text)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextContentViewHolder {

        val layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        val padding = parent.context.resources.getDimension(R.dimen.content_padding).toInt()
        layoutParams.setMargins(padding, 0, padding, 0)

        val textView = TextView(parent.context)
        textView.layoutParams = layoutParams
        textView.movementMethod = LinkMovementMethod.getInstance()
        return TextContentViewHolder(textView)
    }

    override fun onBindViewHolder(holder: TextContentViewHolder, position: Int) {
        setTextViewHTML(holder.itemView as TextView, Processor.process(list[position]))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                onURLClick(span.url)
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

    private fun replaceQuoteSpans(ctx: Context, spannable: Spannable) {
        val quoteSpans = spannable.getSpans(0, spannable.length, QuoteSpan::class.java)
        for (quoteSpan in quoteSpans) {
            val start = spannable.getSpanStart(quoteSpan)
            val end = spannable.getSpanEnd(quoteSpan)
            val flags = spannable.getSpanFlags(quoteSpan)
            spannable.removeSpan(quoteSpan)
            spannable.setSpan(CustomQuoteSpan(ctx), start, end, flags)
        }
    }

    private fun setTextViewHTML(text: TextView, html: String, ctx: Context = text.context) {
        class CustomImageGetter : Html.ImageGetter {
            override fun getDrawable(source: String?): Drawable {

                val bitmapDrawable = BitmapDrawable.createFromStream(ctx.assets.open("md/" + source), source) as BitmapDrawable

                val ratio = bitmapDrawable.bitmap.height.toFloat() / bitmapDrawable.bitmap.width

                bitmapDrawable.setBounds(0, 0, imageWidth, (imageWidth * ratio).toInt())
                return bitmapDrawable
            }

        }

        val sequence = HtmlCompat.fromHtml(html, CustomImageGetter(), null)
        val spannable = SpannableStringBuilder(sequence)
        val urls = spannable.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {

            if (NavigationDefinitions.getMenuResFromURL(span.url) != null) {
                makeLinkClickable(spannable, span)
            } else {
                if (!span.url.startsWith("#")) {
                    throw(Exception("Err cannot handle " + span.url))
                }
            }
        }

        replaceQuoteSpans(ctx, spannable)
        text.text = spannable
        text.movementMethod = LinkMovementMethod.getInstance()
    }

}