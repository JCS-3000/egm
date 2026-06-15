package org.jcs.egm.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import org.jcs.egm.stones.stone_soul.SoulStoneItem;

public class SoulStoneRezResetCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("soul_stone_rez_energy_refill")
                        .requires(source -> source.hasPermission(2)) // OP-only
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            SoulStoneItem.refillResurrectionEnergy(player.getUUID());
                            player.sendSystemMessage(Component.literal("Soul Stone resurrection energy refilled")
                                    .withStyle(Style.EMPTY.withItalic(true)));
                            return 1;
                        })
        );
    }
}
