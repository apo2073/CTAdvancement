package kr.apo2073.customToasts.utilities

import com.google.gson.JsonObject
import kr.apo2073.customToasts.enums.Trigger

class Criteria(
    private val name:String,
    private val trigger: Trigger,
    private val conditions:JsonObject?
) {
    fun getName():String=name
    fun getJson():JsonObject {
        val criteria=JsonObject()

        val json=JsonObject()
        json.addProperty("trigger", trigger.getTrigger())
        if (conditions!=null) json.add("conditions", conditions)

        criteria.add(name, json)

        return criteria
    }
}