package org.ligi.survivalmanual

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.ortiz.touch.TouchImageView

class ImageViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val touchImageView = TouchImageView(this)
        setContentView(touchImageView)

        val bitmap = BitmapFactory.decodeStream(assets.open("md/"+intent.extras["URL"]))
        touchImageView.setImageBitmap(bitmap)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)

    }
}
