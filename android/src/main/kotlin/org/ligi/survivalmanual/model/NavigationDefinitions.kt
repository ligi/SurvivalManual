package org.ligi.survivalmanual.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.ligi.survivalmanual.R


open class NavigationEntry(val url: String,
                           @StringRes val titleRes: Int,
                           @DrawableRes val iconRes: Int? = null,
                           val isAppendix: Boolean = false,
                           val isListed: Boolean = true)

class NavigationEntryWithId(val id: Int,
                            val entry: NavigationEntry)

val navigationEntryMap = arrayOf(
        NavigationEntry("Introduction", R.string.introduction, R.drawable.ic_info_outline),
        NavigationEntry("Psychology", R.string.psychology, R.drawable.ic_portrait),
        NavigationEntry("Power", R.string.power, R.drawable.ic_power),
        NavigationEntry("Planning", R.string.planning, R.drawable.ic_format_list_numbered),
        NavigationEntry("Kits", R.string.kits, R.drawable.ic_business_center),
        NavigationEntry("Apps", R.string.apps, R.drawable.ic_phone_android),
        NavigationEntry("Medicine", R.string.basic_medicine, R.drawable.ic_healing),
        NavigationEntry("Shelter", R.string.shelter, R.drawable.ic_store),
        NavigationEntry("Water", R.string.water, R.drawable.ic_local_drink),
        NavigationEntry("Fire", R.string.fire, R.drawable.ic_whatshot),
        NavigationEntry("Food", R.string.food, R.drawable.ic_local_dining),
        NavigationEntry("Plants", R.string.plants, R.drawable.ic_local_florist),
        NavigationEntry("10", R.string.poisonous_plants, R.drawable.ic_face_dead),
        NavigationEntry("Animals", R.string.animals, R.drawable.ic_pets),
        NavigationEntry("Tools", R.string.tools, R.drawable.ic_gavel),
        NavigationEntry("Desert", R.string.desert, R.drawable.ic_wb_sunny),
        NavigationEntry("Tropical", R.string.tropical, R.drawable.ic_beach_access),
        NavigationEntry("Cold", R.string.cold, R.drawable.ic_ac_unit),
        NavigationEntry("Sea", R.string.sea, R.drawable.ic_directions_boat),
        NavigationEntry("WaterCrossing", R.string.water_crossing, R.drawable.ic_rowing),
        NavigationEntry("DirectionFinding", R.string.directionfinding, R.drawable.ic_explore),
        NavigationEntry("ManMadeHazards", R.string.man_made_hazards, R.drawable.ic_attach_money),
        NavigationEntry("Self-defense", R.string.self_defense, R.drawable.ic_security),
        NavigationEntry("Signaling", R.string.signaling, R.drawable.ic_flag),
        NavigationEntry("HostileAreas", R.string.hostile_areas, R.drawable.ic_flash_on),
        NavigationEntry("Camouflage", R.string.camouflage, R.drawable.ic_palette),
        NavigationEntry("People", R.string.people, R.drawable.ic_people),
        NavigationEntry("Credits", R.string.credits, R.drawable.ic_star),

        NavigationEntry("MultiTool", R.string.multitool, isAppendix = true),
        NavigationEntry("DangerousArthropods", R.string.insects_and_arachnids, isAppendix = true),
        NavigationEntry("FishAndMollusks", R.string.fish_and_mollusks, isAppendix = true),
        NavigationEntry("RopesAndKnots", R.string.ropes_and_knots, isAppendix = true),
        NavigationEntry("FAQ", R.string.faq, isAppendix = true),
        NavigationEntry("TranslatorNotes", R.string.translator_notes, isListed = false)


).mapIndexed(::NavigationEntryWithId)

val titleResByURLMap = navigationEntryMap.associate { it.entry.url to it.entry.titleRes }
fun getTitleResByURL(url: String) = titleResByURLMap[url.split("#").first()]

