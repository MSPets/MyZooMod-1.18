package mspets.my.zoomod.setup;

import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.common.entities.CrocodileEntity;
import mspets.my.zoomod.setup.registries.BlockRegistries;
import mspets.my.zoomod.setup.registries.EntityRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static mspets.my.zoomod.MyZooMod.LOGGER;

@Mod.EventBusSubscriber(modid = MyZooMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup
{
    public static final String CREATIVE_TAB_NAME = "myzoomod";

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(CREATIVE_TAB_NAME)
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(Items.DIAMOND);
        }
    };

    public static void setup()
    {

    }

    public static void init(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
    {
        // Register a new block here
        LOGGER.info("HELLO from Register Block");
        LOGGER.info("FEEDINGTROUGH BLOCK >> {}", BlockRegistries.FEEDING_TROUGH.get().getRegistryName());
    }

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event)
    {
        event.put(EntityRegistries.CROCODILE.get(), CrocodileEntity.prepareAttributes().build());
    }
}
