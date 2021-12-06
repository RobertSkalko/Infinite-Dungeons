package com.robertx22.infinite_dungeons.gui.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.robertx22.infinite_dungeons.gui.PickDungeonScreen;
import com.robertx22.infinite_dungeons.main.MainID;
import com.robertx22.infinite_dungeons.packets.StartDungeonPacket;
import com.robertx22.library_of_exile.main.Packets;
import com.robertx22.library_of_exile.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class StartDungeonButton extends ImageButton {

    public static int SIZE_X = 80;
    public static int SIZE_Y = 16;

    static ResourceLocation ID = MainID.id("textures/gui/start_dungeon.png");

    Minecraft mc = Minecraft.getInstance();

    PickDungeonScreen screen;

    public StartDungeonButton(PickDungeonScreen screen, int xPos, int yPos) {
        super(xPos, yPos, SIZE_X, SIZE_Y, 0, 0, SIZE_Y, ID, (button) -> {
            Packets.sendToServer(new StartDungeonPacket(screen.currentLayout.GUID(), screen.difficulty.GUID()));
            screen.onClose();
        });
        this.screen = screen;

    }

    @Override
    public void renderToolTip(MatrixStack matrix, int x, int y) {
        if (isInside(x, y)) {
            List<ITextComponent> tooltip = new ArrayList<>();

            GuiUtils.renderTooltip(matrix, tooltip, x, y);

        }
    }

    public boolean isInside(int x, int y) {
        return GuiUtils.isInRect(this.x, this.y, SIZE_X, SIZE_Y, x, y);
    }

    @Override
    public void renderButton(MatrixStack matrix, int x, int y, float f) {

        super.renderButton(matrix, x, y, f);

        String txt = "Start Dungeon";

        mc.font.drawShadow(matrix, txt, this.x + (SIZE_X / 2F - (mc.font.width(txt) / 2F)), this.y + 3, TextFormatting.WHITE.getColor());
    }

}
