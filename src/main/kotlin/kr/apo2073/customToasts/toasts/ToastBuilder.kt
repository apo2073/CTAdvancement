package kr.apo2073.customToasts.toasts

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kr.apo2073.customToasts.enums.Frame
import kr.apo2073.customToasts.enums.Trigger
import kr.apo2073.customToasts.utilities.Criteria
import kr.apo2073.customToasts.utilities.Reward
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

// https://misode.github.io/advancement/
class ToastBuilder(
    private val plugin:JavaPlugin,
    private val key: NamespacedKey,
    private val icon: ItemStack,
    private val title: String,
    private val description: String,
    private val frame:Frame,
    private var showToasts:Boolean=true,
    private var announceToChat:Boolean=true,
    private var hidden:Boolean=false,
    private var parent:String?=null,
    private var requirements: MutableList<Criteria>?=null,
    private var rewards: MutableList<Reward>?=null,
    private var background:String?="minecraft:textures/gui/advancements/backgrounds/adventure.png"
) {
    fun show(player: Player):ToastBuilder {
        try {
            grantAdvancement(player)
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                revokeAdvancement(player)
            }, 20L)
        } catch (e:IllegalArgumentException ) {
            return this
        } catch (e:Exception) {e.printStackTrace()}
        return this
    }

    fun build():ToastBuilder {
        try {
            plugin.server.unsafe.loadAdvancement(key, toastJson())
        } catch (e:IllegalArgumentException) {
            plugin.server.unsafe.removeAdvancement(key)
            Bukkit.reloadData()
            plugin.server.unsafe.loadAdvancement(key, toastJson())
            return this
        }
        return this
    }

    fun remove():ToastBuilder {
        plugin.server.unsafe.removeAdvancement(key)
        return this
    }

    private fun grantAdvancement(player: Player) {
        requirements?.forEach {
            player.getAdvancementProgress(
                Bukkit.getAdvancement(key) ?: return
            ).awardCriteria(it.getName())
        }
    }

    private fun revokeAdvancement(player: Player) {
        requirements?.forEach {
            player.getAdvancementProgress(
                Bukkit.getAdvancement(key) ?: return
            ).revokeCriteria(it.getName())
        }
    }

    private fun toastJson():String {
        if (background==null) background="minecraft:textures/gui/advancements/backgrounds/adventure.png"

        val json=JsonObject()

        val display=JsonObject()
        display.add(
            "icon",
            JsonObject().apply { addProperty("item", icon.type.key.toString()) }
        )
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
        val requireArray=JsonArray()
        requirements?.forEach {
            json.add("criteria", it.getJson())
            requireArray.add(it.getName())
        }
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

        return GsonBuilder().setPrettyPrinting().create().toJson(json)
    }
}