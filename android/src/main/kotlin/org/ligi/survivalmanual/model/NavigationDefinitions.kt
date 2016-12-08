package org.ligi.survivalmanual.model

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import org.ligi.survivalmanual.R

object NavigationDefinitions {

    open class MenuEntry(val url: String,
                         @StringRes val titleRes: Int,
                         @DrawableRes val iconRes: Int? = null,
                         val isAppendix: Boolean = false,
                         val isListed: Boolean = true)

    class MenuEntryWithId(val id: Int,
                          val entry: MenuEntry)

    val content = arrayOf(
            MenuEntry("02", R.string.psychology, R.drawable.ic_image_portrait),
            MenuEntry("03", R.string.planning_and_kits, R.drawable.ic_editor_format_list_numbered),
            MenuEntry("04", R.string.basic_medicine, R.drawable.ic_image_healing),
            MenuEntry("05", R.string.shelter, R.drawable.ic_action_store),
            MenuEntry("06", R.string.water, R.drawable.ic_maps_local_drink),
            MenuEntry("07", R.string.fire, R.drawable.ic_social_whatshot),
            MenuEntry("08", R.string.food, R.drawable.ic_maps_restaurant_menu),
            MenuEntry("09", R.string.plants, R.drawable.ic_maps_local_florist),
            MenuEntry("10", R.string.poisonous_plants, R.drawable.ic_social_sentiment_very_dissatisfied),
            MenuEntry("11", R.string.animals, R.drawable.ic_action_pets),
            MenuEntry("12", R.string.tools, R.drawable.ic_action_gavel),
            MenuEntry("13", R.string.desert, R.drawable.ic_image_wb_sunny),
            MenuEntry("14", R.string.tropical, R.drawable.ic_places_beach_access),
            MenuEntry("15", R.string.cold, R.drawable.ic_places_ac_unit),
            MenuEntry("16", R.string.sea, R.drawable.ic_maps_directions_boat),
            MenuEntry("17", R.string.water_crossing, R.drawable.ic_action_rowing),
            MenuEntry("18", R.string.directionfinding, R.drawable.ic_action_explore),
            MenuEntry("19", R.string.signaling, R.drawable.ic_content_flag),
            MenuEntry("20", R.string.hostile_areas, R.drawable.ic_image_flash_on),
            MenuEntry("21", R.string.camouflage, R.drawable.ic_image_color_lens),
            MenuEntry("22", R.string.people, R.drawable.ic_social_people),
            MenuEntry("23", R.string.man_made_hazards, R.drawable.ic_editor_attach_money),

            MenuEntry("a", R.string.kits, isAppendix = true),
            MenuEntry("b", R.string.edible_medicin_plants, isAppendix = true),
            MenuEntry("b_wip", R.string.edible_medicin_plants, isListed = false),
            MenuEntry("c", R.string.poisonous_plants, isAppendix = true),
            MenuEntry("d", R.string.insects_and_arachnids, isAppendix = true),
            MenuEntry("e", R.string.snakes_and_lizards, isAppendix = true),
            MenuEntry("f", R.string.fish_and_mollusks, isAppendix = true),
            MenuEntry("g", R.string.ropes_and_knots, isAppendix = true),
            MenuEntry("h", R.string.clouds, isAppendix = true)
    ).mapIndexed(::MenuEntryWithId)

    val titleResByURLMap = content.associate { it.entry.url to it.entry.titleRes }
    fun getTitleResByURL(url: String) = titleResByURLMap[url.split("#").first()]
}
