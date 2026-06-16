package org.jcs.egm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jcs.egm.gauntlet.InfinityGauntletItem;
import org.jcs.egm.network.NetworkHandler;
import org.jcs.egm.network.SetAbilityIndexPacket;
import org.jcs.egm.stones.StoneContainer;
import org.lwjgl.glfw.GLFW;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class StoneAbilityMenuScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.parse("egm:textures/gui/stoneabilitygui.png");
    private static final ResourceLocation ARROW_TEX =
            ResourceLocation.parse("egm:textures/gui/arrow.png");

    private final ItemStack stoneStack;
    private final InteractionHand hand;
    private final List<Component> abilityNames;
    private int selectedIndex;
    private int hoverIndex;
    private final int menuWidth = 176;
    private final int menuHeight = 107;
    private int guiLeft;
    private int guiTop;

    // Scrolling
    private static final int VISIBLE_ENTRIES = 4;
    private int scrollOffset = 0;

    public StoneAbilityMenuScreen(ItemStack stoneStack, InteractionHand hand, List<Component> abilityNames, int selectedIndex) {
        super(Component.literal("Select Ability"));
        this.stoneStack = stoneStack;
        this.hand = hand;
        this.abilityNames = abilityNames;
        this.selectedIndex = normalizeIndex(selectedIndex);
        this.hoverIndex = this.selectedIndex;
    }

    @Override
    protected void init() {
        this.guiLeft = (this.width - menuWidth) / 2;
        this.guiTop = (this.height - menuHeight) / 2;
        updateScrollOffset();
        super.init();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);

        RenderSystem.setShaderTexture(0, TEXTURE);
        graphics.blit(TEXTURE, guiLeft, guiTop, 0, 0, menuWidth, menuHeight);

        // Draw the title at the fixed position (do not change this Y)
        graphics.drawCenteredString(this.font, this.title, this.width / 2, guiTop + 10, 0xFFFFFF);

        // Spacing setup
        int entryHeight = 18;
        int startY = guiTop + 34; // Leaves room for the title

        // Calculate visible abilities (scrolling)
        int total = abilityNames.size();
        int firstIdx = scrollOffset;
        int lastIdx = Math.min(firstIdx + VISIBLE_ENTRIES, total);

        for (int i = firstIdx; i < lastIdx; i++) {
            int row = i - firstIdx;
            int y = startY + row * entryHeight;

            if (i == hoverIndex) {
                RenderSystem.setShaderTexture(0, ARROW_TEX);
                graphics.blit(ARROW_TEX,
                        this.width / 2 - 55, // X position
                        y - 2,   // Y position
                        0, 0,
                        12, 12,
                        12, 12);
            }

            int color = i == selectedIndex ? 0xFFE6A3 : i == hoverIndex ? 0xFFFFFF : 0xCCCCCC;
            graphics.drawCenteredString(this.font, abilityNames.get(i), this.width / 2, y, color);
        }

        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    private void updateScrollOffset() {
        // Ensure hovered entry is visible if there are more than 4 abilities
        if (abilityNames.size() <= VISIBLE_ENTRIES) {
            scrollOffset = 0;
        } else {
            if (hoverIndex < scrollOffset) {
                scrollOffset = hoverIndex;
            } else if (hoverIndex >= scrollOffset + VISIBLE_ENTRIES) {
                scrollOffset = hoverIndex - VISIBLE_ENTRIES + 1;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int size = abilityNames.size();
        if (size > 0) {
            if (delta < 0) {
                hoverIndex = (hoverIndex + 1) % size;
            } else if (delta > 0) {
                hoverIndex = (hoverIndex - 1 + size) % size;
            }
            updateScrollOffset();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            commitHoveredSelection();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            int size = abilityNames.size();
            if (size == 0) return true;
            hoverIndex = (hoverIndex + 1) % size;
            updateScrollOffset();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_UP) {
            int size = abilityNames.size();
            if (size == 0) return true;
            hoverIndex = (hoverIndex - 1 + size) % size;
            updateScrollOffset();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER || keyCode == GLFW.GLFW_KEY_SPACE) {
            commitHoveredSelection();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    private void commitHoveredSelection() {
        selectedIndex = hoverIndex;
        saveSelection();
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.connection != null) {
            NetworkHandler.INSTANCE.sendToServer(new SetAbilityIndexPacket(selectedIndex, hand));
        }
    }

    private void saveSelection() {
        // Always try to set the AbilityIndex on the *stone* inside the holder or gauntlet
        if (StoneContainer.isHolderLike(stoneStack)) {
            ItemStack inside = StoneContainer.getSingleContainedStone(stoneStack);
            if (!inside.isEmpty()) {
                inside.getOrCreateTag().putInt("AbilityIndex", selectedIndex);
                StoneContainer.setSingleContainedStone(stoneStack, inside);
            }
        } else if (stoneStack.getItem() instanceof InfinityGauntletItem) {
            int idx = InfinityGauntletItem.getSelectedStone(stoneStack);
            ItemStackHandler handler = new ItemStackHandler(6);
            if (stoneStack.hasTag() && stoneStack.getTag().contains("Stones")) {
                handler.deserializeNBT(stoneStack.getTag().getCompound("Stones"));
            }
            ItemStack innerStone = handler.getStackInSlot(idx);
            if (!innerStone.isEmpty()) {
                innerStone.getOrCreateTag().putInt("AbilityIndex", selectedIndex);
                handler.setStackInSlot(idx, innerStone);
                stoneStack.getTag().put("Stones", handler.serializeNBT());
            }
        } else {
            // Raw stone or other
            if (stoneStack != null) {
                stoneStack.getOrCreateTag().putInt("AbilityIndex", selectedIndex);
            }
        }
    }

    private int normalizeIndex(int index) {
        return abilityNames.isEmpty() || index < 0 || index >= abilityNames.size() ? 0 : index;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
