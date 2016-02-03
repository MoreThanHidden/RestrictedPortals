package morethanhidden.restrictedportals.handlers;

import morethanhidden.restrictedportals.RestrictedPortals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class CraftingHandler
{
    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event)
    {
    	if (event.crafting.getItem() == RestrictedPortals.netherItem)
        {
            event.player.addStat(RestrictedPortals.netherUnlock, 1);
            
        }else if (event.crafting.getItem() == RestrictedPortals.endItem)
        {
            event.player.addStat(RestrictedPortals.endUnlock, 1);
        }
    }
}