package kr.apo2073.customToasts

import kr.apo2073.customToasts.cmds.TestCommand
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
    }
}
