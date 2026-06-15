package org.jcs.egm.stones.stone_reality;

import org.jcs.egm.stones.StoneItem;

public class RealityStoneItem extends StoneItem {
    public RealityStoneItem(Properties properties) {
        super(properties);
    }

    @Override public String getKey() { return "reality"; }
    @Override public int getColor() { return 0xFF0033; }
}
