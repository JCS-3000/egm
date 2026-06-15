package org.jcs.egm.stones.stone_time;

import org.jcs.egm.stones.StoneItem;

public class TimeStoneItem extends StoneItem {
    public TimeStoneItem(Properties properties) {
        super(properties);
    }

    @Override public String getKey() { return "time"; }
    @Override public int getColor() { return 0x00FF66; }
}
