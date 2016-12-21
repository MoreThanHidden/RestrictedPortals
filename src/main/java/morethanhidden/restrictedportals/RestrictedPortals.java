package morethanhidden.restrictedportals;

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

import java.util.ArrayList;

@Mod(modid="RestrictedPortals", name="RestrictedPortals", version="0.3.3")
public class RestrictedPortals {

	@Instance(value = "RestrictedPortals")
	public static RestrictedPortals instance;
	
	public static Logger logger = LogManager.getLogger("RestrictedPortals");

	public static Achievement netherUnlock;
	public static Achievement endUnlock;

	public static final Item netherKey = new WorldKey("netherKey");
	public static final Item endKey = new WorldKey("endKey");

	public static Item netherItem;
	public static Item endItem;
	public static boolean netherLock;
	public static boolean endLock;
	public static boolean ConsumeItem;
	public boolean useKeys;

	@EventHandler
		public void preInit(FMLPreInitializationEvent event) {

			Configuration config = new Configuration(event.getSuggestedConfigurationFile());
			
			config.load();
			
        	// Configuration
        	String netherItemRaw = config.get(Configuration.CATEGORY_GENERAL, "Item to Unlock the Nether", "minecraft:flint_and_steel").getString();
        	String endItemRaw = config.get(Configuration.CATEGORY_GENERAL, "Item to Unlock the End", "minecraft:ender_eye").getString();
        	useKeys = config.get(Configuration.CATEGORY_GENERAL, "Use keys rather than Items specified above", false).getBoolean();
			netherLock = config.get(Configuration.CATEGORY_GENERAL, "Lock the nether", true).getBoolean();
			endLock = config.get(Configuration.CATEGORY_GENERAL, "Lock the end", true).getBoolean();
			ConsumeItem = config.get(Configuration.CATEGORY_GENERAL, "Consume item on Right Click", false).getBoolean();
			config.save();

			if (!useKeys) {
				String[] netherSplit = netherItemRaw.split(":");
				String[] endSplit = endItemRaw.split(":");

				endItem = GameRegistry.findItem(endSplit[0], endSplit[1]);
				netherItem = GameRegistry.findItem(netherSplit[0], netherSplit[1]);
			}else{
				if (endLock)
					GameRegistry.registerItem(endKey, "endKey");
				if (netherLock)
					GameRegistry.registerItem(netherKey, "netherKey");

				endItem = RestrictedPortals.endKey;
				netherItem = RestrictedPortals.netherKey;
			}

			//If Configuration is invalid
			if (endLock && endItem == null){
				endItem = Items.ender_eye;
				logger.info("Please fix the End Item in the Config");
			}
			if (netherLock && netherItem == null){
				netherItem = Items.flint_and_steel;
				logger.info("Please fix the Nether Item in the Config");
			}
				
			//Register Tick Handler
			TickHandler tickHandler = new TickHandler();
			FMLCommonHandler.instance().bus().register(tickHandler);
			MinecraftForge.EVENT_BUS.register(tickHandler);
    
			//Achievements
			ArrayList<Achievement> achievements = new ArrayList<>();
			
			if (netherLock) {
				netherUnlock = new Achievement("achievement.netherUnlock", "netherUnlock", 0, 0, netherItem, null).initIndependentStat().registerStat();
				achievements.add(netherUnlock);
			}
			if (endLock) {
				endUnlock = new Achievement("achievement.endUnlock", "endUnlock", 1, 0, endItem, netherLock ? netherUnlock : null).initIndependentStat().registerStat();
				achievements.add(endUnlock);
			}
			if (achievements.size() == 0) {
				logger.warn("Neither the nether nor the end are locked. You are using this mod why again?");
			} else {
				AchievementPage.registerAchievementPage(new AchievementPage("Restricted Portals", achievements.toArray(new Achievement[achievements.size()])));
			}

		}

		@EventHandler
		public void load(FMLInitializationEvent event) {
	
			//Temporary Naming based on config
			if (netherLock)
				LanguageRegistry.instance().addStringLocalization("achievement.netherUnlock.desc", "en_US", "Craft a " + StatCollector.translateToLocal(netherItem.getUnlocalizedName() + ".name"));
			if (endLock)
				LanguageRegistry.instance().addStringLocalization("achievement.endUnlock.desc", "en_US", "Craft a " + StatCollector.translateToLocal(endItem.getUnlocalizedName() + ".name"));

			if(useKeys) {
				//Recipes for Keys
				if (endLock)
					GameRegistry.addShapelessRecipe(new ItemStack(endKey), new ItemStack(Items.ender_eye));
				if (netherLock)
					GameRegistry.addShapelessRecipe(new ItemStack(netherKey), new ItemStack(Items.flint_and_steel));
			}
		}

}


