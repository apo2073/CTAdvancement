package kr.apo2073.customToasts.utilities

import org.bukkit.inventory.ItemStack

data class Reward(
    private val function:String?,
    private val loots:List<String>?,
    private val recipes: List<ItemStack>?,
    private val experience: Int?
) {
    fun getFunction():String? = function
    fun getLoots():List<String>?= loots
    fun getRecipes():List<ItemStack>?=recipes
    fun getExp():Int?=experience
}
