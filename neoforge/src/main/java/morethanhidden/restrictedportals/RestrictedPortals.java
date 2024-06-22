package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.ConfigHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod("restrictedportals")
public class RestrictedPortals {

	public RestrictedPortals() {
		ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);
        NeoForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent()
	public void onServerStarting(ServerStartingEvent event){
		RPCommon.onServerStarting(event.getServer());
    }

	@SubscribeEvent
	public void onPlayerChangeDim(EntityTravelToDimensionEvent event){
		if(RPCommon.blockPlayerFromTransit(event.getEntity(), event.getDimension())){
			event.setCanceled(true);
		}
	}

}


