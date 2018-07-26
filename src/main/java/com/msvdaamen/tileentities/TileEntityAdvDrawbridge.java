package com.msvdaamen.tileentities;

import com.msvdaamen.blocks.Drawbridge;
import net.minecraft.block.Block;
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

public class TileEntityAdvDrawbridge extends TileEntityBase implements ITickable {

    private int RANGE = 16;
    public static int SIZE = 17;
    public int placementDuration = 10;
    public int timer = 0;
    private boolean active;
    private boolean mode = true;
    private int placed = 0;

    public TileEntityAdvDrawbridge() {
    }


    public ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {

            if(slot > 0) {
                tempStack.setStackInSlot(slot - 1, itemStackHandler.getStackInSlot(slot).copy());
            }

            TileEntityAdvDrawbridge.this.markDirty();
        }



        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };
    public ItemStackHandler tempStack = new ItemStackHandler(16) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityAdvDrawbridge.this.markDirty();
        }



        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    @Override
    public void update() {
        if(!getWorld().isRemote) {
            timer++;
            if(timer >= placementDuration) {
                if(mode) {
                    redstoneMode();
                }else {
                    pulseMode();
                }
                timer = 0;
            }
        } else {
        }
    }

    private void pulseMode() {
        if(getActive()) {
            placeBlocksFacing();
        }else {
            removeBlocksFacing();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if(compound.hasKey("items"))
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        if(compound.hasKey("tempStack"))
            tempStack.deserializeNBT((NBTTagCompound) compound.getTag("tempStack"));
        if(compound.hasKey("timer"))
            timer = compound.getInteger("timer");
        if(compound.hasKey("active"))
            active = compound.getBoolean("active");
        if(compound.hasKey("mode"))
            mode = compound.getBoolean("mode");
        if(compound.hasKey("placed"))
            placed = compound.getInteger("placed");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("items", itemStackHandler.serializeNBT());
        compound.setTag("tempStack", tempStack.serializeNBT());
        compound.setInteger("timer", timer);
        compound.setBoolean("active", active);
        compound.setBoolean("mode", mode);
        compound.setInteger("placed", placed);
        return super.writeToNBT(compound);
    }

    public boolean getMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
        setActive(false);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return active;
    }

    public void redstoneMode() {
        if(shouldPlace()) {
            placeBlocksFacing();
        }else {
            removeBlocksFacing();
        }
    }

    public void removeBlocksFacing() {
        if(startOfRetraction() > 0 && placed > 0) {
            EnumFacing facing = getWorld().getBlockState(getPos()).getValue(Drawbridge.FACING);
            for (int i = 1; i < SIZE; i++) {
                if(itemStackHandler.getStackInSlot(i).getCount() != itemStackHandler.getSlotLimit(i)) {
                    if(ItemStack.areItemsEqual(tempStack.getStackInSlot(i - 1), getItemstackWithMeta((getBlockStateAtPos(getPos().offset(facing, startOfRetraction())))))) {
                        if(itemStackHandler.getStackInSlot(i).isEmpty()) {
                            itemStackHandler.setStackInSlot(i, getItemstackWithMeta((getBlockStateAtPos(getPos().offset(facing, startOfRetraction())))));
                            getWorld().destroyBlock(getPos().offset(facing, startOfRetraction()), false);
                            placed--;
                            break;
                        }
                    }
                }
            }
        }else {
            placed = 0;
        }
    }


    public void placeBlocksFacing() {
        EnumFacing facing = getWorld().getBlockState(getPos()).getValue(Drawbridge.FACING);
        if(getWorld().getBlockState(getPos().offset(facing, 1)).getBlock() == Blocks.AIR) {
            placed = 0;
        }
        if(placed < RANGE) {
            for (int i = 1; i < SIZE; i++) {
            if(itemStackHandler.getStackInSlot(i) != ItemStack.EMPTY) {
                if(itemStackHandler.getStackInSlot(i).getItem() instanceof ItemBlock) {
                    timer++;
                        Block block = getWorld().getBlockState(getPos().offset(facing, placed + 1)).getBlock();
                        if(block == Blocks.AIR) {
                            if(!itemStackHandler.getStackInSlot(i).isEmpty()) {
                                tempStack.setStackInSlot(placed, itemStackHandler.getStackInSlot(i).copy());
                                placeBlock(getPos().offset(facing, placed + 1), Block.getBlockFromItem(itemStackHandler.getStackInSlot(i).getItem()).getStateFromMeta(itemStackHandler.getStackInSlot(i).getMetadata()));
                                itemStackHandler.getStackInSlot(i).shrink(1);
                                placed++;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public int startOfRetraction() {
        EnumFacing facing = getWorld().getBlockState(getPos()).getValue(Drawbridge.FACING);
        int start = 0;
        for(int i = 1; i <= placed; i++) {
            if(getBlockAtPos(getPos().offset(facing, i)) != Blocks.AIR) {
                if(ItemStack.areItemsEqual(tempStack.getStackInSlot(i - 1), getItemstackWithMeta(getBlockStateAtPos(getPos().offset(facing, i))))) {
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

    public void setRANGE(int RANGE) {
        this.RANGE = RANGE;
    }

    public boolean shouldPlace() {
        return getWorld().isBlockIndirectlyGettingPowered(getPos()) > 0;
    }

    public void setPlacementDuration(int placementDuration) {
        this.placementDuration = placementDuration;
    }

    public int getPlacementDuration() {return this.placementDuration; }
}
