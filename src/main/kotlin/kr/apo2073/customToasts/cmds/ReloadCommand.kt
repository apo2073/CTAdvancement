package kr.apo2073.customToasts.cmds

import kr.apo2073.customToasts.utilities.Utilities.loadToasts
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReloadCommand: CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (p0 !is Player) return false
        if (loadToasts()) {
            p0.sendMessage(
                MiniMessage.miniMessage()
                    .deserialize("<b><gradient:#DBCDF0:#8962C3>[ CTAdvancement ]</gradient></b> ")
                    .append(
                        Component.text("리로드를 완료했습니다.")
                    )
            )
        } else {
            p0.sendMessage(
                MiniMessage.miniMessage()
                    .deserialize("<b><gradient:#DBCDF0:#8962C3>[ CTAdvancement ]</gradient></b> ")
                    .append(
                        Component.text("리로드 중 오류가 발생했습니다!")
                    )
            )
        }
        return true
    }
}