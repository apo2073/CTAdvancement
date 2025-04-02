package kr.apo2073.ctadvancement.utilities

import org.bukkit.Bukkit

object VersionManager {
    fun versionLeast(major: Int, minor: Int): Boolean {
        val version = Bukkit.getBukkitVersion().split("-")[0] // "1.21.1-R0.1-SNAPSHOT" -> "1.21.1"
        val versionNumbers = version.split(".")
            .mapNotNull { it.toIntOrNull() }

        if (versionNumbers.size < 2) return false

        val serverMajor = versionNumbers[0]
        val serverMinor = versionNumbers[1]

        return serverMajor > major || (serverMajor == major && serverMinor >= minor)
    }

    fun isV1_21(): Boolean = versionLeast(1, 21)
}