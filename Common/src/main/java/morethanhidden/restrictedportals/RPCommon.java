package morethanhidden.restrictedportals;

import com.google.common.collect.Lists;
import morethanhidden.restrictedportals.platform.services.Services;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RPCommon {

	public static String[] nameSplit;
    public static List<ResourceLocation> dimResSplit;
    public static String[] itemSplit;
    public static Advancement[] advancements;

    private static HashMap<UUID, Long> sentMessage = new HashMap<>();

    /**
     * Method called when the Minecraft server is starting up.
     * @param server the Minecraft server instance
     */
	public static void onServerStarting(MinecraftServer server){
        //Get the path to the datapack folder
        String path = server.getWorldPath(LevelResource.DATAPACK_DIR).toString();

        AdvancementHelper.CreateDatapack(path);
        AdvancementHelper.ClearCustomAdvancements(path);

        //Get Config Values
        RPCommon.nameSplit = Services.PLATFORM.getConfigDimensionsNames().split(",");
        RPCommon.dimResSplit = Arrays.stream(Services.PLATFORM.getConfigDimensionsResourceNames().split(",")).map(String::toLowerCase).map(ResourceLocation::new).collect(Collectors.toList());
        RPCommon.itemSplit = Services.PLATFORM.getConfigCraftItems().split(",");

        RPCommon.advancements = new Advancement[RPCommon.nameSplit.length];

        //Get Advancements from Config
        for (int i = 0; i < RPCommon.nameSplit.length; i++) {
            AdvancementHelper.AddCustomAdvancement(
                    Services.PLATFORM.getConfigCraftedMessage().replace("%dim%", RPCommon.nameSplit[i]),
                    Services.PLATFORM.getConfigDescription().replace("%dim%", RPCommon.nameSplit[i]).replace("%item%", Component.translatable(Registry.ITEM.get(new ResourceLocation(RPCommon.itemSplit[i])).getDescriptionId()).getString()),
                    RPCommon.itemSplit[i],
                    RPCommon.nameSplit[i].toLowerCase().replace(" ",""),
                    path
            );
        }

        //Get Datapacks
        PackRepository resourcepacklist = server.getPackRepository();
        resourcepacklist.reload();
        List<Pack> list = Lists.newArrayList(resourcepacklist.getSelectedPacks());

        //Enable the Restricted Portals Dynamic Datapack
        Pack restrictedPortalsDatapack = resourcepacklist.getPack("file/restrictedportals");
        if(!list.contains(restrictedPortalsDatapack)) {
            list.add(2, restrictedPortalsDatapack);
        }

        //Reload Datapacks
        server.reloadResources(list.stream().map(Pack::getId).collect(Collectors.toList())).exceptionally(ex -> null);

	    //Put the advancements into the array
        for (int i = 0; i < nameSplit.length; i++) {
            advancements[i] = server.getAdvancements().getAdvancement(new ResourceLocation("restrictedportals:" + RPCommon.nameSplit[i].toLowerCase().replace(" ", "")));
        }
    }

    /**
     * Called when a player tries to Teleport to another dimension
     * returns true if they should be blocked or false if not.
     * @param player the Player attempting to teleport
     * @param dimension the Dimension the player is attempting to enter.
     */
    public static boolean blockPlayerFromTransit(Entity player, ResourceKey<Level> dimension){
        if(player instanceof ServerPlayer playerMP) {
            for (int i = 0; i < RPCommon.nameSplit.length; i++) {
                if (dimension == ResourceKey.create(Registry.DIMENSION_REGISTRY, RPCommon.dimResSplit.get(i))
                        && !playerMP.getAdvancements().getOrStartProgress(RPCommon.advancements[i]).isDone()) {
                    //Prevent Spam (Only send message when interval is greater then 40 ticks and new servers are negative for some reason)
                    if(!sentMessage.containsKey(playerMP.getUUID())
                            || (playerMP.level.getGameTime() - sentMessage.get(playerMP.getUUID())) > 40
                            || (playerMP.level.getGameTime() - sentMessage.get(playerMP.getUUID())) < 0) {
                        String item = new ItemStack(Registry.ITEM.get(new ResourceLocation(RPCommon.itemSplit[i]))).getDisplayName().getString();
                        if(!playerMP.level.isClientSide)
                            playerMP.displayClientMessage(Component.translatable(Services.PLATFORM.getConfigBlockedMessage().replace("%item%", item).replace("%dim%", RPCommon.nameSplit[i])), false);
                        sentMessage.put(playerMP.getUUID(), playerMP.level.getGameTime());
                    }
                    //Prevent Death by Lava for End Portal
                    if(dimension == ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("the_end")) && Services.PLATFORM.getConfigPreventEPDeath()){
                        BlockPos coordinates = playerMP.getRespawnPosition();
                        if (coordinates == null){ coordinates = playerMP.level.getSharedSpawnPos(); }
                        playerMP.setPos(coordinates.getX(), coordinates.getY(), coordinates.getZ());
                    }
                    return true;
                }
            }
        }
        return false;
    }

}


