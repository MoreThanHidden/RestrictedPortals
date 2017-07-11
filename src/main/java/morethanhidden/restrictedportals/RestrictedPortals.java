package morethanhidden.restrictedportals;

import morethanhidden.restrictedportals.handlers.CraftingHandler;
import morethanhidden.restrictedportals.handlers.TickHandler;
import morethanhidden.restrictedportals.util.StringFormatter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid="restrictedportals", name="Restricted Portals", version="1.11.2-0.5.9")
public class RestrictedPortals {

	@Mod.Instance(value = "restrictedportals")
	public static RestrictedPortals instance;
	
	public static Logger logger = LogManager.getLogger("restrictedportals");

	public static Achievement[] portalUnlock;
	public static String[] idSplit;
	public static ItemStack[] itemList;
    public static boolean[] metaUsed;
	public static String[] nameSplit;
	public Configuration config;
	public static String blockedmessage;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event){
		config = new Configuration(event.getSuggestedConfigurationFile());
	}

	@Mod.EventHandler
		public void Init(FMLInitializationEvent event) {

		// Configuration
			config.load();
			config.setCategoryComment(Configuration.CATEGORY_GENERAL, "Achievement Names currently need to be set in lang, Descriptions are Dynamic.");
			blockedmessage = config.get(Configuration.CATEGORY_GENERAL, "Blocked Message", "Please craft a %item% to enter the %dim%").getString();
			String craftItemRaw = config.get(Configuration.CATEGORY_GENERAL, "Crafted Items", "minecraft:flint_and_steel,minecraft:ender_eye").getString();
			String dimNameRaw = config.get(Configuration.CATEGORY_GENERAL, "Dimension Names", "Nether,End").getString();
			String dimIDRaw = config.get(Configuration.CATEGORY_GENERAL, "Dimension IDs", "-1,1").getString();

			config.save();

			String[] craftSplit = craftItemRaw.split(",");
			nameSplit = dimNameRaw.split(",");
			idSplit = dimIDRaw.split(",");

			itemList = new ItemStack[nameSplit.length];
			portalUnlock = new Achievement[nameSplit.length];

			//Basic Configuration Check
			for (int i = 0; i < nameSplit.length; i++) {

				String[] itemsplit = craftSplit[i].split(":");

                int meta = 0;

                if (itemsplit.length == 3){
                    meta = Integer.parseInt(itemsplit[2]);
                }

                Item item = Item.REGISTRY.getObject(new ResourceLocation(itemsplit[0], itemsplit[1]));

				if (item != null){
                    itemList[i] = new ItemStack(item, 1, meta);
				}else {
                    itemList[i] = new ItemStack(Block.REGISTRY.getObject(new ResourceLocation(itemsplit[0], itemsplit[1])), 1, meta);
                }

				if (itemList[i] == null){

				}

				if(idSplit[i].equals("")){
					logger.info("Please fix the " + nameSplit[i] + " Dimension ID in the Config");
				}else if (itemList[i] ==  null || itemList[i].isEmpty()){
					logger.info("Please fix the " + nameSplit[i] + " Item in the Config");
				}
				portalUnlock[i] = new Achievement("rpunlock." + nameSplit[i],"rpunlock." + nameSplit[i] , i, 0, itemList[i], null).initIndependentStat().registerStat();
				if(event.getSide() != Side.SERVER){
					portalUnlock[i].setStatStringFormatter(StringFormatter.format(itemList[i]));
				}
			}
			    //Register Tick Handler
			    TickHandler tickHandler = new TickHandler();
			    MinecraftForge.EVENT_BUS.register(tickHandler);
				
    		    //Register Crafting Handler
				MinecraftForge.EVENT_BUS.register(new CraftingHandler());

				//Achievements
				AchievementPage.registerAchievementPage(new AchievementPage("Restricted Portals", portalUnlock));
		}
}


