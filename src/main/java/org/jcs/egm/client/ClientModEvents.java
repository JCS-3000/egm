package org.jcs.egm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jcs.egm.client.input.InfinityKeybinds;
import org.jcs.egm.client.particle.UniversalTintParticle;
import org.jcs.egm.egm;
import org.jcs.egm.gauntlet.InfinityGauntletItem;
import org.jcs.egm.network.NetworkHandler;
import org.jcs.egm.network.OpenGauntletMenuPacket;
import org.jcs.egm.network.OpenStoneHolderMenuPacket;
import org.jcs.egm.registry.ModItems;
import org.jcs.egm.stones.StoneItem;
import org.jcs.egm.registry.ModEffects;
import org.jcs.egm.stones.StoneAbilityRegistries;
import org.jcs.egm.stones.StoneContainer;
import org.jcs.egm.stones.stone_power.EmpoweredPunchPowerStoneAbility;

@Mod.EventBusSubscriber(modid = "egm", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientModEvents {

    private static int particleTick = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                ItemStack mainHand = mc.player.getMainHandItem();
                
                // Check for empowered punch particles (every 2 ticks)
                particleTick++;
                if (particleTick % 2 == 0) {
                    if (mc.player.hasEffect(ModEffects.EMPOWERED_PUNCH.get())) {
                        // Show empowered punch effect particles around the player
                        var empoweredPunch = new EmpoweredPunchPowerStoneAbility();
                        empoweredPunch.spawnChargedParticlesPublic(mc.level, mc.player);
                    }
                }
                
                if (InfinityKeybinds.OPEN_STONE_MENU.consumeClick()) {
                    if (!mainHand.isEmpty()) {
                        if (mainHand.getItem() == ModItems.INFINITY_GAUNTLET.get()) {
                            NetworkHandler.INSTANCE.sendToServer(new OpenGauntletMenuPacket());
                        } else if (StoneContainer.isHolderLike(mainHand)) {
                            NetworkHandler.INSTANCE.sendToServer(new OpenStoneHolderMenuPacket());
                        }
                    }
                }
            }
        }
        if (InfinityKeybinds.OPEN_ABILITY_MENU.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                ItemStack stack = mc.player.getMainHandItem();
                if (stack.getItem() instanceof StoneItem stoneItem) {
                    openStoneAbilityMenu(stack, InteractionHand.MAIN_HAND, stoneItem.getKey());
                } else if (StoneContainer.isHolderLike(stack)) {
                    openHolderAbilityMenu(stack, StoneContainer.getSingleStoneKey(stack));
                } else if (stack.getItem() instanceof InfinityGauntletItem) {
                    if (InfinityGauntletItem.getSelectedStone(stack) == InfinityGauntletItem.SNAP_SELECTION) return;
                    ItemStack selectedStone = InfinityGauntletItem.getStoneStack(stack, InfinityGauntletItem.getSelectedStone(stack));
                    if (selectedStone.isEmpty()) return;
                    String stoneKey = InfinityGauntletItem.getSelectedStoneName(stack);
                    openStoneAbilityMenu(stack, selectedStone, InteractionHand.MAIN_HAND, stoneKey);
                }
            }
        }
    }

    private static void openHolderAbilityMenu(ItemStack holderStack, String stoneKey) {
        ItemStack contained = StoneContainer.getSingleContainedStone(holderStack);
        if (contained.isEmpty()) return;
        openStoneAbilityMenu(holderStack, contained, InteractionHand.MAIN_HAND, stoneKey);
    }

    private static void openStoneAbilityMenu(ItemStack stack, InteractionHand hand, String stoneKey) {
        openStoneAbilityMenu(stack, stack, hand, stoneKey);
    }

    private static void openStoneAbilityMenu(ItemStack stack, ItemStack selectedAbilityStack, InteractionHand hand, String stoneKey) {
        Minecraft mc = Minecraft.getInstance();
        var names = StoneAbilityRegistries.getAbilityNames(stoneKey);
        if (names.isEmpty()) return;
        int idx = selectedAbilityStack.hasTag() ? selectedAbilityStack.getTag().getInt("AbilityIndex") : 0;
        if (!StoneAbilityRegistries.isValidAbilityIndex(stoneKey, idx)) idx = 0;
        mc.setScreen(new StoneAbilityMenuScreen(stack, hand, names, idx));
    }
}

