package kr.apo2073.ctadvancement.toasts

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kr.apo2073.ctadvancement.enums.Frame
import kr.apo2073.ctadvancement.enums.Trigger
import kr.apo2073.ctadvancement.toasts.setting.Criteria
import kr.apo2073.ctadvancement.toasts.setting.Icons
import kr.apo2073.ctadvancement.toasts.setting.Reward
import kr.apo2073.ctadvancement.utilities.VersionManager
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

// https://misode.github.io/advancement/
/**
 * @param plugin Your Own Plugin
 * @param name The name of toast
 * @param icon Item to set Icon
 * @param title Advancement title
 * @param description Description of advancement
 * @param frame Frame of advancement
 * @param showToasts Is show advancement clear alert
 * @param announceToChat Is show advancement clear
 * @param hidden Is the advancement hidden
 * @param parent Parent of advancement
 * @param requirements Advancement Requirements to grant it
 * @param rewards Rewards of advancement
 * @param background Background of advancement
 *
 * @return Custom Toasts
 * */
class ToastBuilder(
    internal val plugin:JavaPlugin,
    internal val name: String,
    private val icon: Icons,
    private val title: String,
    private val description: String,
    private val frame:Frame,
    private var showToasts:Boolean=true,
    private var announceToChat:Boolean=true,
    private var hidden:Boolean=false,
    private var parent:String?=null,
    internal var requirements: MutableList<Criteria>?=null,
    private var rewards: MutableList<Reward>?=null,
    private var background:String?="minecraft:textures/gui/advancements/backgrounds/adventure.png",
    private var sendsTelemetryEvent:Boolean?=false
) {
    var key=NamespacedKey(plugin, name)
        private set
    fun build():Toasts {
//        println(toastJson())
        try {
            plugin.server.unsafe.loadAdvancement(key, toastJson())
        } catch (e:IllegalArgumentException) {
            plugin.server.unsafe.removeAdvancement(key)
            Bukkit.reloadData()
            plugin.server.unsafe.loadAdvancement(key, toastJson())
            return Toasts(this)
        }
        return Toasts(this)
    }

    private fun toastJson():String {
        if (background==null) background="minecraft:textures/gui/advancements/backgrounds/adventure.png"

        val json=JsonObject()

        val display=JsonObject()
        println(VersionManager.isV1_21())
        display.add("icon", icon.generate())
        display.addProperty("title", title)
        display.addProperty("description", description)
        display.addProperty("background", background)
        display.addProperty("frame", frame.toString().lowercase())
        display.addProperty("show_toast", showToasts)
        display.addProperty("announce_to_chat", announceToChat)
        display.addProperty("hidden", hidden)

        json.add("display", display)

        if (parent!=null) json.addProperty("parent", parent)

        if (requirements==null) requirements= arrayListOf(
            Criteria("empties", Trigger.IMPOSSIBLE, null)
        )
        val requireArray = JsonArray()
        val criteriaObject = JsonObject()
        requirements?.forEach {
            criteriaObject.add(it.getName(), it.getJson())
            requireArray.add(it.getName())
        }
        json.add("criteria", criteriaObject)
        json.add("requirements", JsonArray().apply { add(requireArray) })

        val reward = JsonObject()
        rewards?.forEach { it ->
            it.getFunction()?.let { reward.addProperty("function", it) }
            val lootArray = JsonArray()
            it.getLoots()?.forEach { lootArray.add(it) }
            if (lootArray.size() > 0) reward.add("loot", lootArray)
            val recipeArray = JsonArray()
            it.getRecipes()?.forEach { recipeArray.add(it?.type?.key.toString()) }
            if (recipeArray.size() > 0) reward.add("recipes", recipeArray)
            it.getExp()?.let { reward.addProperty("experience", it) }
        }
        json.add("rewards", reward)
        json.addProperty("sends_telemetry_event", sendsTelemetryEvent)

        return GsonBuilder().setPrettyPrinting().create().toJson(json)/*.also { println(it)}*/
    }
}