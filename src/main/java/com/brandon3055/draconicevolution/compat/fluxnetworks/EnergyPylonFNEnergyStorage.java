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
        return pylon.opAdapter.receiveOP(maxReceive, simulate);
    }

    @Override
    public long extractEnergyL(long maxExtract, boolean simulate) {
        return pylon.opAdapter.extractOP(maxExtract, simulate);
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
