package kr.apo2073.customToasts.cmds

import kr.apo2073.customToasts.CustomToasts
import kr.apo2073.customToasts.enums.Frame
import kr.apo2073.customToasts.toasts.ToastBuilder
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TestCommand:TabExecutor {
    override fun onTabComplete(
        p0: CommandSender,
        p1: Command,
        p2: String,
        p3: Array<out String>
    ): MutableList<String> {
        val tab= mutableListOf<String>()
        return tab
    }

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p0 !is Player) return false
        ToastBuilder(
            CustomToasts.instance,
            NamespacedKey(CustomToasts.instance, "tests"),
            ItemStack(Material.LEATHER_HELMET),
            "Test toasts",
            "description",
            Frame.CHALLENGE
        ).build().show(player = p0)
        return true
    }
}