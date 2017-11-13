package org.ligi.survivalmanual.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.text.Layout
import android.text.style.LeadingMarginSpan
import org.ligi.survivalmanual.R

/**
 * android.text.style.QuoteSpan hard-codes the strip color and gap. :(
 */
class CustomQuoteSpan(val context: Context) : LeadingMarginSpan {

    val size by lazy { context.resources.getDimension(R.dimen.blockquote_width) }
    private val paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.colorAccent)
        }
    }

    override fun getLeadingMargin(first: Boolean) = (size * 2).toInt()

    override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int, top: Int, baseline: Int, bottom: Int,
                                   text: CharSequence, start: Int, end: Int, first: Boolean, layout: Layout) {

        c.drawRect(x.toFloat(), top.toFloat(), x + dir * size, bottom.toFloat(), paint)
    }
}