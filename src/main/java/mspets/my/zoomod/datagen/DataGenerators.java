package mspets.my.zoomod.datagen;

import mspets.my.zoomod.MyZooMod;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = MyZooMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
    @SubscribeEvent
    public static void generateData(GatherDataEvent event)
    {
        DataGenerator dataGenerator = event.getGenerator();
        if (event.includeServer())
        {
            dataGenerator.addProvider(new RecipeGen(dataGenerator));
            dataGenerator.addProvider(new LootTablesGen(dataGenerator));
            BlockTagsGen blockTagsGen = new BlockTagsGen(dataGenerator, event.getExistingFileHelper());
            dataGenerator.addProvider(blockTagsGen);
            dataGenerator.addProvider(new ItemTagsGen(dataGenerator, blockTagsGen, event.getExistingFileHelper()));
        }
        if (event.includeClient())
        {
            dataGenerator.addProvider(new BlockStatesGen(dataGenerator, event.getExistingFileHelper()));
            dataGenerator.addProvider(new ItemModelGen(dataGenerator, event.getExistingFileHelper()));
            dataGenerator.addProvider(new LanguageProviderGen(dataGenerator, "en_us"));
        }
    }
}
