package mspets.my.zoomod.datagen;

import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.setup.registries.ItemRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGen extends ItemModelProvider
{
    public ItemModelGen(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper)
    {
        super(dataGenerator, MyZooMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        withExistingParent(ItemRegistries.FEEDINGTROUGH_ITEM.get().getRegistryName().getPath(), modLoc("block/feedingtrough"));
        withExistingParent(ItemRegistries.CROCODILE_EGG.get().getRegistryName().getPath(), "item/template_spawn_egg");
    }
}
