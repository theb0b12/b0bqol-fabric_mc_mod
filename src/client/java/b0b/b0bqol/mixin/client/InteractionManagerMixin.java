package b0b.b0bqol.mixin.client;

import b0b.b0bqol.B0bqolClient;
import b0b.b0bqol.features.AutoTopSlab;
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
	@Unique private boolean bypassFeatures = false; // Flag to prevent recursion

	@Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
	public void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		// Prevent recursion when we call interactBlock again
		if (bypassFeatures) {
			return;
		}

		// Run all features including AutoTopSlab
		B0bqolClient.featureList.forEach(f -> f.onInteractBlock(player, hand, hitResult, cir));

		// Check if AutoTopSlab prepared a modification
		if (AutoTopSlab.modifiedHitResult != null) {
			BlockHitResult modifiedHit = AutoTopSlab.modifiedHitResult;
			AutoTopSlab.modifiedHitResult = null; // Clear it after use

			// Set bypass flag and call with modified hit result
			bypassFeatures = true;
			try {
				ActionResult result = this.interactBlock(player, hand, modifiedHit);
				cir.setReturnValue(result);
			} finally {
				bypassFeatures = false; // Always reset the flag
			}
		}
	}
}