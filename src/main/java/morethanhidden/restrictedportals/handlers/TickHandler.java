package morethanhidden.restrictedportals.handlers;

import java.util.HashMap;
import java.util.UUID;

import morethanhidden.restrictedportals.RestrictedPortals;
import morethanhidden.restrictedportals.events.PlayerMoveEvent;
import morethanhidden.restrictedportals.object.PlayerPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

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
            if (moveEvent.isCanceled()){
            	
            	if (player.dimension != before.getDim())
                	{
                		MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, before.getDim());
                	}
                	player.playerNetServerHandler.setPlayerLocation(before.getX(), before.getY(), before.getZ(), before.getYaw(), before.getPitch());
                	player.prevPosX = player.posX = before.getX();
                	player.prevPosY = player.posY = before.getY();
                	player.prevPosZ = player.posZ = before.getZ();
            }
        }
		
		lastPlayerPosition.put(player.getPersistentID(), new PlayerPos(event.player));
		
		
		
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
