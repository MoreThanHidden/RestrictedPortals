package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.ConfigHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;

@Mod("restrictedportals")
public class RestrictedPortals {

	public RestrictedPortals() {

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (remote, isServer) -> true));

		// Register ourselves for server, registry and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

	}

	@SubscribeEvent()
	public void onServerStarting(ServerAboutToStartEvent event){
		RPCommon.onServerStarting(event.getServer());
    }

	@SubscribeEvent
	public void onPlayerChangeDim(EntityTravelToDimensionEvent event){
		if(RPCommon.blockPlayerFromTransit(event.getEntity(), event.getDimension())){
			event.setCanceled(true);
		}
	}

}


