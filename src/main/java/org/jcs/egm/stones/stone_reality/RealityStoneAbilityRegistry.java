package org.jcs.egm.stones.stone_reality;

import org.jcs.egm.stones.IGStoneAbility;

import java.util.ArrayList;
import java.util.List;

public class RealityStoneAbilityRegistry {
    // Register all abilities for the reality stone
    public static final IGStoneAbility WILLED_CHAOS = new WilledChaosRealityStoneAbility();
    public static final IGStoneAbility METAMORPHOSIS = new MetamorphosisRealityStoneAbility();
    public static final IGStoneAbility TITANS_PARADISE = new TitansParadiseRealityStoneAbility();

    private static final List<IGStoneAbility> ABILITIES = new ArrayList<>();
    static {
        ABILITIES.add(WILLED_CHAOS);
        ABILITIES.add(METAMORPHOSIS);
        ABILITIES.add(TITANS_PARADISE);
    }

    public static List<IGStoneAbility> getAbilities() {
        return ABILITIES;
    }

    public static IGStoneAbility getSelectedAbility(net.minecraft.world.item.ItemStack stack) {
        int idx = stack.getOrCreateTag().getInt("AbilityIndex");
        if (idx < 0 || idx >= ABILITIES.size()) return ABILITIES.get(0);
        return ABILITIES.get(idx);
    }
}
