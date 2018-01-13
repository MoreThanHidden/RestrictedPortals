package morethanhidden.restrictedportals.handlers;

import morethanhidden.restrictedportals.RestrictedPortals;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CraftingHandler
{
    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event){
        if(!RestrictedPortals.consumed) {
            for (int i = 0; i < RestrictedPortals.idSplit.length; i++) {
                if (event.player instanceof EntityPlayerMP && ((EntityPlayerMP)event.player).getStatFile().readStat(RestrictedPortals.portalUnlock[i]) == 0 && event.crafting.getItem() == RestrictedPortals.itemList[i].getItem()) {
                    if (!RestrictedPortals.metaUsed[i] || (event.crafting.getMetadata() == RestrictedPortals.itemList[i].getMetadata())) {
                        event.player.addStat(RestrictedPortals.portalUnlock[i], 1);
                        event.player.sendStatusMessage(new TextComponentTranslation(RestrictedPortals.craftedmessage.replace("%dim%", RestrictedPortals.nameSplit[i])), false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onUse(PlayerInteractEvent.RightClickBlock event){
        if(RestrictedPortals.consumed) {
            for (int i = 0; i < RestrictedPortals.idSplit.length; i++) {
                if(event.getEntityPlayer() instanceof EntityPlayerMP && ((EntityPlayerMP)event.getEntityPlayer()).getStatFile().readStat(RestrictedPortals.portalUnlock[i]) == 0 && event.getItemStack().getItem() == RestrictedPortals.itemList[i].getItem()){
                    if(RestrictedPortals.pblockwhitelist.contains(event.getWorld().getBlockState(event.getPos()).getBlock())){
                        event.getEntityPlayer().getHeldItem(event.getHand()).setCount(event.getItemStack().getCount() - 1);
                        event.getEntityPlayer().addStat(RestrictedPortals.portalUnlock[i], 1);
                        event.getEntityPlayer().sendStatusMessage(new TextComponentTranslation(RestrictedPortals.craftedmessage.replace("%dim%", RestrictedPortals.nameSplit[i])), false);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}