package mspets.my.zoomod.datagen;

import mspets.my.zoomod.MyZooMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagsGen extends ItemTagsProvider
{
    public ItemTagsGen(DataGenerator dataGenerator, BlockTagsGen blockTagsGen, ExistingFileHelper existingFileHelper)
    {
        super(dataGenerator, blockTagsGen, MyZooMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {

    }

    @Override
    public String getName()
    {
        return "My Zoo Mod Tags";
    }
}
