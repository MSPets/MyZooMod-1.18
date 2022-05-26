package mspets.my.zoomod.common.blockentities;

import mspets.my.zoomod.setup.registries.BlockRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FeedingTroughBE extends BlockEntity
{

    // Never create lazy optionals in getCapability. Always place them as fields in the tile entity:
    // Internal usage
    private final ItemStackHandler itemStackHandler = createHandler();
    // External usage
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemStackHandler);

    public FeedingTroughBE(BlockPos pos, BlockState state)
    {
        super(BlockRegistries.FEEDING_TROUGH_BE.get(), pos, state);
    }

    @Override
    public void setRemoved()
    {
        super.setRemoved();
        handler.invalidate();
    }

    @Override
    public void load(CompoundTag tag)
    {
        if (tag.contains("Inventory"))
        {
            itemStackHandler.deserializeNBT(tag.getCompound("Inventory"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        tag.put("Inventory", itemStackHandler.serializeNBT());
    }

    private ItemStackHandler createHandler()
    {
        // TODO FIX THIS ASAP
        return new ItemStackHandler(6)
        {
            //private static final TagKey<Item> wheat = ItemTags.create(new ResourceLocation("forge", "wheat"));

            @Override
            protected void onContentsChanged(int slot)
            {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack)
            {
                return isMyItemValid(stack);
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate)
            {
                if (!isItemValid(slot, stack))
                {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public int getSlotLimit(int slot)
            {
                return 64;
            }
        };
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    public static boolean isMyItemValid(ItemStack stack)
    {
        if (stack.is(Items.WHEAT)
                || stack.is(Items.APPLE))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}


