package kr.apo2073.ctadvancement.toasts.setting

import com.google.gson.JsonObject
import kr.apo2073.ctadvancement.utilities.VersionManager
import org.bukkit.inventory.ItemStack

class Icons(private var item:ItemStack) {
    private var nbtTag:String?=null
    private var componentMap:MutableMap<String, String>?=null

    fun setNbt(nbt:String?) {
        this.nbtTag=nbt
    }
    fun addComponent(info:String, setting:String) {
        if (componentMap==null) componentMap= mutableMapOf()
        componentMap!![info]=setting
    }
    fun getNbt():String=nbtTag ?: ""
    fun getComponent():MutableMap<String, String> = componentMap ?: mutableMapOf()

    fun generate():JsonObject {
        val json=JsonObject()
        if (VersionManager.isV1_21()) {
            json.addProperty("id", item.type.key.toString())
            if (componentMap!=null) {
                componentMap!!.forEach { (v, k) ->
                    json.addProperty(v, k)
                }
            }
        } else {
            json.addProperty("item", item.type.key.toString())
            if (nbtTag!=null) json.addProperty("nbt", nbtTag)
        }
        return json
    }
}
