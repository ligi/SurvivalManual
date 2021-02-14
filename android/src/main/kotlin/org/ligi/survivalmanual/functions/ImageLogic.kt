package org.ligi.survivalmanual.functions

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import org.ligi.survivalmanual.R
import timber.log.Timber
import java.io.FileNotFoundException

fun linkImagesInMarkDown(markdown: String): String {
    return markdown.replace(Regex("!\\[([^\\]]*)\\]\\(([^)]+)\\)")) { matchResult: MatchResult ->
        "[" + matchResult.value + "](" + matchResult.groupValues[2] + ")"
    }
}

fun isImage(fileName: String) = fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".vd")

fun getSurvivalDrawable(ctx: Context, source: String) = if (source.endsWith(".vd")) {
    when (source.substringBeforeLast(".")) {
        "med_open_airway" -> VectorDrawableCompat.create(ctx.resources, R.drawable.ic_med_open_airway, null)
        else -> throw IllegalArgumentException("Illegal vector drawable")
    }
} else try {
    BitmapDrawable.createFromStream(ctx.assets.open("md/$source"), source) as BitmapDrawable
} catch (e: FileNotFoundException) {
    Timber.e("Could not find md/$source")
    null
}