package org.ligi.survivalmanual

object NavigationDefinitions {

    val menu2htmlMap = mapOf(
            R.id.menu_intro to "01",
            R.id.menu_psycho to "02",
            R.id.menu_panning to "03",
            R.id.menu_meds to "04",
            R.id.menu_shelter to "05",
            R.id.menu_water to "06",
            R.id.menu_fire to "07",
            R.id.menu_food to "08",
            R.id.menu_plants to "09",
            R.id.menu_poisonplants to "10",
            R.id.menu_animals to "11",
            R.id.menu_tools to "12",
            R.id.menu_desert to "13",
            R.id.menu_tropical to "14",
            R.id.menu_cold to "15",
            R.id.menu_sea to "16",
            R.id.menu_watercrossing to "17",
            R.id.menu_directionfinding to "18",
            R.id.menu_signaling to "19",
            R.id.menu_hostile to "20",
            R.id.menu_camouflage to "21",
            R.id.menu_people to "22",
            R.id.menu_man_made_hazards to "23",
            R.id.menu_appendix_kits to "a",
            R.id.menu_appendix_medicin_plants to "b",
            R.id.menu_appendix_poisonplants to "c",
            R.id.menu_appendix_insects to "d",
            R.id.menu_appendix_snakes to "e",
            R.id.menu_appendix_fish to "f",
            R.id.menu_appendix_ropes to "g",
            R.id.menu_appendix_clouds to "h"

    )

    private val html2menu = menu2htmlMap.entries.associate { it.value to it .key }

    fun getMenuResFromURL(url: String) = html2menu[url.split("#").first()]

    fun getTitleResByURL(url: String) = when (url) {
        "01" -> R.string.introduction
        "02" -> R.string.psychology
        "03" -> R.string.planning_and_kits
        "04" -> R.string.basic_medicine
        "05" -> R.string.shelter
        "06" -> R.string.water
        "07" -> R.string.fire
        "08" -> R.string.plants
        "09" -> R.string.food
        "10" -> R.string.poisonous_plants
        "11" -> R.string.tools
        "12" -> R.string.animals
        "13" -> R.string.camouflage
        "14" -> R.string.desert
        "15" -> R.string.tropical
        "16" -> R.string.cold
        "17" -> R.string.sea
        "18" -> R.string.water_crossing
        "19" -> R.string.directionfinding
        "20" -> R.string.signaling
        "21" -> R.string.hostile_areas
        "22" -> R.string.people
        "23" -> R.string.man_made_hazards
        "a" -> R.string.kits
        "b" -> R.string.edible_medicin_plants
        "b_wip" -> R.string.edible_medicin_plants
        "c" -> R.string.poisonous_plants
        "d" -> R.string.insects_and_arachnids
        "e" -> R.string.snakes_and_lizards
        "f" -> R.string.fish_and_mollusks
        "g" -> R.string.ropes_and_knots
        "h" -> R.string.clouds

        else -> null
    }


}
