package mspets.my.zoomod.setup;

import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.client.entities.models.CrocodileModel;
import mspets.my.zoomod.client.entities.renderers.CrocodileRenderer;
import mspets.my.zoomod.client.screens.FeedingTroughScreen;
import mspets.my.zoomod.setup.registries.BlockRegistries;
import mspets.my.zoomod.setup.registries.EntityRegistries;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(modid = MyZooMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
    public static void init(final FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            MenuScreens.register(BlockRegistries.FEEDING_TROUGH_CONTAINER.get(), FeedingTroughScreen::new);
            //ItemBlockRenderTypes.setRenderLayer(BlockRegistries.FEEDING_TROUGH.get(), RenderType.translucent());
        });
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(CrocodileModel.LAYER_LOCATION, CrocodileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(EntityRegistries.CROCODILE.get(), CrocodileRenderer::new);
    }
}
