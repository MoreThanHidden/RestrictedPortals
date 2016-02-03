package morethanhidden.restrictedportals.handlers;

import java.util.HashMap;
import java.util.UUID;

import morethanhidden.restrictedportals.RestrictedPortals;
import morethanhidden.restrictedportals.events.PlayerMoveEvent;
import morethanhidden.restrictedportals.object.PlayerPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
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

					player.travelToDimension(1);
        			player.setPositionAndUpdate(coordinates.getX(), coordinates.getY() + 1, coordinates.getZ());
        			
                }else{            	
                	MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, before.getDim());
                	player.playerNetServerHandler.setPlayerLocation(before.getX(), before.getY(), before.getZ(), before.getYaw(), before.getPitch());
            	}
            }
        }
		
		lastPlayerPosition.put(player.getPersistentID(), new PlayerPos(event.player));

	}
	
	
	@SubscribeEvent
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		
		EntityPlayerMP player = (EntityPlayerMP) e.entityPlayer;
		
		if (e.before.dim != e.entityPlayer.dimension){
			
		//Nether
		if (e.entityPlayer.dimension == -1 && ! player.getStatFile().hasAchievementUnlocked(RestrictedPortals.netherUnlock)){
			player.addChatComponentMessage(new ChatComponentTranslation("Sorry, You need to make a " + StatCollector.translateToLocal(RestrictedPortals.netherItem.getUnlocalizedName() + ".name") + " first"));
			e.setCanceled(true);
		}
		
		//End
		if (e.entityPlayer.dimension == 1 && ! player.getStatFile().hasAchievementUnlocked(RestrictedPortals.endUnlock)){
			player.addChatComponentMessage(new ChatComponentTranslation("Sorry, You need to make a " + StatCollector.translateToLocal(RestrictedPortals.endItem.getUnlocalizedName() + ".name") + " first"));
			e.setCanceled(true);
		}
		}
		
	}
}
