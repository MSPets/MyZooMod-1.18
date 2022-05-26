package mspets.my.zoomod.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.common.containers.FeedingTroughContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FeedingTroughScreen extends AbstractContainerScreen<FeedingTroughContainer>
{
    private final ResourceLocation GUI = new ResourceLocation(MyZooMod.MODID, "textures/gui/feedingtrough_gui.png");

    public FeedingTroughScreen(FeedingTroughContainer container, Inventory inv, Component name)
    {
        super(container, inv, name);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.setShaderTexture(0, GUI);
        int relx = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, relx, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
