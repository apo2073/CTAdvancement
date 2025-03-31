package kr.apo2073.ctadvancement.utilities

import com.google.gson.Gson
import com.google.gson.JsonObject
import kr.apo2073.ctadvancement.CTA
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import java.io.File

private val prefix= MiniMessage.miniMessage().deserialize("<b><gradient:#DBCDF0:#8962C3>[ CTA ]</gradient></b> ")
fun CommandSender.sendMessage(string: String, boolean: Boolean) {
    if (boolean) {
        this.sendMessage(prefix.append(string.replace("&", "§").str2Component()))
    } else {
        this.sendMessage(string.replace("&", "§").str2Component())
    }
}

object LangManager {
    private val plugin = CTA.instance
    private var language = plugin.config.getString("lang", "ko")
    private var langFile = File("${plugin.dataFolder}/lang", "${language}.json")
    private val langJson: JsonObject

    init {
        plugin.reloadConfig()
        language = plugin.config.getString("lang", "ko") ?: "ko"
        langFile = File("${plugin.dataFolder}/lang", "${language}.json")
        langJson = try {
            val lang = langFile.readText()
            Gson().fromJson(lang, JsonObject::class.java)
        } catch (e: Exception) {
            JsonObject()
        }
    }

    fun translate(text: String): String {
        return try {
            langJson.get(text)?.asString
                ?.replace("{lang}", language.toString())
                ?.replace("&", "§")
                ?: text
        } catch (e: Exception) {
            text
        }
    }
}

