package ca.sperrer.p0t4t0sandwich.tatercomms.forge.listeners;

import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeServerStartedListener {
    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
        try {
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
