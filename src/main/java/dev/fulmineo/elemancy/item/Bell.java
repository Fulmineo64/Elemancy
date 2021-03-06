package dev.fulmineo.elemancy.item;

import java.util.ArrayList;
import java.util.List;

import dev.fulmineo.elemancy.data.ElemancyPlayerEntity;
import dev.fulmineo.elemancy.data.Note;
import dev.fulmineo.elemancy.data.Song;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Bell extends Item {
	protected Instrument defaultInstrument;

	public Bell(Settings settings, Instrument defaultInstrument) {
        super(settings);
		this.defaultInstrument = defaultInstrument;
    }

	@Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
		NbtCompound nbt = itemStack.getOrCreateTag();
        // if (world instanceof ServerWorld) {
			int index = nbt.getInt("SongIndex");
			((ElemancyPlayerEntity)user).startPlayingSong(itemStack, index);
        // }
        return TypedActionResult.success(itemStack);
    }

	public void playNote(Entity user, Note note, Instrument instrument){
		if (note.getPitchIndex() == 0) return;
		BlockPos pos = user.getBlockPos();
		if (user.world.isClient){
			user.world.addParticle(ParticleTypes.NOTE, user.getParticleX(0.6D), user.getRandomBodyY(), user.getParticleZ(0.6D), (double)note.getPitchIndex() / 24.0D, 0.0D, 0.0D);
		} else {
			user.world.playSound(null, pos, instrument.getSound(), SoundCategory.PLAYERS, 3.0F, note.getPitch());
		}
	}

	public List<Song> getSongs(NbtCompound nbt){
		List<Song> songs = new ArrayList<>();
		if (nbt.contains("Songs", NbtElement.LIST_TYPE)) {
			NbtList songsNbt = nbt.getList("Songs", NbtElement.COMPOUND_TYPE);
			for (NbtElement songNbt : songsNbt) {
				songs.add(Song.fromNbt((NbtCompound)songNbt));
			}
		}
		return songs;
	}

	// TODO: Extend both of these on the cacophonous bell

	public Instrument getCurrentInstrument() {
		return this.defaultInstrument;
	}

	public void setCurrentInstrument(Instrument instrument) { }
}
