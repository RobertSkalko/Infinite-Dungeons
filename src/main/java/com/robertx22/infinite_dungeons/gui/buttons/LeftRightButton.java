package com.robertx22.infinite_dungeons.gui.buttons;

import com.robertx22.infinite_dungeons.main.InfiniteDungeonsMain;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;

public class LeftRightButton extends ImageButton {

    public static ResourceLocation TEX = InfiniteDungeonsMain.id("textures/gui/leftright.png");

    public static int xSize = 20;
    public static int ySize = 20;

    public LeftRightButton(ILeftRight screen, int xPos, int yPos, boolean isLeft) {
        super(xPos, yPos, xSize, ySize, isLeft ? 0 : 20, 0, 0, TEX, (button) -> {
            int times = 1;
            for (int i = 0; i < times; i++) {
                if (isLeft) {
                    screen.goLeft();
                } else {
                    screen.goRight();
                }
            }
        });
    }

}
