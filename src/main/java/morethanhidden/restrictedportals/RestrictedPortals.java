package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.CraftingHandler;
import morethanhidden.restrictedportals.handlers.TickHandler;
import morethanhidden.restrictedportals.items.WorldKey;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid="RestrictedPortals", name="RestrictedPortals", version="1.8.9-0.4")
public class RestrictedPortals {

	@Mod.Instance(value = "RestrictedPortals")
	public static RestrictedPortals instance;
	
	public static Logger logger = LogManager.getLogger("RestrictedPortals");

	public static Achievement netherUnlock;
	public static Achievement endUnlock;

	public static final Item netherKey = new WorldKey("netherKey");
	public static final Item endKey = new WorldKey("endKey");

	public static Item netherItem;
	public static Item endItem;
	public boolean useKeys;

	@Mod.EventHandler
		public void preInit(FMLPreInitializationEvent event) {

			Configuration config = new Configuration(event.getSuggestedConfigurationFile());
			
			config.load();
			
        	// Configuration
        	String netherItemRaw = config.get(Configuration.CATEGORY_GENERAL, "Item to Unlock the Nether", "minecraft:flint_and_steel").getString();
        	String endItemRaw = config.get(Configuration.CATEGORY_GENERAL, "Item to Unlock the End", "minecraft:ender_eye").getString();
        	useKeys = config.get(Configuration.CATEGORY_GENERAL, "Use keys rather than Items specified above", false).getBoolean();
        	config.save();

			if (!useKeys) {
				String[] netherSplit = netherItemRaw.split(":");
				String[] endSplit = endItemRaw.split(":");

				endItem = GameRegistry.findItem(endSplit[0], endSplit[1]);
				netherItem = GameRegistry.findItem(netherSplit[0], netherSplit[1]);
			}else{

				GameRegistry.registerItem(endKey, "endKey");
				GameRegistry.registerItem(netherKey, "netherKey");

				endItem = RestrictedPortals.endKey;
				netherItem = RestrictedPortals.netherKey;
			}

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
			netherUnlock = new Achievement("achievement.netherUnlock", "netherUnlock", 0, 0, netherItem, null).initIndependentStat().registerStat();
			endUnlock = new Achievement("achievement.endUnlock", "endUnlock", 1, 0, endItem, netherUnlock).initIndependentStat().registerStat();
			AchievementPage.registerAchievementPage(new AchievementPage("Restricted Portals", new Achievement[]{netherUnlock, endUnlock}));
	


		}

		@Mod.EventHandler
		public void load(FMLInitializationEvent event) {
	
			//Temporary Naming based on config
			LanguageRegistry.instance().addStringLocalization("achievement.netherUnlock.desc", "en_US", "Craft a " + StatCollector.translateToLocal(netherItem.getUnlocalizedName() + ".name"));
			LanguageRegistry.instance().addStringLocalization("achievement.endUnlock.desc", "en_US", "Craft a " + StatCollector.translateToLocal(endItem.getUnlocalizedName() + ".name"));

			if(useKeys) {
				//Recipes for Keys
				GameRegistry.addShapelessRecipe(new ItemStack(endKey), new ItemStack(Items.ender_eye));
				GameRegistry.addShapelessRecipe(new ItemStack(netherKey), new ItemStack(Items.flint_and_steel));
			}
		}

}


