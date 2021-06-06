package dev.fulmineo.elemancy.mixin;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.fulmineo.elemancy.data.ElemancyPlayerEntity;
import dev.fulmineo.elemancy.data.ElemancySongManager;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ElemancyPlayerEntity {
	protected ElemancySongManager elemancySongMangager = new ElemancySongManager(this);

	public PlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(EntityType.PLAYER, world);
	}

	@Inject(at = @At("TAIL"), method = "tick()V")
	public void tick(CallbackInfo ci) {
		this.elemancySongMangager.tick();
	}

	public void startPlayingSong(ItemStack bellStack, int songIndex) {
		this.elemancySongMangager.play(bellStack, songIndex);
	}

	public void stopPlayingSong() {
		this.elemancySongMangager.stop();
	}

	public ElemancySongManager getSongManager() {
		return this.elemancySongMangager;
	}
}
