package dev.fulmineo.elemancy.mixin;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.fulmineo.elemancy.data.ElemancyServerPlayerEntity;
import dev.fulmineo.elemancy.data.Song;
import dev.fulmineo.elemancy.item.AbstractBell;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ElemancyServerPlayerEntity {
	private ElemancySongManager elemancySongMangager = new ElemancySongManager();

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

	class ElemancySongManager {
		private int idleTicks;
		private ItemStack bellStack;
		private Song song;

		public void tick() {
			if (this.song != null) {
				if (this.idleTicks % 5 == 0) {
					this.idleTicks = 0;
					if (ServerPlayerEntityMixin.this.getStackInHand(Hand.MAIN_HAND) == this.bellStack || ServerPlayerEntityMixin.this.getStackInHand(Hand.OFF_HAND) == this.bellStack){
						this.song = this.song.tick(ServerPlayerEntityMixin.this, this.bellStack, true);
					} else {
						this.stop();
					}
				}
				this.idleTicks++;
			}
		}

		public void play(ItemStack bellStack, int songIndex) {
			this.bellStack = bellStack;
			this.song = ((AbstractBell)this.bellStack.getItem()).getSongs(this.bellStack.getOrCreateTag()).get(songIndex);
			this.idleTicks = 0;
		}

		public void stop() {
			this.bellStack = null;
			this.song = null;
		}
	}
}
