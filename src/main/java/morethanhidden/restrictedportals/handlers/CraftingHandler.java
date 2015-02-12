package morethanhidden.restrictedportals.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import morethanhidden.restrictedportals.RestrictedPortals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class CraftingHandler
{
    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event)
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