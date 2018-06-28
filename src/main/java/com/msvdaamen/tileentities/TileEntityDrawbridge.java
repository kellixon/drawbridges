package com.msvdaamen.tileentities;

import com.msvdaamen.blocks.Drawbridge;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityDrawbridge extends TileEntityBase implements ITickable {

    public static final int SIZE = 3;
    private int placementDuration = 10;
    private int timer = 0;

    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {

            if(slot == 1) {
                if(itemStackHandler.getStackInSlot(slot).getItem() instanceof ItemBlock) {

                }
                getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
            }

            TileEntityDrawbridge.this.markDirty();
        }

        @Override
        public int getSlotLimit(int slot) {
            if(slot == 1) {
                return 1;
            }
            return 16;
        }
    };

    @Override
    public void update() {
        if(!getWorld().isRemote) {
            if(getWorld().isBlockPowered(getPos())) {
                placeBlocksFacing();
            }else {
                removeBlocksFacing();
            }
        }
    }

    public void removeBlocksFacing() {
        if(startOfRetraction() > 0) {
            timer++;
            if(timer >= placementDuration) {
                EnumFacing facing = getWorld().getBlockState(getPos()).getValue(Drawbridge.FACING);
                if(itemStackHandler.getStackInSlot(0).getCount() != itemStackHandler.getSlotLimit(0)) {
                    if(ItemStack.areItemsEqual(itemStackHandler.getStackInSlot(2), getItemstackWithMeta((getBlockStateAtPos(getPos().offset(facing, startOfRetraction())))))) {
                        if(itemStackHandler.getStackInSlot(0).isEmpty()) {
                            itemStackHandler.setStackInSlot(0, getItemstackWithMeta((getBlockStateAtPos(getPos().offset(facing, startOfRetraction())))));
                        }else {
                            itemStackHandler.getStackInSlot(0).grow(1);
                        }
                        placeBlock(getPos().offset(facing, startOfRetraction()), Blocks.AIR.getDefaultState());
                    }
                }
                timer = 0;
            }
        }
    }


    public void placeBlocksFacing() {
        if(itemStackHandler.getStackInSlot(0) != ItemStack.EMPTY) {
            if(itemStackHandler.getStackInSlot(0).getItem() instanceof ItemBlock) {
                ItemStack stack = itemStackHandler.getStackInSlot(0);
                EnumFacing facing = getWorld().getBlockState(getPos()).getValue(Drawbridge.FACING);
                timer++;
                if(timer >= placementDuration) {
                    for(int i = 1; i <= 16; i++) {
                        Block block = getWorld().getBlockState(getPos().offset(facing, i)).getBlock();
                        if(block == Blocks.AIR) {
                            if(!itemStackHandler.getStackInSlot(0).isEmpty()) {
                                itemStackHandler.setStackInSlot(2, itemStackHandler.getStackInSlot(0).copy());
                                placeBlock(getPos().offset(facing, i), Block.getBlockFromItem(itemStackHandler.getStackInSlot(0).getItem()).getStateFromMeta(itemStackHandler.getStackInSlot(0).getMetadata()));
                                itemStackHandler.getStackInSlot(0).shrink(1);
                            }
                            break;
                        }else if (ItemStack.areItemsEqual(stack, new ItemStack(block, 1, block.getMetaFromState(getWorld().getBlockState(getPos().offset(facing, i)))))) {
                            continue;
                        }else {
                            break;
                        }
                    }
                    timer = 0;
                }
            }
        }
    }

    public int startOfRetraction() {
        EnumFacing facing = getWorld().getBlockState(getPos()).getValue(Drawbridge.FACING);
        int start = 0;
        for(int i = 1; i <= 16; i++) {
            if(getBlockAtPos(getPos().offset(facing, i)) != Blocks.AIR) {
                if(ItemStack.areItemsEqual(itemStackHandler.getStackInSlot(2), getItemstackWithMeta(getBlockStateAtPos(getPos().offset(facing, i))))) {
                    start++;
                    continue;
                }else {
                    return start;
                }
            }else {
                return start;
            }
        }
        return start;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        if(compound.hasKey("timer")) {
            timer = compound.getInteger("timer");
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("timer", timer);
        compound.setTag("items", itemStackHandler.serializeNBT());
        return compound;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());

    }
}
