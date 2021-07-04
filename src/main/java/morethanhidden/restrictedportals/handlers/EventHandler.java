package morethanhidden.restrictedportals.handlers;

import morethanhidden.restrictedportals.RestrictedPortals;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.UUID;

public class EventHandler {

    private HashMap<UUID, Long> sentMessage = new HashMap<>();

    @SubscribeEvent
    public void onPlayerChangeDim(EntityTravelToDimensionEvent event){

        if(event.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity playerMP = (ServerPlayerEntity) event.getEntity();
            for (int i = 0; i < RestrictedPortals.nameSplit.length; i++) {
                if (event.getDimension() == RegistryKey.create(Registry.DIMENSION_REGISTRY, RestrictedPortals.dimResSplit.get(i))
                        && !playerMP.getAdvancements().getOrStartProgress(RestrictedPortals.advancements[i]).isDone()) {
                    //Prevent Spam (Only send message when interval is greater then 40 ticks and new servers are negative for some reason)
                    if(!sentMessage.containsKey(playerMP.getUUID())
                            || (playerMP.level.getGameTime() - sentMessage.get(playerMP.getUUID())) > 40
                            || (playerMP.level.getGameTime() - sentMessage.get(playerMP.getUUID())) < 0) {
                        String item = new ItemStack(GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(RestrictedPortals.itemSplit[i]))).getDisplayName().getString();
                        if(!playerMP.level.isClientSide)
                            playerMP.displayClientMessage(new TranslationTextComponent(ConfigHandler.GENERAL.blockedmessage.get().replace("%item%", item).replace("%dim%", RestrictedPortals.nameSplit[i])), false);
                        sentMessage.put(playerMP.getUUID(), playerMP.level.getGameTime());
                    }
                    //Prevent Death by Lava for End Portal
                    if(event.getDimension() == RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("the_end")) && ConfigHandler.GENERAL.preventEPDeath.get()){
                        BlockPos coordinates = playerMP.getRespawnPosition();
                        if (coordinates == null){ coordinates = ((ServerWorld)playerMP.level).getSharedSpawnPos(); }
                        playerMP.setPos(coordinates.getX(), coordinates.getY(), coordinates.getZ());
                    }
                    event.setCanceled(true);
                }
            }
        }
    }

}
