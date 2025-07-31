package b0b.b0bqol.mixin.client;

import b0b.b0bqol.features.Features;
import b0b.b0bqol.features.PlacementHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    // This can be a @Redirect call, but for at least somewhat better compatibility it is a WrapOperation
    @WrapOperation(method = "doItemUse()V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;" +
                    "interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;" +
                    "Lnet/minecraft/util/Hand;" +
                    "Lnet/minecraft/util/hit/BlockHitResult;" +
                    ")Lnet/minecraft/util/ActionResult;"
    ))
    private ActionResult wrapInteractBlock(
            ClientPlayerInteractionManager instance,
            ClientPlayerEntity player,
            Hand hand,
            BlockHitResult hitResult,
            Operation<ActionResult> original) {
        if (Features.AUTO_DOWNWARD_HOPPER.shouldModify(player) || Features.AUTO_TOP_SLAB.shouldModify(player))
            return PlacementHandler.handlePlaceBlock(instance, player, hand, hitResult);
        return original.call(instance, player, hand, hitResult);
    }

}
