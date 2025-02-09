package kr.apo2073.customToasts.enums

enum class Trigger(
    private val trigger:String
) {
    EMPTY(""),
    IMPOSSIBLE("minecraft:impossible"),
    TICK("minecraft:tick"),
    USED_TOTEM("minecraft:used_totem"),
    ALLAY_DROP_ITEM_ON_BLOCK("minecraft:allay_drop_item_on_block"),
    AVOID_VIBRATION("minecraft:avoid_vibration"),
    BEE_NEST_DESTROYED("minecraft:bee_nest_destroyed"),
    BRED_ANIMALS("minecraft:bred_animals"),
    BREWED_POTION("minecraft:brewed_potion"),
    CHANGED_DIMENSION("minecraft:changed_dimension"),
    CHANNELED_LIGHTNING("minecraft:channeled_lightning"),
    CONSTRUCT_BEACON("minecraft:construct_beacon"),
    CONSUME_ITEM("minecraft:consume_item"),
    CURED_ZOMBIE_VILLAGER("minecraft:cured_zombie_villager"),
    EFFECTS_CHANGED("minecraft:effects_changed"),
    ENCHANTED_ITEM("minecraft:enchanted_item"),
    ENTER_BLOCK("minecraft:enter_block"),
    ENTITY_HURT_PLAYER("minecraft:entity_hurt_player"),
    ENTITY_KILLED_PLAYER("minecraft:entity_killed_player"),
    FALL_FROM_HEIGHT("minecraft:fall_from_height"),
    FILLED_BUCKET("minecraft:filled_bucket"),
    FISHING_ROD_HOOKED("minecraft:fishing_rod_hooked"),
    HERO_OF_THE_VILLAGE("minecraft:hero_of_the_village"),
    INVENTORY_CHANGED("minecraft:inventory_changed"),
    ITEM_DURABILITY_CHANGED("minecraft:item_durability_changed"),
    ITEM_USED_ON_BLOCK("minecraft:item_used_on_block"),
    KILL_MOB_NEAR_SCULK_CATALYST("minecraft:kill_mob_near_sculk_catalyst"),
    KILLED_BY_CROSSBOW("minecraft:killed_by_crossbow"),
    LEVITATION("minecraft:levitation"),
    LIGHTNING_STRIKE("minecraft:lightning_strike"),
    LOCATION("minecraft:location"),
    NETHER_TRAVEL("minecraft:nether_travel"),
    PLACED_BLOCK("minecraft:placed_block"),
    PLAYER_GENERATES_CONTAINER_LOOT("minecraft:player_generates_container_loot"),
    PLAYER_HURT_ENTITY("minecraft:player_hurt_entity"),
    PLAYER_INTERACTED_WITH_ENTITY("minecraft:player_interacted_with_entity"),
    PLAYER_KILLED_ENTITY("minecraft:player_killed_entity"),
    RECIPE_CRAFTED("minecraft:recipe_crafted"),
    RECIPE_UNLOCKED("minecraft:recipe_unlocked"),
    RIDE_ENTITY_IN_LAVA("minecraft:ride_entity_in_lava"),
    SHOT_CROSSBOW("minecraft:shot_crossbow"),
    SLEPT_IN_BED("minecraft:slept_in_bed"),
    SLIDE_DOWN_BLOCK("minecraft:slide_down_block"),
    STARTED_RIDING("minecraft:started_riding"),
    SUMMONED_ENTITY("minecraft:summoned_entity"),
    TAME_ANIMAL("minecraft:tame_animal"),
    TARGET_HIT("minecraft:target_hit"),
    THROWN_ITEM_PICKED_UP_BY_ENTITY("minecraft:thrown_item_picked_up_by_entity"),
    THROWN_ITEM_PICKED_UP_BY_PLAYER("minecraft:thrown_item_picked_up_by_player"),
    USED_ENDER_EYE("minecraft:used_ender_eye"),
    USING_ITEM("minecraft:using_item"),
    VILLAGER_TRADE("minecraft:villager_trade"),
    VOLUNTARY_EXILE("minecraft:voluntary_exile");

    fun getTrigger():String {
        return trigger
    }
    companion object {
        fun getByTrigger(trigger: String): Trigger? {
            return entries.find { it.getTrigger() == trigger }
        }
    }
}