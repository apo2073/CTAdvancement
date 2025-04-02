package kr.apo2073.ctadvancement

import kr.apo2073.ctadvancement.cmds.CTACommand
import kr.apo2073.ctadvancement.utilities.Utilities.loadToasts
import org.bukkit.plugin.java.JavaPlugin

class CTA : JavaPlugin() {
    companion object {
        lateinit var instance: CTA
            private set
    }
    override fun onEnable() {
        instance=this
        saveDefaultConfig()
        CTACommand(this)

        saveResource("advancement/test.json", false)
        saveResource("advancement/finally_end.yml", false)
        saveResource("lang/en.json", true)
        saveResource("lang/ko.json", true)

        loadToasts()
    }
}

