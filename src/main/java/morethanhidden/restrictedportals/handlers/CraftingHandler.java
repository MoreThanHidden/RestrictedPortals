package morethanhidden.restrictedportals.handlers;

import morethanhidden.restrictedportals.RestrictedPortals;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CraftingHandler
{
    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event)
    {
        for(int i = 0; i < RestrictedPortals.idSplit.length; i++){
    	    if (event.crafting.getItem() == RestrictedPortals.itemList[i].getItem()){
                if (!RestrictedPortals.metaUsed[i] || (event.crafting.getMetadata() == RestrictedPortals.itemList[i].getMetadata())) {
                    event.player.addStat(RestrictedPortals.portalUnlock[i], 1);
                    if(event.player.world.isRemote) {
                        event.player.sendStatusMessage(new TextComponentTranslation(RestrictedPortals.craftedmessage.replace("%dim%", RestrictedPortals.nameSplit[i])), false);
                    }
                }
            }
        }
    }
}