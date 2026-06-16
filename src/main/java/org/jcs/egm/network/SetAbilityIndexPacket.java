package org.jcs.egm.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;
import org.jcs.egm.gauntlet.InfinityGauntletItem;
import org.jcs.egm.stones.StoneAbilityRegistries;
import org.jcs.egm.stones.StoneContainer;
import org.jcs.egm.stones.StoneItem;

import java.util.function.Supplier;

public class SetAbilityIndexPacket {
    private final int index;
    private final InteractionHand hand;

    public SetAbilityIndexPacket(int index, InteractionHand hand) {
        this.index = index;
        this.hand = hand;
    }

    public SetAbilityIndexPacket(FriendlyByteBuf buf) {
        this.index = buf.readVarInt();
        this.hand = buf.readEnum(InteractionHand.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(index);
        buf.writeEnum(hand);
    }

    public static SetAbilityIndexPacket decode(FriendlyByteBuf buf) {
        return new SetAbilityIndexPacket(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.isEmpty()) {
                // For single-stone holders
                if (StoneContainer.isHolderLike(stack)) {
                    String stoneKey = StoneContainer.getSingleStoneKey(stack);
                    if (!StoneAbilityRegistries.isValidAbilityIndex(stoneKey, index)) return;
                    ItemStack inside = StoneContainer.getSingleContainedStone(stack);
                    if (!inside.isEmpty()) {
                        inside.getOrCreateTag().putInt("AbilityIndex", index);
                        StoneContainer.setSingleContainedStone(stack, inside);
                    }
                }
                // For Gauntlet
                else if (stack.getItem() instanceof InfinityGauntletItem) {
                    int stoneIdx = InfinityGauntletItem.getSelectedStone(stack);
                    if (stoneIdx < 0 || stoneIdx >= 6) return;
                    String stoneKey = InfinityGauntletItem.getSelectedStoneName(stack);
                    if (!StoneAbilityRegistries.isValidAbilityIndex(stoneKey, index)) return;
                    ItemStackHandler handler = new ItemStackHandler(6);
                    if (stack.hasTag() && stack.getTag().contains("Stones")) {
                        handler.deserializeNBT(stack.getTag().getCompound("Stones"));
                    }
                    ItemStack stoneStack = handler.getStackInSlot(stoneIdx);
                    if (!stoneStack.isEmpty()) {
                        stoneStack.getOrCreateTag().putInt("AbilityIndex", index);
                        handler.setStackInSlot(stoneIdx, stoneStack);
                        // Save handler back to gauntlet
                        stack.getTag().put("Stones", handler.serializeNBT());
                    }
                }
                // For raw stone or fallback
                else if (stack.getItem() instanceof StoneItem stoneItem
                        && StoneAbilityRegistries.isValidAbilityIndex(stoneItem.getKey(), index)) {
                    stack.getOrCreateTag().putInt("AbilityIndex", index);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
