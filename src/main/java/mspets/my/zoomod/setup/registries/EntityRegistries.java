package mspets.my.zoomod.setup.registries;

import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.common.entities.CrocodileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistries
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITIES, MyZooMod.MODID);

    public static final RegistryObject<EntityType<CrocodileEntity>> CROCODILE = ENTITY_TYPE.register("crocodile", () -> EntityType.Builder.of(CrocodileEntity::new, MobCategory.CREATURE)
            .sized(3f, 1f)
            .clientTrackingRange(8)
            .setShouldReceiveVelocityUpdates(false)
            .build("crocodile"));
}
