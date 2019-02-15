package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.ConfigHandler;
import morethanhidden.restrictedportals.handlers.CraftingHandler;
import morethanhidden.restrictedportals.handlers.EventHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


@Mod("restrictedportals")
public class RestrictedPortals {

	public static String[] idSplit;
	public static String[] nameSplit;
	public static Stat[] portalUnlock;
	public static ItemStack[] itemList;
	public static List<Block> pblockwhitelist = new ArrayList<>();
	public static boolean[] metaUsed;

	public static Logger logger = LogManager.getLogger("restrictedportals");

	public RestrictedPortals() {

		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHandler.spec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.spec);

		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		//Register Event Handler
		EventHandler eventHandler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);

		//Register Crafting Handler
		MinecraftForge.EVENT_BUS.register(new CraftingHandler());

		//Achievements
		//AchievementPage.registerAchievementPage(new AchievementPage("Restricted Portals", portalUnlock));

		// Register ourselves for server, registry and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

	}

	private void setup(final FMLCommonSetupEvent event){

		idSplit = ConfigHandler.GENERAL.dimIDs.get().split(",");
		nameSplit = ConfigHandler.GENERAL.dimNames.get().split(",");

		itemList = new ItemStack[idSplit.length];
		portalUnlock = new Stat[idSplit.length];
		metaUsed = new boolean[idSplit.length];

		String[] craftSplit = ConfigHandler.GENERAL.craftItems.get().split(",");
		String[] pblockwhitelistSplit = ConfigHandler.GENERAL.pblockwhitelist.get().split(",");

		//Basic Configuration Check
		for (int i = 0; i < idSplit.length; i++) {

			String[] itemsplit = craftSplit[i].split(":");

			if (itemsplit.length == 3){
				metaUsed[i] = true;
			}else{
				metaUsed[i] = false;
			}

			Item item = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(itemsplit[0], itemsplit[1]));

			if (item != null){
				itemList[i] = new ItemStack(item, 1);
			}else {
				itemList[i] = new ItemStack(GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(itemsplit[0], itemsplit[1])), 1);
			}

			if (itemList[i] == null){

			}

			if(idSplit[i].equals("")){
				logger.info("Please fix the " + nameSplit[i] + " Dimension ID in the Config");
			}else if (itemList[i] ==  null || itemList[i].isEmpty()){
				logger.info("Please fix the " + nameSplit[i] + " Item in the Config");
			}

		}

		for (String pblock : pblockwhitelistSplit) {
			pblockwhitelist.add(GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(pblock.trim())));
		}

	}

}


