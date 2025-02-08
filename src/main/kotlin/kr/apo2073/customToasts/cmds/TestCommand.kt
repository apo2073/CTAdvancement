package kr.apo2073.customToasts.cmds

import kr.apo2073.customToasts.CustomToasts
import kr.apo2073.customToasts.enums.Frame
import kr.apo2073.customToasts.utilities.ToastBuilder
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
        val toast=ToastBuilder(
            CustomToasts.instance,
            NamespacedKey(CustomToasts.instance, "test"),
            ItemStack(Material.BREAD),
            "Test toasts",
            "description",
            Frame.CHALLENGE
        ).build()
        toast.show(p0)
        return true
    }
}