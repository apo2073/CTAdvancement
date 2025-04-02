package kr.apo2073.ctadvancement.toasts.setting

import com.google.gson.JsonObject
import kr.apo2073.ctadvancement.enums.Trigger

class Criteria(
    private val name: String,
    private val trigger: Trigger,
    private val conditions: JsonObject?
) {
    fun getName(): String = name

    fun getJson(): JsonObject {
        val json = JsonObject()
        json.addProperty("trigger", trigger.getTrigger())

        conditions?.let {
            json.add("conditions", it)
        }

        return json
    }
}
