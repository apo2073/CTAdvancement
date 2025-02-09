package kr.apo2073.customToasts

import kr.apo2073.customToasts.cmds.ReloadCommand
import kr.apo2073.customToasts.cmds.TestCommand
import kr.apo2073.customToasts.utilities.Utilities.loadToasts
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
        getCommand("reloadToasts")?.setExecutor(ReloadCommand())

        saveResource("advancement/test.json", true)
        saveResource("advancement/finally_end.yml", true)

        loadToasts()
    }
}
