package morethanhidden.restrictedportals;

import com.google.common.collect.Lists;
import morethanhidden.restrictedportals.handlers.ConfigHandler;
import morethanhidden.restrictedportals.handlers.EventHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
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
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

		//Register Event Handler
		EventHandler eventHandler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);

		// Register ourselves for server, registry and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

	}

	@SubscribeEvent()
	public void onServerStarting(FMLServerAboutToStartEvent event){
	    String path = event.getServer().getWorldPath(FolderName.DATAPACK_DIR).toString();

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
                    ConfigHandler.GENERAL.description.get().replace("%dim%", nameSplit[i]).replace("%item%", new TranslationTextComponent(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemSplit[i])).getDescriptionId()).getString()),
                    itemSplit[i],
                    nameSplit[i].toLowerCase().replace(" ",""),
                    path
            );
        }

        //Get Datapacks
        ResourcePackList resourcepacklist = event.getServer().getPackRepository();
        resourcepacklist.reload();
        List<ResourcePackInfo> list = Lists.newArrayList(resourcepacklist.getSelectedPacks());

        //Enable the Restricted Portals Dynamic Datapack
        ResourcePackInfo restrictedPortalsDatapack = resourcepacklist.getPack("file/restrictedportals");
        if(!list.contains(restrictedPortalsDatapack)) {
            list.add(2, restrictedPortalsDatapack);
        }

        //Fix Forge / Vanilla Order (Issue #34)
        ResourcePackInfo vanillaDatapack = resourcepacklist.getPack("vanilla");
        if(list.get(0) != vanillaDatapack) {
            list.remove(vanillaDatapack);
            list.add(0, vanillaDatapack);
        }

        //Reload Datapacks
        event.getServer().reloadResources(list.stream().map(ResourcePackInfo::getId).collect(Collectors.toList())).exceptionally(ex -> null);

        //Put the advancements into the array
        for (int i = 0; i < nameSplit.length; i++) {
            advancements[i] = event.getServer().getAdvancements().getAdvancement(new ResourceLocation("restrictedportals:" + RestrictedPortals.nameSplit[i].toLowerCase().replace(" ", "")));
        }

    }

}


