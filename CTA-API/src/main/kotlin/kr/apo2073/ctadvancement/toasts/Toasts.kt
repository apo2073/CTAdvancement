package kr.apo2073.ctadvancement.toasts

import kr.apo2073.ctadvancement.CTAAPI
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import javax.naming.Name

open class Toasts {
    private lateinit var builder: ToastBuilder
    private lateinit var name: String

    internal constructor(toastBuilder: ToastBuilder) {
        this.builder=toastBuilder

    }
    constructor(name: String) {
        this.name=name
    }

    private fun isBuilder():Boolean {
        return (::builder.isInitialized && !::name.isInitialized)
    }

    fun remove():Toasts {
        builder.plugin.server.unsafe.removeAdvancement(NamespacedKey(CTAAPI.plugin,
            if (isBuilder()) { builder.name } else {name}
        ))
        return this
    }

    fun show(player: Player):Toasts {
        try {
            grantAdvancement(player)
            Bukkit.getScheduler().runTaskLater(builder.plugin, Runnable {
                revokeAdvancement(player)
            }, 20L)
        } catch (e:IllegalArgumentException ) {
            return this
        } catch (e:Exception) {e.printStackTrace()}
        return this
    }

    fun grantAdvancement(player: Player) {
        builder.requirements?.forEach {
            player.getAdvancementProgress(
                Bukkit.getAdvancement(if (isBuilder()) { builder.key}
                else {NamespacedKey(CTAAPI.plugin, name)}) ?: return
            ).awardCriteria(it.getName())
        }
    }

    fun revokeAdvancement(player: Player) {
        builder.requirements?.forEach {
            player.getAdvancementProgress(
                Bukkit.getAdvancement(if (isBuilder()) { builder.key}
                else {NamespacedKey(CTAAPI.plugin, name)}) ?: return
            ).revokeCriteria(it.getName())
        }
    }
}