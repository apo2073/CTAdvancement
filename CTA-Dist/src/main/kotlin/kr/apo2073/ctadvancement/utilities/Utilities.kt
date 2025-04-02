package kr.apo2073.ctadvancement.utilities

import com.google.gson.Gson
import kr.apo2073.ctadvancement.CTA
import kr.apo2073.ctadvancement.enums.Frame
import kr.apo2073.ctadvancement.enums.Trigger
import kr.apo2073.ctadvancement.toasts.ToastBuilder
import kr.apo2073.ctadvancement.toasts.setting.Criteria
import kr.apo2073.ctadvancement.toasts.setting.Icons
import kr.apo2073.ctadvancement.toasts.setting.Reward
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File
import java.nio.charset.StandardCharsets

object Utilities {
    private val plugin=CTA.instance
    fun loadToasts(name:String?=null): Boolean {
        plugin.reloadConfig()
        try {

            for (toast in toastList()) {
                if (name==null) {
                    val file = File("${plugin.dataFolder}/advancement", toast)
                    if (toast.endsWith(".json")) load(FileType.JSON, file)
                    if (toast.endsWith(".yml")) load(FileType.YML, file)
                } else {
                    if (toast.contains(name)) {
                        val file = File("${plugin.dataFolder}/advancement", toast)
                        if (toast.endsWith(".json")) load(FileType.JSON, file)
                        if (toast.endsWith(".yml")) load(FileType.YML, file)
                    }
                }

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
                val name=config.getString("${fileName}.id") ?: return
                val itemId = config.getString("${fileName}.icon.id") ?: return
                val itemStack = getItemStack(itemId) ?: return
                val icon = Icons(itemStack).apply {
                    config.getString("${fileName}.icon.nbt")?.let { setNbt(it) }

                    config.getConfigurationSection("${fileName}.icon.component")?.let { component ->
                        component.getValues(false).forEach { (key, value) ->
                            addComponent(key, value.toString())
                        }
                    }
                }
                val title=config.getString("${fileName}.title") ?: return
                val description=config.getString("${fileName}.description") ?: return
                val frame=Frame.valueOf(config.getString("${fileName}.frame") ?: return)
                val showToasts=config.getBoolean("${fileName}.show_toast")
                val announceToChat=config.getBoolean("${fileName}.announce_to_chat")
                val hidden=config.getBoolean("${fileName}.hidden")
                val parent=config.getString("${fileName}.parent") ?: return
                val background=config.getString("${fileName}.background")
                val reward= Reward(
                    experience = config.getInt("${fileName}.rewards.experience"),
                    function = config.getString("${fileName}.rewards.function"),
                    loots = config.getStringList("${fileName}.rewards.loots"),
                    recipes = config.getStringList("${fileName}.rewards.recipes").map { getItemStack(it) }
                )
                val criteria = mutableListOf<Criteria>()
                val criteriaSection = config.getConfigurationSection("${fileName}.criteria") ?: return
                val sendsTelemetryEvent=config.getBoolean("${fileName}.sends_telemetry_event")
                for (name in criteriaSection.getKeys(false)) {
                    val dataMap = criteriaSection.getConfigurationSection(name)?.getValues(false) ?: continue
                    val trigger = Trigger.getByTrigger(dataMap["trigger"].toString()) ?: return
                    val conditions = when (val rawConditions = dataMap["conditions"]) {
                        is Map<*, *> -> rawConditions as Map<String, Any>
                        is MemorySection -> rawConditions.getValues(false)
                        else -> emptyMap()
                    }
                    criteria.add(Criteria(name, trigger, Gson().toJsonTree(conditions).asJsonObject))
                }

                ToastBuilder(
                    plugin, name,
                    icon, title, description, frame,
                    showToasts, announceToChat, hidden,
                    parent, criteria, arrayListOf(reward), background,
                    sendsTelemetryEvent
                ).build()
            }
            FileType.JSON-> {
                val content=file.readText(StandardCharsets.UTF_8)
                val key=NamespacedKey(plugin, fileName)
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