package b0b.b0bqol.mixin.client;

import b0b.b0bqol.B0bqolClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ClientPlayerInteractionManager.class)
public abstract class InteractionManagerMixin {
	@Shadow public abstract ActionResult interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult);
	@Unique private boolean interacted = false;

	@Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
	public void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir){
		B0bqolClient.featureList.forEach(f -> f.onInteractBlock(player,hand,hitResult,cir));
	}
}