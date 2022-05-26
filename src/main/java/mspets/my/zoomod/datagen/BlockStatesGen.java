package mspets.my.zoomod.datagen;

import mspets.my.zoomod.MyZooMod;
import mspets.my.zoomod.setup.registries.BlockRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesGen extends BlockStateProvider
{
    public BlockStatesGen(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen, MyZooMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        feedingTrough();
    }

    private void feedingTrough()
    {
        BlockModelBuilder frame = models().getBuilder("block/feedingtrough");

        frame.parent(models().getExistingFile(mcLoc("block/cube_bottom_top")));

        frame.texture("top", "block/feedingtrough_top");
        frame.texture("bottom", "block/feedingtrough_bottom");
        frame.texture("side", "block/feedingtrough_side");

        /*
        frame.element().face(Direction.DOWN).texture("#bottom");
        frame.element().face(Direction.UP).texture("#top");
        frame.element().face(Direction.NORTH).texture("#wall");
        frame.element().face(Direction.EAST).texture("#wall");
        frame.element().face(Direction.SOUTH).texture("#wall");
        frame.element().face(Direction.WEST).texture("#wall");
         */

        MultiPartBlockStateBuilder bld = getMultipartBuilder(BlockRegistries.FEEDING_TROUGH.get());
        bld.part().modelFile(frame).addModel();

    }
}
