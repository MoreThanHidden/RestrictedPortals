package morethanhidden.restrictedportals;

import com.google.common.collect.Lists;
import morethanhidden.restrictedportals.handlers.ConfigHandler;
import morethanhidden.restrictedportals.handlers.EventHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.registries.ForgeRegistries;
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

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (remote, isServer) -> true));

		//Register Event Handler
		EventHandler eventHandler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);

		// Register ourselves for server, registry and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

	}

	@SubscribeEvent()
	public void onServerStarting(ServerAboutToStartEvent event){
	    String path = event.getServer().getWorldPath(LevelResource.DATAPACK_DIR).toString();

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
                    ConfigHandler.GENERAL.description.get().replace("%dim%", nameSplit[i]).replace("%item%", new TranslatableComponent(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemSplit[i])).getDescriptionId()).getString()),
                    itemSplit[i],
                    nameSplit[i].toLowerCase().replace(" ",""),
                    path
            );
        }

        //Get Datapacks
        PackRepository resourcepacklist = event.getServer().getPackRepository();
        resourcepacklist.reload();
        List<Pack> list = Lists.newArrayList(resourcepacklist.getSelectedPacks());

        //Enable the Restricted Portals Dynamic Datapack
        Pack restrictedPortalsDatapack = resourcepacklist.getPack("file/restrictedportals");
        if(!list.contains(restrictedPortalsDatapack)) {
            list.add(2, restrictedPortalsDatapack);
        }

        //Fix Forge / Vanilla Order (Issue #34)
        Pack vanillaDatapack = resourcepacklist.getPack("vanilla");
        if(list.get(0) != vanillaDatapack) {
            list.remove(vanillaDatapack);
            list.add(0, vanillaDatapack);
        }

        //Reload Datapacks
        event.getServer().reloadResources(list.stream().map(Pack::getId).collect(Collectors.toList())).exceptionally(ex -> null);

        //Put the advancements into the array
        for (int i = 0; i < nameSplit.length; i++) {
            advancements[i] = event.getServer().getAdvancements().getAdvancement(new ResourceLocation("restrictedportals:" + RestrictedPortals.nameSplit[i].toLowerCase().replace(" ", "")));
        }

    }

}


