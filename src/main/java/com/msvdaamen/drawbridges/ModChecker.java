package com.msvdaamen.drawbridges;

import net.minecraftforge.fml.common.Loader;

public class ModChecker {

    public static boolean isThermalFoundationLoaded;

    public ModChecker() {
        this.isThermalFoundationLoaded = Loader.isModLoaded("thermalfoundation");
    }
}
