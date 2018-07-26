package com.msvdaamen.tileentities;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityPulseDrawbridge extends TileEntityBlockPR {

    public TileEntityPulseDrawbridge() {}

    public TileEntityPulseDrawbridge(int range, int placementDuration) {
        this.setRANGE(range);
        this.setPlacementDuration(placementDuration);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }
}
