package com.infinityraider.agricraft.plugins.theoneprobe;

import com.infinityraider.agricraft.api.v1.plugin.AgriPlugin;
import com.infinityraider.agricraft.api.v1.plugin.IAgriPlugin;
import com.infinityraider.agricraft.reference.Names;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@AgriPlugin
@SuppressWarnings("unused")
public class TheOneProbePlugin implements IAgriPlugin {
    @Override
    public boolean isEnabled() {
        return ModList.get().isLoaded(this.getId());
    }

    @Override
    public String getId() {
        return Names.Mods.THE_ONE_PROBE;
    }

    @Override
    public String getName() {
        return this.getId();
    }

    @Override
    public void onInterModEnqueueEvent(InterModEnqueueEvent event) {
        InterModComms.sendTo(this.getId(), "getTheOneProbe", TheOneProbeSupplier.getSupplier());
    }
}
