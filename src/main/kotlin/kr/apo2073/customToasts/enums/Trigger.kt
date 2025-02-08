package kr.apo2073.customToasts.enums

enum class Trigger(
    private val trigger:String
) {
    EMPTY(""),
    IMPOSSIBLE("minecraft:impossible"),
    TICK("minecraft:tick"),
    USED_TOTEM("minecraft:used_totem");
    // TODO: trigger 노가다 하기(gpt한테 시킬 수 있으면 시키기)

    fun getTrigger():String {
        return trigger
    }
}