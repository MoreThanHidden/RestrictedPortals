package morethanhidden.restrictedportals.handlers;

import morethanhidden.restrictedportals.RestrictedPortals;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.UUID;

public class EventHandler {

    private HashMap<UUID, Long> sentMessage = new HashMap<>();

    @SubscribeEvent
    public void onPlayerChangeDim(EntityTravelToDimensionEvent event){

        if(event.getEntity() instanceof ServerPlayer) {
            ServerPlayer playerMP = (ServerPlayer) event.getEntity();
            for (int i = 0; i < RestrictedPortals.nameSplit.length; i++) {
                if (event.getDimension() == ResourceKey.create(Registry.DIMENSION_REGISTRY, RestrictedPortals.dimResSplit.get(i))
                        && !playerMP.getAdvancements().getOrStartProgress(RestrictedPortals.advancements[i]).isDone()) {
                    //Prevent Spam (Only send message when interval is greater then 40 ticks and new servers are negative for some reason)
                    if(!sentMessage.containsKey(playerMP.getUUID())
                            || (playerMP.level.getGameTime() - sentMessage.get(playerMP.getUUID())) > 40
                            || (playerMP.level.getGameTime() - sentMessage.get(playerMP.getUUID())) < 0) {
                        String item = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RestrictedPortals.itemSplit[i]))).getDisplayName().getString();
                        if(!playerMP.level.isClientSide)
                            playerMP.displayClientMessage(new TranslatableComponent(ConfigHandler.GENERAL.blockedmessage.get().replace("%item%", item).replace("%dim%", RestrictedPortals.nameSplit[i])), false);
                        sentMessage.put(playerMP.getUUID(), playerMP.level.getGameTime());
                    }
                    //Prevent Death by Lava for End Portal
                    if(event.getDimension() == ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("the_end")) && ConfigHandler.GENERAL.preventEPDeath.get()){
                        BlockPos coordinates = playerMP.getRespawnPosition();
                        if (coordinates == null){ coordinates = ((ServerLevel)playerMP.level).getSharedSpawnPos(); }
                        playerMP.setPos(coordinates.getX(), coordinates.getY(), coordinates.getZ());
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

}
