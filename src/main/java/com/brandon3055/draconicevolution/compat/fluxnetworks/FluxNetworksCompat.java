package com.brandon3055.draconicevolution.compat.fluxnetworks;

import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyPylon;
import com.brandon3055.draconicevolution.init.DEContent;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import sonar.fluxnetworks.api.FluxCapabilities;

public class FluxNetworksCompat {

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Register Flux Networks capability for Energy Pylon on all faces
        event.registerBlock(FluxCapabilities.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
            if (level instanceof ServerLevel && blockEntity instanceof TileEnergyPylon pylon) {
                return new EnergyPylonFNEnergyStorage(pylon);
            }
            return null;
        }, DEContent.ENERGY_PYLON.get());
    }
}
