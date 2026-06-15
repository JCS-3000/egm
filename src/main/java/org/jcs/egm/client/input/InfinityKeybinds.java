package org.jcs.egm.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.jcs.egm.egm;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = egm.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InfinityKeybinds {
    public static final KeyMapping OPEN_STONE_MENU = new KeyMapping(
            "key.egm.open_stone_menu",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "key.categories.inventory"
    );

    public static final KeyMapping OPEN_ABILITY_MENU = new KeyMapping(
            "key.egm.open_ability_menu",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.inventory"
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_STONE_MENU);
        event.register(OPEN_ABILITY_MENU);
    }
}
