package org.ligi.survivalmanual.ui

import android.R
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.*
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()

        if (org.ligi.survivalmanual.model.State.nightModeString() == "darknight") {
            window.decorView.rootView.setBackgroundColor(getColor(this, R.color.black))
        }
    }
}
