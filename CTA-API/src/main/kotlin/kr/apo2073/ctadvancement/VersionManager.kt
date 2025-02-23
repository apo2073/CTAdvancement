package kr.apo2073.ctadvancement

import org.bukkit.Bukkit

class VersionManager {
    companion object {
        fun getVersions():String {
            return Bukkit.getVersion().split(".")[2]
        }
    }
}