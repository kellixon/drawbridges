package com.msvdaamen.inventory;


import com.msvdaamen.tileentities.TileEntityAdvDrawbridge;
import com.msvdaamen.tileentities.TileEntityDrawbridge;
import com.msvdaamen.tileentities.TileEntityPulseDrawbridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class AdvDrawbridgeContainer extends Container {

    private TileEntityAdvDrawbridge te;

    public AdvDrawbridgeContainer(IInventory playerInventory, TileEntityAdvDrawbridge te) {
        this.te = te;
        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
//        addSlotToContainer(new SlotItemHandler(itemHandler, 0, 13, 12));
        int index = 1;
        for(int  b = 0; b < 2; b++) {
            for(int  i = 0; i < 8; i++) {
                addSlotToContainer(new SlotItemHandler(itemHandler,  index, 25 + i * 18, 43 + b * 18));
                index++;
            }
        }
    }


    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 18 + 124;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < TileEntityAdvDrawbridge.SIZE) {
                if (!this.mergeItemStack(itemstack1, TileEntityAdvDrawbridge.SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, TileEntityAdvDrawbridge.SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return te.canInteractWith(player);
    }
}
