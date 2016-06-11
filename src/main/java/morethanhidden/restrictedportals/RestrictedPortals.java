package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.CraftingHandler;
import morethanhidden.restrictedportals.handlers.TickHandler;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


@Mod(modid="RestrictedPortals", name="Restricted Portals", version="1.9.4-0.5")
public class RestrictedPortals {

	@Mod.Instance(value = "RestrictedPortals")
	public static RestrictedPortals instance;
	
	public static Logger logger = LogManager.getLogger("RestrictedPortals");

	public static Achievement[] portalUnlock;
	public static String[] idSplit;
	public static Item[] itemList;
	public String[] nameSplit;

	@Mod.EventHandler
		public void preInit(FMLPreInitializationEvent event) {

			Configuration config = new Configuration(event.getSuggestedConfigurationFile());
			
			config.load();
			
        	// Configuration
        	String craftItemRaw = config.get(Configuration.CATEGORY_GENERAL, "Crafted Items", "minecraft:flint_and_steel,minecraft:ender_eye").getString();
		    String dimNameRaw = config.get(Configuration.CATEGORY_GENERAL, "Dimension Names", "Nether,End").getString();
        	String dimIDRaw = config.get(Configuration.CATEGORY_GENERAL, "Dimension IDs", "-1,1").getString();
        	config.save();

			String[] craftSplit = craftItemRaw.split(",");
			nameSplit = dimNameRaw.split(",");
			idSplit = dimIDRaw.split(",");

			itemList = new Item[nameSplit.length];
			portalUnlock = new Achievement[nameSplit.length];

			//Basic Configuration Check
			for (int i = 0; i < nameSplit.length; i++) {
				String[] itemSplit = craftSplit[i].split(":");
				itemList[i] = (GameRegistry.findItem(itemSplit[0], itemSplit[1]));
				if(idSplit[i].equals("")){
					logger.info("Please fix the " + nameSplit[i] + " Dimension ID in the Config");
				}else if (itemList[i] == null){
					logger.info("Please fix the " + nameSplit[i] + " Item in the Config");
				}else{
					portalUnlock[i] = new Achievement("achievement." + nameSplit[i] + "Unlock", nameSplit[i] + "Unlock", i, 0, itemList[i], null).initIndependentStat().registerStat();
				}
			}

				
			//Register Tick Handler
			TickHandler tickHandler = new TickHandler();
			FMLCommonHandler.instance().bus().register(tickHandler);
			MinecraftForge.EVENT_BUS.register(tickHandler);
				
    		//Register Crafting Handler
			FMLCommonHandler.instance().bus().register(new CraftingHandler());
    
			//Achievements
			AchievementPage.registerAchievementPage(new AchievementPage("Restricted Portals", portalUnlock));

		}

}


