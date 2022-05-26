package mspets.my.zoomod.datagen;

import mspets.my.zoomod.MyZooMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsGen extends BlockTagsProvider
{
    public BlockTagsGen(DataGenerator dataGenerator, ExistingFileHelper helper)
    {
        super(dataGenerator, MyZooMod.MODID, helper);
    }

    @Override
    protected void addTags()
    {
        //tag(BlockTags.M);
    }

    @Override
    public String getName()
    {
        return "My Zoo Mod Tags";
    }
}
