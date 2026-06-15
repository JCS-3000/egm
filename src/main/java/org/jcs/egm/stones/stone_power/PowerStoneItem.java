package org.jcs.egm.stones.stone_power;

import org.jcs.egm.stones.StoneItem;

public class PowerStoneItem extends StoneItem {
    public PowerStoneItem(Properties properties) {
        super(properties);
    }

    @Override public String getKey() { return "power"; }
    @Override public int getColor() { return 0xA000FF; }
}
