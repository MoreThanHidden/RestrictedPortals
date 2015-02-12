package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.CraftingHandler;
import morethanhidden.restrictedportals.handlers.TickHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="RestrictedPortals", name="RestrictedPortals", version="0.1")
public class RestrictedPortals {

	@Instance(value = "DoomShrine")
	public static RestrictedPortals instance;

	public static Logger logger = LogManager.getLogger("RestrictedPortals");

	public static Achievement netherUnlock;
	public static Achievement endUnlock;

	public static Item netherItem = Items.flint_and_steel;
	public static Item endItem = Items.ender_eye;

		@EventHandler
		public void preInit(FMLPreInitializationEvent event) {
	
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


