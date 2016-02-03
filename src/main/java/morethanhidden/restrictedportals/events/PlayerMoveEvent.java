package morethanhidden.restrictedportals.events;

import morethanhidden.restrictedportals.object.PlayerPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PlayerMoveEvent extends PlayerEvent{
	public final PlayerPos before;
    public final PlayerPos after;
    
	public PlayerMoveEvent(EntityPlayerMP player, PlayerPos before, PlayerPos after)
    {
		super(player);
		this.before = before;
        this.after = after;
    }

	
}
