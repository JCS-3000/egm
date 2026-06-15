package org.jcs.egm.stones;

import net.minecraft.network.chat.Component;
import org.jcs.egm.stones.stone_mind.MindStoneAbilityRegistry;
import org.jcs.egm.stones.stone_power.PowerStoneAbilityRegistry;
import org.jcs.egm.stones.stone_reality.RealityStoneAbilityRegistry;
import org.jcs.egm.stones.stone_soul.SoulStoneAbilityRegistry;
import org.jcs.egm.stones.stone_space.SpaceStoneAbilityRegistry;
import org.jcs.egm.stones.stone_time.TimeStoneAbilityRegistry;

import java.util.List;

/**
 * Central utility for accessing per-stone multi-ability registries.
 * Supports menu display, left-click selection, etc.
 */
public class StoneAbilityRegistries {

    /**
     * Returns the list of ability display names for the given stone key.
     * Example: "power" → ["Infinite Lightning", ...]
     */
    public static List<Component> getAbilityNames(String stoneKey) {
        return StoneAbilityCosts.displayNamesForStone(stoneKey);
    }

    public static boolean isValidAbilityIndex(String stoneKey, int index) {
        return index >= 0 && index < getAbilityNames(stoneKey).size();
    }

    public static List<IGStoneAbility> getAbilities(String stoneKey) {
        return switch (stoneKey) {
            case "mind"    -> MindStoneAbilityRegistry.getAbilities();
            case "power"   -> PowerStoneAbilityRegistry.getAbilities();
            case "reality" -> RealityStoneAbilityRegistry.getAbilities();
            case "soul"    -> SoulStoneAbilityRegistry.getAbilities();
            case "space"   -> SpaceStoneAbilityRegistry.getAbilities();
            case "time"    -> TimeStoneAbilityRegistry.getAbilities();
            default        -> List.of();
        };
    }

    public static List<IGStoneAbility> getAllAbilities() {
        return List.of("mind", "power", "reality", "soul", "space", "time").stream()
                .flatMap(stone -> getAbilities(stone).stream())
                .toList();
    }

    /**
     * Returns the IGStoneAbility for the selected index on the stack (always uses NBT).
     */
    public static IGStoneAbility getSelectedAbility(String stoneKey, net.minecraft.world.item.ItemStack stack) {
        return switch (stoneKey) {
            case "mind"    -> MindStoneAbilityRegistry.getSelectedAbility(stack);
            case "power"   -> PowerStoneAbilityRegistry.getSelectedAbility(stack);
            case "reality" -> RealityStoneAbilityRegistry.getSelectedAbility(stack);
            case "soul"    -> SoulStoneAbilityRegistry.getSelectedAbility(stack);
            case "space"   -> SpaceStoneAbilityRegistry.getSelectedAbility(stack);
            case "time"    -> TimeStoneAbilityRegistry.getSelectedAbility(stack);
            default        -> null;
        };
    }
}
