package mspets.my.zoomod.datagen;

import net.minecraft.data.DataGenerator;

public class LootTablesGen extends BaseLootTableProvider
{
    public LootTablesGen(DataGenerator dataGenerator)
    {
        super(dataGenerator);
    }

    @Override
    protected void addTables()
    {
        /*
        lootTables.put(BlockRegistries.FEEDING_TROUGH.get(),
                createStandardTable("feedingtrough",
                        BlockRegistries.FEEDING_TROUGH.get(),
                        BlockRegistries.FEEDINGTROUGH_BE.get()));
         */
    }
}
