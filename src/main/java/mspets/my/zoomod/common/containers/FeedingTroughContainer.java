package mspets.my.zoomod.common.containers;

import mspets.my.zoomod.setup.registries.BlockRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import static mspets.my.zoomod.common.blockentities.FeedingTroughBE.isMyItemValid;

public class FeedingTroughContainer extends AbstractContainerMenu
{
    private BlockEntity blockEntity;
    private Player playerEntity;
    private IItemHandler playerInventory;

    // TODO FIX ASAP
//    private static final TagKey<Item> wheat = ItemTags.create(new ResourceLocation("forge", "wheat"));

    public FeedingTroughContainer(int windowId, BlockPos pos, Inventory playerInventory, Player player)
    {
        super(BlockRegistries.FEEDING_TROUGH_CONTAINER.get(), windowId);
        this.blockEntity = player.getCommandSenderWorld().getBlockEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);

        if (blockEntity != null)
        {
            blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                int x = 44;
                int y = 36;
                for (int i = 0; i < 5; i++)
                {
                    addSlot(new SlotItemHandler(h, i, x, y));
                    x += 18;
                }
            });
        }
        layoutPlayerInventorySlots(8, 86);
    }

    @Override
    public boolean stillValid(Player playerIn)
    {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, BlockRegistries.FEEDING_TROUGH.get());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index)
    {
        // TODO MAKE THIS WORK https://pastebin.com/jEuBxZJC
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        int conSize = 5;
        if (slot.hasItem())
        {
            ItemStack stack = slot.getItem();
            itemStack = stack.copy();

            // if in container
            if (index < conSize)
            {
                if (!this.moveItemStackTo(stack, conSize, this.slots.size(), false))
                {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemStack);
            }
            else
            {
                // if in inv and wheat
                if (isMyItemValid(stack))
                {
                    if (!this.moveItemStackTo(stack, 0, conSize, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                // Standard player slots to hotbar
                else if (index < 28)
                {
                    if (!this.moveItemStackTo(stack, 28, 37, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index < 37 && !this.moveItemStackTo(stack, 1, 28, false))
                {
                    return itemStack.EMPTY;
                }
            }
            if (stack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }
            if (stack.getCount() == itemStack.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, stack);
        }
        return itemStack;
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx)
    {
        for (int i = 0; i < amount; i++)
        {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy)
    {
        for (int j = 0; j < verAmount; j++)
        {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow)
    {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
}
