package mspets.my.zoomod.setup.registries;

import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.setup.CommonSetup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistries
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MyZooMod.MODID);
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(CommonSetup.ITEM_GROUP);
    public static final RegistryObject<Item> FEEDINGTROUGH_ITEM = fromBlock(BlockRegistries.FEEDING_TROUGH);

    public static final RegistryObject<Item> CROCODILE_EGG = ITEMS.register("crocodile", () -> new ForgeSpawnEggItem(EntityRegistries.CROCODILE, 0xff0000, 0x00ff00, ITEM_PROPERTIES));

    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block)
    {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }
}
