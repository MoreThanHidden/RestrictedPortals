package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.ConfigHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class RestrictedPortals implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigHandler.reloadConfig();
        ServerWorldEvents.LOAD.register((server, world) -> {
            RPCommon.onServerStarting(server);
        });
    }
}