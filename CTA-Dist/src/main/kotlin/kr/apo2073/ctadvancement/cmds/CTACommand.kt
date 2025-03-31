package kr.apo2073.ctadvancement.cmds

import kr.apo2073.ctadvancement.toasts.Toasts
import kr.apo2073.ctadvancement.utilities.LangManager.translate
import kr.apo2073.ctadvancement.utilities.Utilities.loadToasts
import kr.apo2073.ctadvancement.utilities.sendMessage
import kr.apo2073.ctadvancement.utilities.str2Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class CTACommand(private val plugin: JavaPlugin): TabExecutor {
    init {
        plugin.getCommand("cta")?.also {
            it.setExecutor(this)
            it.tabCompleter=this
        }
    }

    private val prefix= MiniMessage.miniMessage().deserialize("<b><gradient:#DBCDF0:#8962C3>[ CTAdvancement ]</gradient></b> ")

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p3.isEmpty()) {
            sendUsage(p0)
        }
        if (p3.size==1) {
            if (p3[0]=="reload") {
                if (!p0.hasPermission("apo.cta.reload")) {
                    p0.sendMessage(translate("command.no.permissions"), true)
                    return true
                }
                if (loadToasts()) {
                    p0.sendMessage(prefix.append("§l리로드를 완료했습니다".str2Component()))
                } else {
                    p0.sendMessage(prefix.append("§l리로드 중 오류가 발생했습니다!".str2Component()))
                }
            } else {
                sendUsage(p0)
            }
        }
        if (p3.size==2) {
            if (p3[0]!="load" && p3[0]!="remove") {
                sendUsage(p0)
                return true
            }
            val list= plugin.config.getStringList("advancement")
            val advancement=p3[1]
            if (advancement !in list) {
                p0.sendMessage(prefix.append("§l해당 도전과제가 존재하지 않습니다".str2Component()))
                return true
            }
            when(p3[0]) {
                "load"-> loadToast(p0, advancement)
                "remove"-> removeToast(p0, advancement)
            }
        }
        if (p3.size==3) {
            if (p3[0]!="show") {
                sendUsage(p0)
                return true
            }
            val list= plugin.config.getStringList("advancement")
            val advancement=p3[1]
            if (advancement !in list) {
                p0.sendMessage(prefix.append("§l해당 도전과제가 존재하지 않습니다".str2Component()))
                return true
            }
            val player=plugin.server.getPlayer(p3[2]) ?: run {
                p0.sendMessage(prefix.append("§l해당 플레이어가 존재하지 않습니다".str2Component()))
                return true
            }
            showToast(p0, advancement, player)
        }
        return true
    }

    private fun sendUsage(p0: CommandSender) {
        val message= arrayOf(
            "§l올바른 명령어를 입력해 주세요",
            "§l ⌊ §7/cta reload §f- 커스텀 도전과제를 리로드 합니다",
            "§l ⌊ §7/cta load [<string>] §f- 커스텀 도전과제 [<string>]을(를) 로드 합니다",
            "§l ⌊ §7/cta remove [<string>] §f- 커스텀 도전과제 [<string>]을(를) 제거 합니다",
            "§l ⌊ §7/cta show [<string>] [<player>] §f- 커스텀 도전과제 [<string>]을(를) [<player>]에게 보여줍니다"
        )
        message.forEach {
            p0.sendMessage(prefix.append(
                it.str2Component()
            ))
        }
    }

    private fun loadToast(p0: CommandSender, toast: String) {
        if (!p0.hasPermission("apo.cta.load")) {
            p0.sendMessage(translate("command.no.permissions"), true)
            return
        }
        if (loadToasts(toast)) {
            p0.sendMessage(prefix.append("§l로드를 완료했습니다".str2Component()))
        } else {
            p0.sendMessage(prefix.append("§l로드 중 오류가 발생했습니다!".str2Component()))
        }
    }

    private fun removeToast(p0: CommandSender, toast: String) {
        if (!p0.hasPermission("apo.cta.remove")) {
            p0.sendMessage(translate("command.no.permissions"), true)
            return
        }
        try {
            plugin.server.unsafe.removeAdvancement(
                NamespacedKey(plugin, toast
                    .replace(".json", "")
                    .replace(".yml", ""))
            )
            Bukkit.reloadData()
            p0.sendMessage(prefix.append("§l제거를 완료했습니다".str2Component()))
        } catch (e: Exception) {
            p0.sendMessage(prefix.append("§l제거 중 오류가 발생했습니다".str2Component()))
            e.printStackTrace()
        }
    }

    private fun showToast(p0: CommandSender, toast: String, player: Player) {
        if (!p0.hasPermission("apo.cta.show")) {
            p0.sendMessage(translate("command.no.permissions"), true)
            return
        }
        try {
            val toasts=Toasts(toast)
            toasts.show(player)
        } catch (e: Exception) {
            p0.sendMessage(prefix.append("§l명령어를 실행하던 도중 오류가 발생했습니다".str2Component()))
            e.printStackTrace()
        }
    }

    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>
    ): MutableList<String> {
        val tab= mutableListOf<String>()
        if (p3.size==1) {
            tab.add("reload")
            tab.add("load")
            tab.add("remove")
            tab.add("show")
        }
        if (p3.size==2 && (p3[0] == "load" || p3[0] == "remove" || p3[0] == "show")) {
            val list= plugin.config.getStringList("advancement")
            tab.addAll(list)
        }
        if (p3.size==3 && (p3[0] == "show")) {
            tab.addAll(plugin.server.onlinePlayers.map { it.name })
        }
        return tab
    }
}