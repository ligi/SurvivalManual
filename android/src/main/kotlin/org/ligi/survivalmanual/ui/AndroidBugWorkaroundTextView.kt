package org.ligi.survivalmanual.ui

import android.content.Context
import android.text.Selection
import android.text.Spannable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView

class AndroidBugWorkaroundTextView(context: Context, attrs: AttributeSet) : TextView(context, attrs) {

    // workaround for https://github.com/ligi/SurvivalManual/issues/22
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (selectionStart < 0 || selectionEnd < 0)
            Selection.setSelection(text as Spannable, text.length)
        else if (selectionStart != selectionEnd && event.actionMasked == MotionEvent.ACTION_DOWN) {
            val text = text
            setText(null)
            setText(text)
        }

        return super.dispatchTouchEvent(event)
    }

}
