package org.ligi.survivalmanual.adapter

import android.content.Context
import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.QuoteSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.ligi.compat.HtmlCompat
import org.ligi.survivalmanual.R
import org.ligi.survivalmanual.functions.convertMarkdownToHtml
import org.ligi.survivalmanual.functions.getSurvivalDrawable
import org.ligi.survivalmanual.functions.highLight
import org.ligi.survivalmanual.functions.linkImagesInMarkDown
import org.ligi.survivalmanual.model.State
import org.ligi.survivalmanual.ui.CustomQuoteSpan
import org.ligi.survivalmanual.viewholder.TextContentViewHolder
import org.xml.sax.XMLReader

class MarkdownRecyclerAdapter(val list: List<String>,
                              val imageWidth: Int,
                              val onURLClick: (url: String) -> Unit
) : RecyclerView.Adapter<TextContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextContentViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.text, parent, false) as TextView
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setTextIsSelectable(State.allowSelect())
        return TextContentViewHolder(textView)
    }

    override fun onBindViewHolder(holder: TextContentViewHolder, position: Int) {

        val paddingTop = if (position == 0) holder.view.paddingLeft else 0
        holder.view.setPadding(holder.view.paddingLeft, paddingTop, holder.view.paddingRight, 0)

        val html = convertMarkdownToHtml(linkImagesInMarkDown(if (State.searchTerm.isNullOrEmpty()) {
            list[position]
        } else {
            highLight(list[position], State.searchTerm)
        }))

        setTextViewHTML(holder.view, html)
    }

    override fun getItemCount() = list.size

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

        text.textSize = State.getFontSize()
        class CustomImageGetter : Html.ImageGetter {
            override fun getDrawable(source: String) = getSurvivalDrawable(ctx, source)?.apply {
                val ratio = intrinsicHeight.toFloat() / intrinsicWidth.toFloat()
                setBounds(0, 0, imageWidth, (imageWidth * ratio).toInt())
            }
        }

        val sequence = HtmlCompat.fromHtml(html, CustomImageGetter(), ListTagHandler())
        val spannable = SpannableStringBuilder(sequence)
        val urls = spannable.getSpans(0, sequence.length, URLSpan::class.java)

        urls.forEach { makeLinkClickable(spannable, it) }

        replaceQuoteSpans(ctx, spannable)
        text.text = spannable
        text.movementMethod = LinkMovementMethod.getInstance()
    }

    internal inner class ListTagHandler : Html.TagHandler {

        override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
            if (tag.equals("li", ignoreCase = true)) {
                output.append(if (opening) "\u2023 " else "\n")
            }
        }
    }

}