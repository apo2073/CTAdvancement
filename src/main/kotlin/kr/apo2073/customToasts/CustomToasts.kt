package kr.apo2073.customToasts

import kr.apo2073.customToasts.cmds.TestCommand
import kr.apo2073.customToasts.enums.Frame
import kr.apo2073.customToasts.enums.Trigger
import kr.apo2073.customToasts.utilities.Criteria
import kr.apo2073.customToasts.utilities.Reward
import kr.apo2073.customToasts.utilities.ToastBuilder
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class CustomToasts : JavaPlugin() {
    companion object {
        lateinit var instance: CustomToasts
            private set
    }
    override fun onEnable() {
        instance=this
        saveDefaultConfig()
        getCommand("toast")?.setExecutor(TestCommand())

        ToastBuilder(
            this,
            NamespacedKey(this, "test"),
            ItemStack(Material.PAPER),
            "테스트 도전과제",
            "설명",
            Frame.CHALLENGE,
            requirements = mutableListOf(Criteria("yeah", Trigger.USED_ENDER_EYE, null)),
            hidden = true,
            rewards = mutableListOf(Reward(experience = 123))
        ).build()
    }
}
