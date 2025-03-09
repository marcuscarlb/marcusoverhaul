package net.marcus.overhaul.util.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.marcus.overhaul.MarcusMinecraftOverhaul;
import net.marcus.overhaul.util.networking.KnappingPayload;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.function.Function;

public class KnappingScreen extends HandledScreen<KnappingScreenHandler> {
    private static final Identifier KNAPPING = new Identifier(MarcusMinecraftOverhaul.MOD_ID, "textures/gui/container/knapping.png");
    private static final Identifier KNAPPINGSTONE = new Identifier(MarcusMinecraftOverhaul.MOD_ID, "textures/gui/container/knappingstone.png");
    private boolean isMouseHeld = false;

    public KnappingScreen(KnappingScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 256;
        this.backgroundHeight = 256;
        this.playerInventoryTitleY = this.backgroundHeight - 120;
        this.playerInventoryTitleX = this.backgroundWidth - 220;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f,  1f, 1f);
        RenderSystem.setShaderTexture(0, KNAPPING);
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        context.drawTexture(KNAPPING, x, y, 0, 0, backgroundWidth, backgroundHeight);

        boolean[][] grid = handler.getGrid();
        for (int row = 0; row <5; ++row) {
            for (int col = 0; col < 5; ++col) {
                if (!grid[row][col]) {
                    context.drawTexture(KNAPPINGSTONE, x + 51 + col * 16, y + 52 + row * 16, 176, 0, 16, 16);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        isMouseHeld = true;
        if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
            handleGridClick(mouseX, mouseY);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isMouseHeld = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isMouseHeld) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
                handleGridClick(mouseX, mouseY);
            }

        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    public void handleGridClick(double mouseX, double mouseY) {
        // Handle clicks on the knapping grid
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        int gridStartX = x + 51;
        int gridStartY = y + 52;
        int gridEndX = x + 131;
        int gridEndY = y + 132;

        if (mouseX >= gridStartX && mouseX < gridEndX && mouseY >= gridStartY && mouseY < gridEndY) {
            for (int row = 0; row < 5; ++row) {
                for (int col = 0; col < 5; ++col) {
                    int gridX = gridStartX + col * 16;
                    int gridY = gridStartY + row * 16;
                    if (mouseX >= gridX && mouseX < gridX + 16 && mouseY >= gridY && mouseY < gridY + 16) {
                        boolean[][] grid = handler.getGrid();
                        if (!grid[row][col]) {
                            grid[row][col] = !grid[row][col];
                            this.client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_STONE_HIT, 1.0F));
                            this.handler.setGrid(row, col, grid[row][col]);
                        }
                        ClientPlayNetworking.send(new KnappingPayload(row, col));
                    }
                }
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }
}

