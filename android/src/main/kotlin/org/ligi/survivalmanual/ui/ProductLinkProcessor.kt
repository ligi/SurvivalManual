package org.ligi.survivalmanual.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import org.ligi.kaxt.inflate
import org.ligi.kaxt.startActivityFromURL
import org.ligi.survivalmanual.EventTracker
import org.ligi.survivalmanual.R

val PRODUCT_MAP = mapOf(
        "SolarUSBCharger" to "B012YUJJM8/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B012YUJJM8&linkCode=as2&tag=ligi-20&linkId=02d3fbda3eaadbd10744c42805e0e791",
        "CampStoveUSB" to "B00BQHET9O/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B00BQHET9O&linkCode=as2&tag=ligi-20&linkId=d949a1aca04d67b5e61d3bf77ce89d22",
        "HandCrankUSB" to "B01AD7IN4O/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B01AD7IN4O&linkCode=as2&tag=ligi-20&linkId=9a9c9d7ff318d594d077fa917f8c3739",
        "CarUSBCharger" to "B00VH84L5E/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B00VH84L5E&linkCode=as2&tag=ligi-20&linkId=41a56b9c800ed019a0af367a49050502",
        "OHTMultiTool" to "B008P8EYWE/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B008P8EYWE&linkCode=as2&tag=ligi-20&linkId=e72720183453da1b74c2a0413521b194",
        "LifeStraw" to "B006QF3TW4/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B006QF3TW4&linkCode=as2&tag=ligi-20&linkId=5b949343cb5a5f03b220651ad6830a9d",
        "TreadMultiTool" to "B018IOXXP8/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B018IOXXP8&linkCode=as2&tag=ligi-20&linkId=1c14c69653606e308b9f4b98372a5a51",
        "PandaDubLionsDen" to "B00F48QJUS/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B00F48QJUS&linkCode=as2&tag=ligi-20&linkId=3e9b42f0dee5e03da96ea1f14be223ea"
)

fun processProductLinks(it: String, activity: Activity): Boolean {

    if (PRODUCT_MAP.containsKey(it)) {
        EventTracker.trackGeneric("product", "click")
        val url = "https://www.amazon.com/gp/product/" + PRODUCT_MAP[it]
        val view = activity.inflate(R.layout.alert_product_link)

        AlertDialog.Builder(activity)
                .setTitle("Product Link")
                .setView(view)
                .setNegativeButton("cancel", { dialogInterface: DialogInterface, i: Int ->
                    EventTracker.trackGeneric("product", "cancel")
                })
                .setPositiveButton("to amazon", { dialogInterface: DialogInterface, i: Int ->
                    EventTracker.trackGeneric("product", "go")
                    activity.startActivityFromURL(url)
                })
                .setNeutralButton("send link", { dialogInterface: DialogInterface, i: Int ->
                    EventTracker.trackGeneric("product", "send")
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, url)
                    sendIntent.type = "text/plain"
                    activity.startActivity(Intent.createChooser(sendIntent, "Send link to"))
                })
                .show()
        return true
    } else {
        return false
    }
}