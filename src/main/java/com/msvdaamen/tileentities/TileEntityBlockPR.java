package com.msvdaamen.tileentities;

import com.msvdaamen.blocks.BlockFacing;
import com.msvdaamen.blocks.Drawbridge;
import com.msvdaamen.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileEntityBlockPR extends TileEntityBase implements ITickable {

    private int RANGE = 16;
    public static int SIZE = 3;
    public int placementDuration = 10;
    public int timer = 0;
    public int TEMPSLOT = SIZE - 1;
    public int MAINSLOT = 0;
    public int CAMMOSLOT = 1;
    private boolean active;
    private boolean mode = true;
    private int placed = 0;

    public TileEntityBlockPR() {}

    public TileEntityBlockPR(int range, int placementDuration) {
        this.setRANGE(range);
        this.setPlacementDuration(placementDuration);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    public ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {

        @Override
        protected void onContentsChanged(int slot) {

            if(slot == MAINSLOT) {
                itemStackHandler.setStackInSlot(TEMPSLOT, itemStackHandler.getStackInSlot(MAINSLOT).copy());
            }
            if(slot == CAMMOSLOT) {
                if(itemStackHandler.getStackInSlot(CAMMOSLOT).getItem() instanceof ItemBlock) {
                    if(getWorld().getBlockState(getPos()).getBlock() instanceof Drawbridge) {
                        System.out.println("sdhd");
                        world.setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockFacing.HAS_CAMMO, true));
                    }

                }else if(itemStackHandler.getStackInSlot(slot).isEmpty()) {
                    world.setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockFacing.HAS_CAMMO, false));
                }
            }

            TileEntityBlockPR.this.markDirty();
        }



        @Override
        public int getSlotLimit(int slot) {
            if(slot == CAMMOSLOT) {
                return 1;
            }
            return RANGE;
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
            if(itemStackHandler.getStackInSlot(MAINSLOT).getCount() != itemStackHandler.getSlotLimit(MAINSLOT)) {
                if(ItemStack.areItemsEqual(itemStackHandler.getStackInSlot(TEMPSLOT), getItemstackWithMeta((getBlockStateAtPos(getPos().offset(facing, startOfRetraction())))))) {
                    if(itemStackHandler.getStackInSlot(MAINSLOT).isEmpty()) {
                        itemStackHandler.setStackInSlot(MAINSLOT, getItemstackWithMeta((getBlockStateAtPos(getPos().offset(facing, startOfRetraction())))));
                    }else {
                        itemStackHandler.getStackInSlot(MAINSLOT).grow(1);
                    }
                    getWorld().destroyBlock(getPos().offset(facing, startOfRetraction()), false);
                    placed--;
                }
            }
        }else {
            placed = 0;
        }
    }


    public void placeBlocksFacing() {
        if(itemStackHandler.getStackInSlot(MAINSLOT) != ItemStack.EMPTY) {
            if(itemStackHandler.getStackInSlot(MAINSLOT).getItem() instanceof ItemBlock) {
                EnumFacing facing = getWorld().getBlockState(getPos()).getValue(Drawbridge.FACING);
                timer++;
                if(placed <= RANGE) {
                    Block block = getWorld().getBlockState(getPos().offset(facing, placed + 1)).getBlock();
                    if(block == Blocks.AIR) {
                        if(!itemStackHandler.getStackInSlot(MAINSLOT).isEmpty()) {
                            itemStackHandler.setStackInSlot(TEMPSLOT, itemStackHandler.getStackInSlot(MAINSLOT).copy());
                            placeBlock(getPos().offset(facing, placed + 1), Block.getBlockFromItem(itemStackHandler.getStackInSlot(MAINSLOT).getItem()).getStateFromMeta(itemStackHandler.getStackInSlot(MAINSLOT).getMetadata()));
                            itemStackHandler.getStackInSlot(MAINSLOT).shrink(1);
                            placed++;
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
                if(ItemStack.areItemsEqual(itemStackHandler.getStackInSlot(TEMPSLOT), getItemstackWithMeta(getBlockStateAtPos(getPos().offset(facing, i))))) {
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
