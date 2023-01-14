package morethanhidden.restrictedportals.mixins;

import morethanhidden.restrictedportals.AdvancementHelper;
import morethanhidden.restrictedportals.RPCommon;
import morethanhidden.restrictedportals.platform.services.Services;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mixin(ServerPacksSource.class)
public class MinecraftServerMixin {

    @Inject(method = "createPackRepository(Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;)Lnet/minecraft/server/packs/repository/PackRepository;", at = @At("TAIL"))
    private static void createPackRepository(LevelStorageSource.LevelStorageAccess storageAccess, CallbackInfoReturnable<PackRepository> cir) {
        String path = storageAccess.getLevelPath(LevelResource.DATAPACK_DIR).toString();

        AdvancementHelper.CreateDatapack(path);
        AdvancementHelper.ClearCustomAdvancements(path);

        RPCommon.nameSplit = Services.PLATFORM.getConfigDimensionsNames().split(",");
        RPCommon.dimResSplit = Arrays.stream(Services.PLATFORM.getConfigDimensionsResourceNames().split(",")).map(String::toLowerCase).map(ResourceLocation::new).collect(Collectors.toList());
        RPCommon.itemSplit = Services.PLATFORM.getConfigCraftItems().split(",");

        RPCommon.advancements = new Advancement[RPCommon.nameSplit.length];

        //Get Advancements from Config
        for (int i = 0; i < RPCommon.nameSplit.length; i++) {
            AdvancementHelper.AddCustomAdvancement(
                    Services.PLATFORM.getConfigCraftedMessage().replace("%dim%", RPCommon.nameSplit[i]),
                    Services.PLATFORM.getConfigDescription().replace("%dim%", RPCommon.nameSplit[i]).replace("%item%", Component.translatable(BuiltInRegistries.ITEM.get(new ResourceLocation(RPCommon.itemSplit[i])).getDescriptionId()).getString()),
                    RPCommon.itemSplit[i],
                    RPCommon.nameSplit[i].toLowerCase().replace(" ",""),
                    path
            );
        }
    }
}
