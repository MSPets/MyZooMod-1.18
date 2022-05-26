package mspets.my.zoomod.datagen;

import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.setup.registries.BlockRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import static mspets.my.zoomod.common.blocks.FeedingTroughBlock.SCREEN_MYZOOMOD_FEEDINGTROUGH;

public class LanguageProviderGen extends LanguageProvider
{
    public LanguageProviderGen(DataGenerator dataGenerator, String en_us)
    {
        super(dataGenerator, MyZooMod.MODID, en_us);
    }

    @Override
    protected void addTranslations()
    {
        add(SCREEN_MYZOOMOD_FEEDINGTROUGH, "Feeding Trough");
        add(BlockRegistries.FEEDING_TROUGH.get(), "Feeding Trough");
    }
}
