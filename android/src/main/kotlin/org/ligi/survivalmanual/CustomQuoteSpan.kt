package org.ligi.survivalmanual

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.text.Layout
import android.text.style.LeadingMarginSpan

/**
 * android.text.style.QuoteSpan hard-codes the strip color and gap. :(
 */
class CustomQuoteSpan(val context: Context) : LeadingMarginSpan {

    val size by lazy { context.resources.getDimension(R.dimen.blockquote_width) }
    val paint by lazy {
        val result = Paint()
        result.style = Paint.Style.FILL
        result.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        result
    }

    override fun getLeadingMargin(first: Boolean): Int {
        return (size * 2).toInt()
    }

    override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int, top: Int, baseline: Int, bottom: Int,
                                   text: CharSequence, start: Int, end: Int, first: Boolean, layout: Layout) {

        c.drawRect(x.toFloat(), top.toFloat(), x + dir * size, bottom.toFloat(), paint)
    }
}