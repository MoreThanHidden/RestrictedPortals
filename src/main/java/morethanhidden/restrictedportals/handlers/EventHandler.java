package morethanhidden.restrictedportals.handlers;

import morethanhidden.restrictedportals.RestrictedPortals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.UUID;

public class EventHandler {
	
	private HashMap<UUID, Long> sentMessage = new HashMap<>();

	@SubscribeEvent
	public void onPlayerChangeDim(EntityTravelToDimensionEvent event){

		if(event.getEntity() instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) event.getEntity();
			for (int i = 0; i < RestrictedPortals.idSplit.length; i++) {
				if (event.getDimension() == Integer.parseInt(RestrictedPortals.idSplit[i].trim()) && playerMP.getStatFile().readStat(RestrictedPortals.portalUnlock[i]) == 0) {
					//Prevent Spam
				    if(!sentMessage.containsKey(playerMP.getUniqueID()) || (playerMP.world.getWorldTime() - sentMessage.get(playerMP.getUniqueID())) > 40 ) {
						playerMP.sendStatusMessage(new TextComponentTranslation(RestrictedPortals.blockedmessage.replace("%item%", RestrictedPortals.itemList[i].getDisplayName()).replace("%dim%", RestrictedPortals.nameSplit[i])), false);
						sentMessage.put(playerMP.getUniqueID(), playerMP.world.getWorldTime());
					}
					//Prevent Death by Lava for End Portal
					if(event.getDimension() == 1 && RestrictedPortals.preventEPDeath){
                        BlockPos coordinates = playerMP.getBedLocation(0);
                        if (coordinates == null){ coordinates = playerMP.world.getSpawnPoint(); }
                        playerMP.setPositionAndUpdate(coordinates.getX(), playerMP.world.getTopSolidOrLiquidBlock(coordinates).getY(), coordinates.getZ());
                    }
					event.setCanceled(true);
				}
			}
		}
	}

}
