package morethanhidden.restrictedportals.handlers;

import java.util.HashMap;
import java.util.UUID;

import cpw.mods.fml.client.FMLClientHandler;
import morethanhidden.restrictedportals.RestrictedPortals;
import morethanhidden.restrictedportals.events.PlayerMoveEvent;
import morethanhidden.restrictedportals.object.PlayerPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class TickHandler {
	
	private HashMap<UUID, PlayerPos> lastPlayerPosition = new HashMap<>();
	
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
            		
                	player.travelToDimension(1);
                	
                	ChunkCoordinates coordinates = player.getBedLocation(0);
        			if (coordinates == null){ coordinates = player.worldObj.getSpawnPoint(); }
        			
        			player.setPositionAndUpdate(coordinates.posX, coordinates.posY + 1, coordinates.posZ);
        			
                }else{            	
                	MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, before.getDim());
                	player.playerNetServerHandler.setPlayerLocation(before.getX(), before.getY(), before.getZ(), before.getYaw(), before.getPitch());
            	}
            }
        }
		
		lastPlayerPosition.put(player.getPersistentID(), new PlayerPos(event.player));

	}

	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent event)
	{
		if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)
		{
			if (FMLClientHandler.instance().getClient().theWorld.getBlock(event.x, event.y, event.z) == Blocks.portal && event.entityPlayer.getHeldItem().getItem() == RestrictedPortals.netherItem)
			{
				event.entityPlayer.addStat(RestrictedPortals.netherUnlock, 1);
			}
			if (FMLClientHandler.instance().getClient().theWorld.getBlock(event.x, event.y, event.z) == Blocks.end_portal && event.entityPlayer.getHeldItem().getItem() == RestrictedPortals.endItem)
			{
				event.entityPlayer.addStat(RestrictedPortals.endUnlock, 1);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		
		EntityPlayerMP player = (EntityPlayerMP) e.entityPlayer;
		
		if (e.before.dim != e.entityPlayer.dimension){
			
		//Nether
		if (e.entityPlayer.dimension == -1 && ! player.func_147099_x().hasAchievementUnlocked(RestrictedPortals.netherUnlock)){
			player.addChatComponentMessage(new ChatComponentTranslation("Sorry, You need to make a " + StatCollector.translateToLocal(RestrictedPortals.netherItem.getUnlocalizedName() + ".name") + " first"));
			e.setCanceled(true);
		}
		
		//End
		if (e.entityPlayer.dimension == 1 && ! player.func_147099_x().hasAchievementUnlocked(RestrictedPortals.endUnlock)){
			player.addChatComponentMessage(new ChatComponentTranslation("Sorry, You need to make a " + StatCollector.translateToLocal(RestrictedPortals.endItem.getUnlocalizedName() + ".name") + " first"));
			e.setCanceled(true);
		}
		}
		
	}
}
