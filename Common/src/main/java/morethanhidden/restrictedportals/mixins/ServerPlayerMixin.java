package morethanhidden.restrictedportals.mixins;

import morethanhidden.restrictedportals.RPCommon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "changeDimension(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/world/entity/Entity;", at = @At("HEAD"), cancellable = true)
    private void injected(ServerLevel serverLevel, CallbackInfoReturnable<Entity> cir){
        if(RPCommon.blockPlayerFromTransit( (ServerPlayer)(Object)this, serverLevel.dimension())){
            cir.setReturnValue(null);
        }
    }
}
