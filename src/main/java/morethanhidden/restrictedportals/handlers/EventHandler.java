package morethanhidden.restrictedportals.handlers;

import morethanhidden.restrictedportals.RestrictedPortals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.UUID;

public class EventHandler {
	
	private HashMap<UUID, Long> sentMessage = new HashMap<>();

	@SubscribeEvent
	public void onPlayerChangeDim(EntityTravelToDimensionEvent event){

		if(event.getEntity() instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) event.getEntity();
			for (int i = 0; i < RestrictedPortals.idSplit.length; i++) {
				if (event.getDimension().getId() == Integer.parseInt(RestrictedPortals.idSplit[i].trim()) && !playerMP.getAdvancements().getProgress(RestrictedPortals.advancements[i]).isDone()) {
					//Prevent Spam (Only send message when interval is greater then 40 ticks and new servers are negative for some reason)
				    if(!sentMessage.containsKey(playerMP.getUniqueID()) || (playerMP.world.getGameTime() - sentMessage.get(playerMP.getUniqueID())) > 40 || (playerMP.world.getGameTime() - sentMessage.get(playerMP.getUniqueID())) < 0) {
						String item = new ItemStack(GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(RestrictedPortals.itemSplit[i]))).getDisplayName().getString();
				    	if(!playerMP.world.isRemote)
				    		playerMP.sendStatusMessage(new TextComponentTranslation(ConfigHandler.GENERAL.blockedmessage.get().replace("%item%", item).replace("%dim%", RestrictedPortals.nameSplit[i])), false);
				    	sentMessage.put(playerMP.getUniqueID(), playerMP.world.getGameTime());
					}
					//Prevent Death by Lava for End Portal
					if(event.getDimension().getId() == 1 && ConfigHandler.GENERAL.preventEPDeath.get()){
                        BlockPos coordinates = playerMP.getBedLocation(DimensionType.OVERWORLD);
                        if (coordinates == null){ coordinates = playerMP.world.getSpawnPoint(); }
                        playerMP.setPositionAndUpdate(coordinates.getX(), coordinates.getY(), coordinates.getZ());
                    }
					event.setCanceled(true);
				}
			}
		}
	}

}
