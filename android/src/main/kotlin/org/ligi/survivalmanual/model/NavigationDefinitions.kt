package org.ligi.survivalmanual.model

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import org.ligi.survivalmanual.R


open class NavigationEntry(val url: String,
                           @StringRes val titleRes: Int,
                           @DrawableRes val iconRes: Int? = null,
                           val isAppendix: Boolean = false,
                           val isListed: Boolean = true)

class NavigationEntryWithId(val id: Int,
                            val entry: NavigationEntry)

val NavigationEntryMap = arrayOf(
        NavigationEntry("Introduction", R.string.introduction, R.drawable.ic_action_info_outline),
        NavigationEntry("02", R.string.psychology, R.drawable.ic_image_portrait),
        NavigationEntry("Power", R.string.power, R.drawable.ic_notification_power),
        NavigationEntry("Planning", R.string.planning, R.drawable.ic_editor_format_list_numbered),
        NavigationEntry("Kits", R.string.kits, R.drawable.ic_places_business_center),
        NavigationEntry("Apps", R.string.apps, R.drawable.ic_hardware_phone_android),
        NavigationEntry("04", R.string.basic_medicine, R.drawable.ic_image_healing),
        NavigationEntry("05", R.string.shelter, R.drawable.ic_action_store),
        NavigationEntry("06", R.string.water, R.drawable.ic_maps_local_drink),
        NavigationEntry("07", R.string.fire, R.drawable.ic_social_whatshot),
        NavigationEntry("08", R.string.food, R.drawable.ic_maps_restaurant_menu),
        NavigationEntry("09", R.string.plants, R.drawable.ic_maps_local_florist),
        NavigationEntry("10", R.string.poisonous_plants, R.drawable.ic_social_sentiment_very_dissatisfied),
        NavigationEntry("11", R.string.animals, R.drawable.ic_action_pets),
        NavigationEntry("12", R.string.tools, R.drawable.ic_action_gavel),
        NavigationEntry("13", R.string.desert, R.drawable.ic_image_wb_sunny),
        NavigationEntry("14", R.string.tropical, R.drawable.ic_places_beach_access),
        NavigationEntry("15", R.string.cold, R.drawable.ic_places_ac_unit),
        NavigationEntry("16", R.string.sea, R.drawable.ic_maps_directions_boat),
        NavigationEntry("17", R.string.water_crossing, R.drawable.ic_action_rowing),
        NavigationEntry("18", R.string.directionfinding, R.drawable.ic_action_explore),
        NavigationEntry("19", R.string.signaling, R.drawable.ic_content_flag),
        NavigationEntry("20", R.string.hostile_areas, R.drawable.ic_image_flash_on),
        NavigationEntry("21", R.string.camouflage, R.drawable.ic_image_color_lens),
        NavigationEntry("22", R.string.people, R.drawable.ic_social_people),
        NavigationEntry("23", R.string.man_made_hazards, R.drawable.ic_editor_attach_money),

        NavigationEntry("MultiTool", R.string.multitool, isAppendix = true),
        NavigationEntry("b", R.string.edible_medicin_plants, isAppendix = true),
        NavigationEntry("b_wip", R.string.edible_medicin_plants, isListed = false),

        NavigationEntry("c", R.string.poisonous_plants, isAppendix = true),
        NavigationEntry("d", R.string.insects_and_arachnids, isAppendix = true),
        NavigationEntry("e", R.string.snakes_and_lizards, isAppendix = true),
        NavigationEntry("f", R.string.fish_and_mollusks, isAppendix = true),
        NavigationEntry("g", R.string.ropes_and_knots, isAppendix = true),
        NavigationEntry("h", R.string.clouds, isAppendix = true)
).mapIndexed(::NavigationEntryWithId)

val titleResByURLMap = NavigationEntryMap.associate { it.entry.url to it.entry.titleRes }
fun getTitleResByURL(url: String) = titleResByURLMap[url.split("#").first()]

