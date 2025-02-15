package kr.apo2073.customToasts.utilities

import com.google.gson.Gson
import com.google.gson.JsonObject
import kr.apo2073.customToasts.CustomToasts
import kr.apo2073.customToasts.enums.Frame
import kr.apo2073.customToasts.enums.Trigger
import kr.apo2073.customToasts.toasts.ToastBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File
import java.nio.charset.StandardCharsets

object Utilities {
    private val plugin=CustomToasts.instance
    fun loadToasts(): Boolean {
        plugin.reloadConfig()
        try {
            for (toast in toastList()) {
                val file=File("${plugin.dataFolder}/advancement", toast)
                if (toast.endsWith(".json")) load(FileType.JSON, file)
                if (toast.endsWith(".yml")) load(FileType.YML, file)
            }
            return true
        } catch (e:Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun toastList():MutableList<String> {
        plugin.reloadConfig()
        return plugin.config.getStringList("advancement")
    }

    private fun load(type: FileType, file: File) {
        val fileName=file.name
            .replace(".json", "")
            .replace(".yml", "")
        when(type) {
            FileType.YML-> {
                val config=YamlConfiguration.loadConfiguration(file)
                val key=NamespacedKey(plugin, config.getString("${fileName}.id") ?: return)
                val icon= getItemStack(config.getString("${fileName}.icon") ?: return) ?: return
                val title=config.getString("${fileName}.title") ?: return
                val description=config.getString("${fileName}.description") ?: return
                val frame=Frame.valueOf(config.getString("${fileName}.frame") ?: return)
                val showToasts=config.getBoolean("${fileName}.show_toast")
                val announceToChat=config.getBoolean("${fileName}.announce_to_chat")
                val hidden=config.getBoolean("${fileName}.hidden")
                val parent=config.getString("${fileName}.parent") ?: return
                val background=config.getString("${fileName}.background")
                val reward=Reward(
                    experience = config.getInt("${fileName}.rewards.experience"),
                    function = config.getString("${fileName}.rewards.function"),
                    loots = config.getStringList("${fileName}.rewards.loots"),
                    recipes = config.getStringList("${fileName}.rewards.recipes").map { getItemStack(it) }
                )
                val criteria = mutableListOf<Criteria>()
                val criteriaSection = config.getConfigurationSection("${fileName}.criteria") ?: return
                for (name in criteriaSection.getKeys(false)) {
                    val dataMap = criteriaSection.getConfigurationSection(name)?.getValues(false) ?: continue
                    val trigger = Trigger.getByTrigger(dataMap["trigger"].toString()) ?: return
                    val conditionsJson = JsonObject()
                    println(name)

                    println(dataMap["conditions"])
                    if (dataMap["conditions"] is Map<*, *>) {
                        val conditions = dataMap["conditions"] as Map<*, *>
                        conditions.forEach { (key, value) ->
                            when (value) {
                                is Map<*, *> -> {
                                    val innerJson = JsonObject()
                                    value.forEach { (k, v) ->
                                        innerJson.addProperty(k.toString(), v.toString())
                                    }
                                    conditionsJson.add(key.toString(), innerJson)
                                }
                                else -> conditionsJson.addProperty(key.toString(), value.toString())
                            }
                        }
                    }

                    criteria.add(Criteria(name, trigger, conditionsJson))
                }

                ToastBuilder(
                    plugin, key,
                    icon, title, description, frame,
                    showToasts, announceToChat, hidden,
                    parent, criteria, arrayListOf(reward), background
                ).build()
            }
            FileType.JSON-> {
                val content=file.readText(StandardCharsets.UTF_8)
                val key=NamespacedKey(plugin, fileName)
//                println(key)
                try {
                    plugin.server.unsafe.loadAdvancement(key, content)
                } catch (e:IllegalArgumentException) {
                    plugin.server.unsafe.removeAdvancement(key)
                    Bukkit.reloadData()
                    plugin.server.unsafe.loadAdvancement(key, content)
                }
            }
        }
    }

    private fun getItemStack(key:String) : ItemStack? {
        return ItemStack(
            Material.getMaterial(key.removePrefix("minecraft:").uppercase())
            ?: run { return null }
        )
    }

    enum class FileType {
        JSON,
        YML
    }
}