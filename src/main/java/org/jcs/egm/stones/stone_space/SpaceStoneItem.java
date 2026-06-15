package org.jcs.egm.stones.stone_space;

import org.jcs.egm.stones.StoneItem;

public class SpaceStoneItem extends StoneItem {
    public SpaceStoneItem(Properties properties) {
        super(properties);
    }

    @Override public String getKey() { return "space"; }
    @Override public int getColor() { return 0x2e3bc9; }
}
