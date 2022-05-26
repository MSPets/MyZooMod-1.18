package mspets.my.zoomod.setup.registries;

import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.common.blockentities.FeedingTroughBE;
import mspets.my.zoomod.common.blocks.FeedingTroughBlock;
import mspets.my.zoomod.common.containers.FeedingTroughContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistries
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MyZooMod.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MyZooMod.MODID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MyZooMod.MODID);

    public static final RegistryObject<FeedingTroughBlock> FEEDING_TROUGH = BLOCKS.register("feedingtrough", FeedingTroughBlock::new);
    public static final RegistryObject<BlockEntityType<FeedingTroughBE>> FEEDING_TROUGH_BE = BLOCK_ENTITIES.register(
            "feedingtrough_be", () -> BlockEntityType.Builder.of(FeedingTroughBE::new, FEEDING_TROUGH.get()).build(null));
    public static final RegistryObject<MenuType<FeedingTroughContainer>> FEEDING_TROUGH_CONTAINER = CONTAINERS.register(
            "feedingtrough_con", () -> IForgeMenuType.create((windowId, inv, data) -> new FeedingTroughContainer(windowId, data.readBlockPos(), inv, inv.player)));
}
