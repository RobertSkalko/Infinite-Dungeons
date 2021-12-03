package com.robertx22.infinite_dungeons.gui.buttons.shop;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.robertx22.infinite_dungeons.database.db_types.layout.ShopEntry;
import com.robertx22.infinite_dungeons.main.InfiniteDungeonsMain;
import com.robertx22.library_of_exile.utils.GuiUtils;
import com.robertx22.library_of_exile.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class ShopCostButton extends ImageButton {

    public static int SIZE_X = 24;
    public static int SIZE_Y = 24;

    static ResourceLocation ID = InfiniteDungeonsMain.id("textures/gui/shop_cost_button.png");

    Minecraft mc = Minecraft.getInstance();

    ShopEntry entry;

    public ShopCostButton(ShopEntry entry, int xPos, int yPos) {
        super(xPos, yPos, SIZE_X, SIZE_Y + 1, 0, 0, SIZE_Y, ID, (button) -> {
            // Packets.sendToServer(new StartDungeonPacket(screen.currentLayout.GUID(), screen.difficulty.GUID()));
        });
        this.entry = entry;
    }

    @Override
    public void renderToolTip(MatrixStack matrix, int x, int y) {
        if (isInside(x, y)) {
            List<ITextComponent> tooltip = new ArrayList<>();
            tooltip.addAll(entry.cost.getShowStack()
                .getTooltipLines(mc.player, ITooltipFlag.TooltipFlags.NORMAL));
            GuiUtils.renderTooltip(matrix, tooltip, x, y);
        }
    }

    public boolean isInside(int x, int y) {
        return GuiUtils.isInRect(this.x, this.y, SIZE_X, SIZE_Y, x, y);
    }

    @Override
    public void renderButton(MatrixStack matrix, int x, int y, float f) {
        super.renderButton(matrix, x, y, f);
        RenderUtils.renderStack(this.entry.cost.getShowStack(), this.x + 4, this.y + 4);
    }

}

