package morethanhidden.restrictedportals.handlers;

import morethanhidden.restrictedportals.RestrictedPortals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class CraftingHandler{
    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event){
        if(!ConfigHandler.GENERAL.consumeItems.get()) {
            for (int i = 0; i < RestrictedPortals.idSplit.length; i++) {
                if (event.getPlayer() instanceof EntityPlayerMP && ((EntityPlayerMP)event.getPlayer()).getStats().getValue(RestrictedPortals.portalUnlock[i]) == 0 && event.getCrafting().getItem() == RestrictedPortals.itemList[i].getItem()) {
                    if (!RestrictedPortals.metaUsed[i] || (event.getCrafting().getTag()) == RestrictedPortals.itemList[i].getTag()) {
                        event.getPlayer().addStat(RestrictedPortals.portalUnlock[i], 1);
                        event.getPlayer().sendStatusMessage(new TextComponentTranslation(ConfigHandler.GENERAL.craftedmessage.get().replace("%dim%", RestrictedPortals.nameSplit[i])), false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onUse(PlayerInteractEvent.RightClickBlock event){
        if(ConfigHandler.GENERAL.consumeItems.get()) {
            for (int i = 0; i < RestrictedPortals.idSplit.length; i++) {
                if(event.getEntityPlayer() instanceof EntityPlayerMP && ((EntityPlayerMP)event.getEntityPlayer()).getStats().getValue(RestrictedPortals.portalUnlock[i]) == 0 && event.getItemStack().getItem() == RestrictedPortals.itemList[i].getItem()){
                    if(RestrictedPortals.pblockwhitelist.contains(event.getWorld().getBlockState(event.getPos()).getBlock())){
                        event.getEntityPlayer().getHeldItem(event.getHand()).setCount(event.getItemStack().getCount() - 1);
                        event.getEntityPlayer().addStat(RestrictedPortals.portalUnlock[i], 1);
                        event.getEntityPlayer().sendStatusMessage(new TextComponentTranslation(ConfigHandler.GENERAL.craftedmessage.get().replace("%dim%", RestrictedPortals.nameSplit[i])), false);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}