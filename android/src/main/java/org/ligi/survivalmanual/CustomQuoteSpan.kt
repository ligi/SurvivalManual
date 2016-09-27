package org.ligi.survivalmanual

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.text.Layout
import android.text.style.LeadingMarginSpan
import android.text.style.LineBackgroundSpan

/**
 * android.text.style.QuoteSpan hard-codes the strip color and gap. :(
 */
class CustomQuoteSpan(val context: Context) : LeadingMarginSpan, LineBackgroundSpan {

    val size by lazy { context.resources.getDimension(R.dimen.blockquote_width) }

    override fun getLeadingMargin(first: Boolean): Int {
        return (size * 2).toInt()
    }

    override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int, top: Int, baseline: Int, bottom: Int,
                                   text: CharSequence, start: Int, end: Int, first: Boolean, layout: Layout) {
        val style = p.style
        val paintColor = p.color

        p.style = Paint.Style.FILL
        p.color = ContextCompat.getColor(context, R.color.colorPrimaryDark);

        c.drawRect(x.toFloat(), top.toFloat(), x + dir * size, bottom.toFloat(), p)

        p.style = style
        p.color = paintColor
    }

    override fun drawBackground(c: Canvas, p: Paint, left: Int, right: Int, top: Int, baseline: Int, bottom: Int, text: CharSequence, start: Int, end: Int, lnum: Int) {
    }
}