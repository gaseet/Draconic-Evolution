package com.brandon3055.draconicevolution.compat.fluxnetworks;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyPylon;
import sonar.fluxnetworks.api.energy.IFNEnergyStorage;

public class EnergyPylonFNEnergyStorage implements IFNEnergyStorage {

    private final TileEnergyPylon pylon;

    public EnergyPylonFNEnergyStorage(TileEnergyPylon pylon) {
        this.pylon = pylon;
    }

    @Override
    public long receiveEnergyL(long maxReceive, boolean simulate) {
        if (pylon.coreOffset.isNull() || !pylon.opAdapter.canReceive() || pylon.getCore() == null || !pylon.getCore().active.get()) {
            return 0;
        }
        return pylon.getCore().energy.receiveOP(maxReceive, simulate);
    }

    @Override
    public long extractEnergyL(long maxExtract, boolean simulate) {
        if (pylon.coreOffset.isNull() || !pylon.opAdapter.canExtract() || pylon.getCore() == null || !pylon.getCore().active.get()) {
            return 0;
        }
        return pylon.getCore().energy.extractOP(maxExtract, simulate);
    }

    @Override
    public long getEnergyStoredL() {
        return pylon.opAdapter.getOPStored();
    }

    @Override
    public long getMaxEnergyStoredL() {
        return pylon.opAdapter.getMaxOPStored();
    }

    @Override
    public boolean canExtract() {
        return pylon.opAdapter.canExtract();
    }

    @Override
    public boolean canReceive() {
        return pylon.opAdapter.canReceive();
    }
}
