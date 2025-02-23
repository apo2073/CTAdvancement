package kr.apo2073.ctadvancement.utilities

import org.bukkit.inventory.ItemStack

data class Reward(
    private val function:String?=null,
    private val loots:List<String>?=null,
    private val recipes: List<ItemStack?>?=null,
    private val experience: Int?=null
) {
    fun getFunction():String? = function
    fun getLoots():List<String>?= loots
    fun getRecipes():List<ItemStack?>?=recipes
    fun getExp():Int?=experience
}
