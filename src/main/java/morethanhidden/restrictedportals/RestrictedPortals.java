package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.CraftingHandler;
import morethanhidden.restrictedportals.handlers.TickHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="RestrictedPortals", name="RestrictedPortals", version="0.2")
public class RestrictedPortals {

	@Instance(value = "RestrictedPortals")
	public static RestrictedPortals instance;
	
	public static Logger logger = LogManager.getLogger("RestrictedPortals");

	public static Achievement netherUnlock;
	public static Achievement endUnlock;

	public static Item netherItem;
	public static Item endItem;

		@EventHandler
		public void preInit(FMLPreInitializationEvent event) {
				
			Configuration config = new Configuration(event.getSuggestedConfigurationFile());
			
			config.load();
			
        	// Configuration
        	String netherItemRaw = config.get(config.CATEGORY_GENERAL, "Item to Unlock the Nether", "minecraft:flint_and_steel").getString();
        	String endItemRaw = config.get(config.CATEGORY_GENERAL, "Item to Unlock the End", "minecraft:ender_eye").getString();
        	
        	config.save();
			
        	String[] netherSplit = netherItemRaw.split(":");
        	String[] endSplit = endItemRaw.split(":");
        	
			endItem = GameRegistry.findItem(endSplit[0], endSplit[1]);
			netherItem = GameRegistry.findItem(netherSplit[0], netherSplit[1]);
			
			//If Configuration is invalid
			if (endItem == null){
				endItem = Items.ender_eye;
				logger.info("Please fix the End Item in the Config");
			}
			if (netherItem == null){
				netherItem = Items.flint_and_steel;
				logger.info("Please fix the Nether Item in the Config");
			}
				
			//Register Tick Handler
			TickHandler tickHandler = new TickHandler();
			FMLCommonHandler.instance().bus().register(tickHandler);
			MinecraftForge.EVENT_BUS.register(tickHandler);
				
    		//Register Crafting Handler
			FMLCommonHandler.instance().bus().register(new CraftingHandler());
    
			//Achievements
			netherUnlock = new Achievement("achievement.netherUnlock", "netherUnlock", 0, 0, netherItem, (Achievement)null).initIndependentStat().registerStat();
			endUnlock = new Achievement("achievement.endUnlock", "endUnlock", 1, 0, endItem, netherUnlock).initIndependentStat().registerStat();
			AchievementPage.registerAchievementPage(new AchievementPage("Restricted Portals", new Achievement[]{netherUnlock, endUnlock}));
	


		}

		@EventHandler
		public void load(FMLInitializationEvent event) {
	
			//Temporary Naming
			LanguageRegistry.instance().addStringLocalization("achievement.netherUnlock", "en_US", "Nether Unlocked!");
			LanguageRegistry.instance().addStringLocalization("achievement.netherUnlock.desc", "en_US", "Craft a " + StatCollector.translateToLocal(netherItem.getUnlocalizedName() + ".name"));
			LanguageRegistry.instance().addStringLocalization("achievement.endUnlock", "en_US", "End Unlocked!");
			LanguageRegistry.instance().addStringLocalization("achievement.endUnlock.desc", "en_US", "Craft a " + StatCollector.translateToLocal(endItem.getUnlocalizedName() + ".name"));

		         
		}

}


