package org.ligi.survivalmanual.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.ortiz.touch.TouchImageView
import org.ligi.survivalmanual.functions.getSurvivalDrawable


class ImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val touchImageView = TouchImageView(this)
        setContentView(touchImageView)

        val bitmap = getSurvivalDrawable(this,intent.getStringExtra("URL"))
        touchImageView.setImageDrawable(bitmap)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)

    }
}
