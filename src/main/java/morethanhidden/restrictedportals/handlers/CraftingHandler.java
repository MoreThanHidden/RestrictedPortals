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
        for(int i = 0; i < RestrictedPortals.idSplit.length; i++){
    	    if (event.crafting.getItem() == RestrictedPortals.itemList[i].getItem()){
                if ((event.crafting.getMetadata() == RestrictedPortals.itemList[i].getMetadata()) || !RestrictedPortals.metaUsed[i]) {
                    event.player.addStat(RestrictedPortals.portalUnlock[i], 1);
                }
            }
        }
    }
}