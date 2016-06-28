package morethanhidden.restrictedportals.handlers;

import java.util.HashMap;
import java.util.UUID;

import morethanhidden.restrictedportals.RestrictedPortals;
import morethanhidden.restrictedportals.events.PlayerMoveEvent;
import morethanhidden.restrictedportals.object.PlayerPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.StatsComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class TickHandler {
	
	private HashMap<UUID, PlayerPos> lastPlayerPosition = new HashMap();
	
	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		if (event.side != Side.SERVER || event.phase == TickEvent.Phase.START)
			return;
		
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		
		PlayerPos before = lastPlayerPosition.get(player.getPersistentID());
		PlayerPos current = new PlayerPos(event.player);
		
		if (before != null && !player.isDead && player.worldObj != null && !before.equals(current))
        {
			
            PlayerMoveEvent moveEvent = new PlayerMoveEvent(player, before, current);
            MinecraftForge.EVENT_BUS.post(moveEvent);
            
            if (moveEvent.isCanceled() && event.side == Side.SERVER){
                	 
            	if (current.dim == 1){

					BlockPos coordinates = player.getBedLocation(0);
					if (coordinates == null){ coordinates = player.worldObj.getSpawnPoint(); }

					player.changeDimension(1);
        			player.setPositionAndUpdate(coordinates.getX(), coordinates.getY() + 1, coordinates.getZ());
        			
                }else{            	
                	player.changeDimension(before.getDim());
                	player.setLocationAndAngles(before.getX(), before.getY(), before.getZ(), before.getYaw(), before.getPitch());
            	}
            }
        }
		
		lastPlayerPosition.put(player.getPersistentID(), new PlayerPos(event.player));

	}
	
	
	@SubscribeEvent
	public void onPlayerMoveEvent(PlayerMoveEvent e) {

		EntityPlayerMP player = (EntityPlayerMP) e.getEntityPlayer();

		if (e.before.dim != player.dimension) {
			for (int i = 0; i < RestrictedPortals.idSplit.length; i++) {
				if (player.dimension == Integer.parseInt(RestrictedPortals.idSplit[i].trim()) && !player.getStatFile().hasAchievementUnlocked(RestrictedPortals.portalUnlock[i])) {
					player.addChatComponentMessage(new TextComponentTranslation("Sorry, You need to craft a " + RestrictedPortals.itemList[i].getDisplayName() + " first"));
					e.setCanceled(true);
				}
			}

		}
	}
}
