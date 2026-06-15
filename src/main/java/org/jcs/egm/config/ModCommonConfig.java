package org.jcs.egm.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.jcs.egm.stones.StoneAbilityCosts;

import java.util.List;

public class ModCommonConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> REALITY_STONE_BLOCK_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> REALITY_STONE_ENTITY_BLACKLIST;

    // Metamorphosis (Reality Stone) – NEW
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> REALITY_METAMORPHOSIS_FLY_ENTITIES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> REALITY_METAMORPHOSIS_SWIM_ENTITIES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> REALITY_METAMORPHOSIS_FIREPROOF_ENTITIES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> REALITY_METAMORPHOSIS_SLOWFALL_ENTITIES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> REALITY_METAMORPHOSIS_SPIDERCLIMB_ENTITIES;

    // Mind Stone (existing)
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MIND_STONE_ENTITY_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MIND_STONE_FLIGHT_ENTITIES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MIND_STONE_WALLCLIMB_ENTITIES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MIND_STONE_SWIMSPEED_ENTITIES;

    public static final ForgeConfigSpec.BooleanValue ENABLE_CAMERA_SHAKE;
    public static final ForgeConfigSpec.IntValue RAW_STONE_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.IntValue STONE_HOLDER_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.IntValue GAUNTLET_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.DoubleValue RAW_STONE_ENERGY_REGEN_PER_SECOND;
    public static final ForgeConfigSpec.DoubleValue STONE_HOLDER_ENERGY_REGEN_PER_SECOND;
    public static final ForgeConfigSpec.DoubleValue GAUNTLET_ENERGY_REGEN_PER_SECOND;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> STONE_ABILITY_INSTANT_COSTS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> STONE_ABILITY_CHANNEL_COSTS_PER_SECOND;
    public static final ForgeConfigSpec.ConfigValue<String> STONE_ENERGY_BAR_BACKGROUND_TEXTURE;
    public static final ForgeConfigSpec.ConfigValue<String> STONE_ENERGY_BAR_FILL_TEXTURE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        // ---------- General ----------
        builder.push("General");
        ENABLE_CAMERA_SHAKE = builder
                .comment("If true, certain abilities (e.g., Shockwave Slam) will trigger a client-side camera shake.")
                .define("enable_camera_shake", true);
        builder.pop();

        // ---------- Stone Energy ----------
        builder.push("StoneEnergy");
        RAW_STONE_ENERGY_CAPACITY = builder
                .comment("Maximum energy stored by a raw Infinity Stone.")
                .defineInRange("raw_stone_capacity", 300, 1, 100000);
        STONE_HOLDER_ENERGY_CAPACITY = builder
                .comment("Maximum energy stored by a Stone Holder.")
                .defineInRange("stone_holder_capacity", 800, 1, 100000);
        GAUNTLET_ENERGY_CAPACITY = builder
                .comment("Maximum energy stored by the Infinity Gauntlet. This is one shared pool for all inserted stones.")
                .defineInRange("gauntlet_capacity", 1000, 1, 100000);
        RAW_STONE_ENERGY_REGEN_PER_SECOND = builder
                .comment("Energy regenerated per second by raw Infinity Stones.")
                .defineInRange("raw_stone_regen_per_second", 2.0D, 0.0D, 100000.0D);
        STONE_HOLDER_ENERGY_REGEN_PER_SECOND = builder
                .comment("Energy regenerated per second by Stone Holders.")
                .defineInRange("stone_holder_regen_per_second", 5.0D, 0.0D, 100000.0D);
        GAUNTLET_ENERGY_REGEN_PER_SECOND = builder
                .comment("Energy regenerated per second by the Infinity Gauntlet.")
                .defineInRange("gauntlet_regen_per_second", 8.0D, 0.0D, 100000.0D);

        STONE_ABILITY_INSTANT_COSTS = builder
                .comment(
                        "Energy costs for instant abilities. Format: stone:ability=cost.",
                        "Charged abilities are charged when they fire, not while charging.",
                        "Set an entry to 0 to make that ability free."
                )
                .defineListAllowEmpty(
                        "instant_costs",
                        StoneAbilityCosts.defaultInstantCostConfig(),
                        obj -> obj instanceof String s && s.matches("[a-z0-9_]+:[a-z0-9_]+=\\d+"));

        STONE_ABILITY_CHANNEL_COSTS_PER_SECOND = builder
                .comment(
                        "Energy costs per second for channeling abilities. Format: stone:ability=cost_per_second.",
                        "These costs are drained only after any charge-up completes."
                )
                .defineListAllowEmpty(
                        "channel_costs_per_second",
                        StoneAbilityCosts.defaultChannelCostConfig(),
                        obj -> obj instanceof String s && s.matches("[a-z0-9_]+:[a-z0-9_]+=\\d+"));

        STONE_ENERGY_BAR_BACKGROUND_TEXTURE = builder
                .comment("Optional GUI texture for the stone energy meter background. Leave empty to use fallback rectangles. Format: modid:path")
                .define("bar_background_texture", "");
        STONE_ENERGY_BAR_FILL_TEXTURE = builder
                .comment("Optional GUI texture for the stone energy meter fill. Leave empty to use fallback rectangles. Format: modid:path")
                .define("bar_fill_texture", "");
        builder.pop();

        // ---------- Reality Stone ----------
        builder.push("RealityStone");

        REALITY_STONE_BLOCK_BLACKLIST = builder
                .comment("Blocks the Reality Stone will never create. Format: modid:block_name")
                .defineListAllowEmpty(
                        "reality_stone_block_blacklist",
                        List.of("minecraft:bedrock", "minecraft:command_block"),
                        obj -> obj instanceof String s && s.contains(":"));

        REALITY_STONE_ENTITY_BLACKLIST = builder
                .comment("Entities the Reality Stone will never create. Format: modid:entity_name")
                .defineListAllowEmpty(
                        "reality_stone_entity_blacklist",
                        List.of("minecraft:ender_dragon", "minecraft:wither"),
                        obj -> obj instanceof String s && s.contains(":"));

        // ---- Metamorphosis (NEW) ----
        builder.push("Metamorphosis");

        REALITY_METAMORPHOSIS_FLY_ENTITIES = builder
                .comment("Entities that grant flight when morphed. Format: modid:entity_name")
                .defineListAllowEmpty(
                        "fly_entities",
                        List.of(
                                "minecraft:bat","minecraft:bee","minecraft:parrot","minecraft:phantom",
                                "minecraft:ghast","minecraft:blaze","minecraft:vex","minecraft:allay",
                                "minecraft:wither","minecraft:ender_dragon"
                        ),
                        obj -> obj instanceof String s && s.contains(":"));

        REALITY_METAMORPHOSIS_SWIM_ENTITIES = builder
                .comment("Entities that grant strong swimming/water breathing when morphed. Format: modid:entity_name")
                .defineListAllowEmpty(
                        "swim_entities",
                        List.of(
                                "minecraft:squid","minecraft:glow_squid","minecraft:axolotl","minecraft:dolphin",
                                "minecraft:cod","minecraft:salmon","minecraft:pufferfish","minecraft:tropical_fish",
                                "minecraft:turtle","minecraft:guardian","minecraft:elder_guardian"
                        ),
                        obj -> obj instanceof String s && s.contains(":"));

        REALITY_METAMORPHOSIS_FIREPROOF_ENTITIES = builder
                .comment("Entities that grant fire resistance when morphed. Format: modid:entity_name")
                .defineListAllowEmpty(
                        "fireproof_entities",
                        List.of("minecraft:blaze","minecraft:wither","minecraft:ghast","minecraft:magma_cube","minecraft:strider"),
                        obj -> obj instanceof String s && s.contains(":"));

        REALITY_METAMORPHOSIS_SLOWFALL_ENTITIES = builder
                .comment("Entities that grant slow falling when morphed. Format: modid:entity_name")
                .defineListAllowEmpty(
                        "slowfall_entities",
                        List.of("minecraft:chicken"),
                        obj -> obj instanceof String s && s.contains(":"));

        REALITY_METAMORPHOSIS_SPIDERCLIMB_ENTITIES = builder
                .comment("Entities that grant wall-climb when morphed. Format: modid:entity_name")
                .defineListAllowEmpty(
                        "spiderclimb_entities",
                        List.of("minecraft:spider","minecraft:cave_spider"),
                        obj -> obj instanceof String s && s.contains(":"));

        builder.pop(); // Metamorphosis
        builder.pop(); // RealityStone

        // ---------- Mind Stone (existing) ----------
        builder.push("MindStone");

        MIND_STONE_ENTITY_BLACKLIST = builder
                .comment("Entities that cannot be possessed by the Mind Stone. Format: modid:entity_name")
                .defineListAllowEmpty(
                        "mind_stone_entity_blacklist",
                        List.of("minecraft:ender_dragon","minecraft:wither","minecraft:warden"),
                        obj -> obj instanceof String s && s.contains(":"));

        MIND_STONE_FLIGHT_ENTITIES = builder
                .comment("Entities that gain flight controls when possessed. Format: modid:entity_name.")
                .defineListAllowEmpty(
                        "mind_stone_flight_entities",
                        List.of("minecraft:bat","minecraft:blaze","minecraft:ghast","minecraft:parrot","minecraft:bee",
                                "minecraft:phantom","minecraft:vex","minecraft:ender_dragon","minecraft:allay"),
                        obj -> obj instanceof String s && s.contains(":"));

        MIND_STONE_WALLCLIMB_ENTITIES = builder
                .comment("Entities that gain wall-climb controls when possessed. Format: modid:entity_name.")
                .defineListAllowEmpty(
                        "mind_stone_wallclimb_entities",
                        List.of("minecraft:spider","minecraft:cave_spider","minecraft:silverfish","minecraft:endermite"),
                        obj -> obj instanceof String s && s.contains(":"));

        MIND_STONE_SWIMSPEED_ENTITIES = builder
                .comment("Entities that gain increased swim speed when possessed. Format: modid:entity_name.")
                .defineListAllowEmpty(
                        "mind_stone_swimspeed_entities",
                        List.of("minecraft:squid","minecraft:glow_squid","minecraft:axolotl","minecraft:dolphin",
                                "minecraft:cod","minecraft:salmon","minecraft:pufferfish","minecraft:tropical_fish","minecraft:turtle"),
                        obj -> obj instanceof String s && s.contains(":"));

        builder.pop(); // MindStone

        COMMON_CONFIG = builder.build();
    }
}
