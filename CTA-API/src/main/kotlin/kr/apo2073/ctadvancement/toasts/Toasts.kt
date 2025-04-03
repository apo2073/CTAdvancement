package kr.apo2073.ctadvancement.toasts

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import java.io.File

open class Toasts {
    private var builder: ToastBuilder? = null
    private var name: String? = null

    internal constructor(toastBuilder: ToastBuilder) {
        this.builder = toastBuilder
    }

    constructor(name: String) {
        this.name = name
    }

    fun isBuilder(): Boolean = builder != null

    fun remove(): Toasts {
        val key = if (isBuilder()) builder!!.name else name ?: return this
        val plugin = if (isBuilder()) builder!!.plugin else Bukkit.getPluginManager().getPlugin("CTAdvancement") ?: return this
        plugin.server.unsafe.removeAdvancement(NamespacedKey(plugin, key))
        return this
    }

    fun show(player: Player): Toasts {
        try {
            grantAdvancement(player)
            val plugin = if (isBuilder()) builder!!.plugin else Bukkit.getPluginManager().getPlugin("CTAdvancement") ?: return this
            Bukkit.getScheduler().runTaskLater(plugin, Runnable { revokeAdvancement(player) }, 20L)
        } catch (e: IllegalArgumentException) {
            return this
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    fun grantAdvancement(player: Player) {
        val key = if (isBuilder()) builder!!.key else NamespacedKey(Bukkit.getPluginManager().getPlugin("CTAdvancement") ?: return, name ?: return)
        val advancement = Bukkit.getAdvancement(key) ?: return
        val requirements = builder?.requirements ?: return
        requirements.forEach { player.getAdvancementProgress(advancement).awardCriteria(it.getName()) }
    }

    fun revokeAdvancement(player: Player) {
        val key = if (isBuilder()) builder!!.key else NamespacedKey(Bukkit.getPluginManager().getPlugin("CTAdvancement") ?: return, name ?: return)
        val advancement = Bukkit.getAdvancement(key) ?: return
        val requirements = builder?.requirements ?: return
        requirements.forEach { player.getAdvancementProgress(advancement).revokeCriteria(it.getName()) }
    }
}
