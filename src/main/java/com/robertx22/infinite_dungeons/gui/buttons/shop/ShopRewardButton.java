package com.robertx22.infinite_dungeons.gui.buttons.shop;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.robertx22.infinite_dungeons.components.PlayerIDCap;
import com.robertx22.infinite_dungeons.database.db_types.layout.ShopEntry;
import com.robertx22.infinite_dungeons.main.InfiniteDungeonsMain;
import com.robertx22.infinite_dungeons.packets.BuyPacket;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.library_of_exile.utils.GuiUtils;
import com.robertx22.library_of_exile.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class ShopRewardButton extends ImageButton {

    public static int SIZE_X = 24;
    public static int SIZE_Y = 24;

    static ResourceLocation ID = InfiniteDungeonsMain.id("textures/gui/shop_reward_button.png");

    Minecraft mc = Minecraft.getInstance();

    ShopEntry entry;

    public boolean renderBackground = true;

    public ShopRewardButton(int index, ShopEntry entry, int xPos, int yPos) {
        super(xPos, yPos, SIZE_X, SIZE_Y + 1, 0, 0, SIZE_Y, ID, (button) -> {
            Packets.sendToServer(new BuyPacket(index));
        });
        this.entry = entry;
    }

    public ShopRewardButton setRenderBackground(boolean renderBackground) {
        this.renderBackground = renderBackground;
        return this;
    }

    @Override
    public void renderToolTip(MatrixStack matrix, int x, int y) {
        if (isInside(x, y)) {
            List<ITextComponent> tooltip = new ArrayList<>();
            tooltip.addAll(entry.reward.type.getStackToShow(entry.reward)
                .getTooltipLines(mc.player, ITooltipFlag.TooltipFlags.NORMAL));

            int left = entry.max_uses - PlayerIDCap.get(mc.player).data.buy_history.getOrCreateHistoryOf(entry).amount;

            tooltip.add(new StringTextComponent("Buys Left: " + left + "/" + this.entry.max_uses));

            if (left == 0) {
                tooltip.add(new StringTextComponent(TextFormatting.RED + "Can't buy anymore."));
            }

            GuiUtils.renderTooltip(matrix, tooltip, x, y);
        }
    }

    public boolean isInside(int x, int y) {
        return GuiUtils.isInRect(this.x, this.y, SIZE_X, SIZE_Y, x, y);
    }

    @Override
    public void renderButton(MatrixStack matrix, int x, int y, float f) {
        if (renderBackground) {
            super.renderButton(matrix, x, y, f);
        }
        RenderUtils.renderStack(entry.reward.type.getStackToShow(entry.reward), this.x + 4, this.y + 4);
    }

}


