package dev.fulmineo.elemancy.mixin;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.fulmineo.elemancy.data.ElemancyServerPlayerEntity;
import dev.fulmineo.elemancy.data.ElemancySongManager;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ElemancyServerPlayerEntity {
	private ElemancySongManager elemancySongMangager = new ElemancySongManager(this);

	public ServerPlayerEntityMixin(MinecraftServer server, ServerWorld world, GameProfile profile) {
		super(world, world.getSpawnPos(), world.getSpawnAngle(), profile);
	}

	@Inject(at = @At("TAIL"), method = "tick()V")
	public void tick(CallbackInfo ci) {
		if (!this.world.isClient) {
			this.elemancySongMangager.tick();
		}
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
