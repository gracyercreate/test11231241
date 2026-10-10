package com.rankyouressence.mixin;

import com.rankyouressence.RankDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, require = 0)
    private void rankyouressence$addIcon(CallbackInfoReturnable<Component> cir) {
        cir.setReturnValue(RankDisplay.decorate((Entity) (Object) this, cir.getReturnValue()));
    }
}
