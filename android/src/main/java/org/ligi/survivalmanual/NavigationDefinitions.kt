package org.ligi.survivalmanual

import io.michaelrocks.bimap.HashBiMap

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
            R.id.menu_appendix_clouds to "h",
            R.id.menu_appendix_ropes to "g",
            R.id.menu_appendix_evasion_plan to "i"
    )

    val html2menu = HashBiMap.create(menu2htmlMap)

    fun getMenuResFromURL(url: String): Int {
        return html2menu.inverse[url.split("/").last().split(".htm").first()]!!
    }

    fun getTitleResById(menuId: Int): Int {

        when (menuId) {
            R.id.menu_intro -> return R.string.introduction
            R.id.menu_psycho -> return R.string.psychology
            R.id.menu_panning -> return R.string.planning_and_kits
            R.id.menu_meds -> return R.string.basic_medicine
            R.id.menu_shelter -> return R.string.shelter
            R.id.menu_water -> return R.string.water
            R.id.menu_fire -> return R.string.fire
            R.id.menu_plants -> return R.string.plants
            R.id.menu_food -> return R.string.food
            R.id.menu_poisonplants -> return R.string.poisonous_plants
            R.id.menu_tools -> return R.string.tools
            R.id.menu_animals -> return R.string.animals
            R.id.menu_camouflage -> return R.string.camouflage
            R.id.menu_desert -> return R.string.desert
            R.id.menu_tropical -> return R.string.tropical
            R.id.menu_cold -> return R.string.cold
            R.id.menu_sea -> return R.string.sea
            R.id.menu_watercrossing -> return R.string.water_crossing
            R.id.menu_directionfinding -> return R.string.directionfinding
            R.id.menu_signaling -> return R.string.signaling
            R.id.menu_hostile -> return R.string.hostile_areas
            R.id.menu_people -> return R.string.people
            R.id.menu_man_made_hazards -> return R.string.man_made_hazards
            R.id.menu_appendix_kits -> return R.string.kits
            R.id.menu_appendix_medicin_plants -> return R.string.edible_medicin_plants
            R.id.menu_appendix_poisonplants -> return R.string.poisonous_plants
            R.id.menu_appendix_insects -> return R.string.insects_and_arachnids
            R.id.menu_appendix_snakes -> return R.string.snakes_and_lizards
            R.id.menu_appendix_fish -> return R.string.fish_and_mollusks
            R.id.menu_appendix_clouds -> return R.string.clouds
            R.id.menu_appendix_ropes -> return R.string.ropes_and_knots
            R.id.menu_appendix_evasion_plan -> return R.string.evasion_plan
        }

        throw IllegalArgumentException("no String for menuId " + menuId +" " +  menu2htmlMap[menuId])
    }

}
