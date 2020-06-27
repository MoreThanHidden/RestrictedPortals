package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.ConfigHandler;
import morethanhidden.restrictedportals.handlers.EventHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Mod("restrictedportals")
public class RestrictedPortals {

	public static String[] nameSplit;
    public static List<ResourceLocation> dimResSplit;
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
	public void onServerStarting(FMLServerAboutToStartEvent event){
	    String path = event.getServer().func_240776_a_(FolderName.field_237251_g_).toString();

		AdvancementHelper.CreateDatapack(path);
        AdvancementHelper.ClearCustomAdvancements(path);

        nameSplit = ConfigHandler.GENERAL.dimNames.get().split(",");
        dimResSplit = Arrays.stream(ConfigHandler.GENERAL.dimResName.get().split(",")).map(String::toLowerCase).map(ResourceLocation::new).collect(Collectors.toList());
        itemSplit = ConfigHandler.GENERAL.craftItems.get().split(",");

        advancements = new Advancement[nameSplit.length];

        //Get Advancements from Config
        for (int i = 0; i < nameSplit.length; i++) {
            AdvancementHelper.AddCustomAdvancement(
                    ConfigHandler.GENERAL.craftedmessage.get().replace("%dim%", nameSplit[i]),
                    "Unlock the %dim%".replace("%dim%", nameSplit[i]),
                    itemSplit[i],
                    nameSplit[i].toLowerCase().replace(" ",""),
                    path
            );
        }

        ResourcePackList<?> resourcepacklist = event.getServer().getResourcePacks();
        resourcepacklist.reloadPacksFromFinders();
        event.getServer().func_240780_a_(resourcepacklist.func_232616_b_());

        //Put the advancements into the array
        for (int i = 0; i < nameSplit.length; i++) {
            advancements[i] = event.getServer().getAdvancementManager().getAdvancement(new ResourceLocation("restrictedportals:" + RestrictedPortals.nameSplit[i].toLowerCase().replace(" ", "")));
        }

    }

}


