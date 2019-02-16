package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.ConfigHandler;
import morethanhidden.restrictedportals.handlers.EventHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Arrays;


@Mod("restrictedportals")
public class RestrictedPortals {

	public static String[] idSplit;
	public static String[] nameSplit;
    public static String[] itemSplit;
    public static Advancement[] advancements;

	public static Logger LOGGER = LogManager.getLogger("RestrictedPortals");

	public RestrictedPortals() {

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHandler.spec);

		//Register Event Handler
		EventHandler eventHandler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);

		// Register ourselves for server, registry and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

	}

	@SubscribeEvent()
	public void onServerStarting(FMLServerStartingEvent event){
	    //TODO Only update when changes have been made.

        String path = event.getServer().getDataDirectory().getPath() + "/saves/" + event.getServer().getFolderName() + "/datapacks";

        if(event.getServer().isDedicatedServer()){
            path = event.getServer().getDataDirectory().getPath() + "/" + event.getServer().getFolderName() + "/datapacks";
        }

		AdvancementHelper.CreateDatapack(path);
        AdvancementHelper.ClearCustomAdvancements(path);

        idSplit = ConfigHandler.GENERAL.dimIDs.get().split(",");
        nameSplit = ConfigHandler.GENERAL.dimNames.get().split(",");
        itemSplit = ConfigHandler.GENERAL.craftItems.get().split(",");

        advancements = new Advancement[idSplit.length];

        //Get Advancements from Config
        for (int i = 0; i < idSplit.length; i++) {
            AdvancementHelper.AddCustomAdvancement(
                    ConfigHandler.GENERAL.craftedmessage.get().replace("%dim%", nameSplit[i]),
                    "Unlock the %dim%".replace("%dim%", nameSplit[i]),
                    itemSplit[i],
                    nameSplit[i].toLowerCase().replace(" ",""),
                    path
            );
        }

		LOGGER.info(Arrays.toString(AdvancementHelper.GetCustomAdvancementList(path + "/restrictedportals/data/restrictedportals/advancements")));

		event.getServer().reload();

		//Put the advancements into the array
        for (int i = 0; i < idSplit.length; i++) {
            advancements[i] = event.getServer().getAdvancementManager().getAdvancement(new ResourceLocation("restrictedportals:" + RestrictedPortals.nameSplit[i].toLowerCase().replace(" ", "")));
        }

    }

}


