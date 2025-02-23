package kr.apo2073.ctadvancement

import org.bukkit.plugin.java.JavaPlugin

class CTAAPI : JavaPlugin() {
    companion object {
        lateinit var plugin: JavaPlugin
            private set
    }
    override fun onEnable() {
        plugin=this
    }
}
