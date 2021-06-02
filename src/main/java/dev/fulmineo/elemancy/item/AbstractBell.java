package dev.fulmineo.elemancy.item;

import java.util.ArrayList;
import java.util.List;

import dev.fulmineo.elemancy.data.Note;
import dev.fulmineo.elemancy.data.Song;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractBell extends Item {
	public AbstractBell(Settings settings) {
        super(settings);
    }

	@Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
		NbtCompound nbt = itemStack.getOrCreateTag();
        if (world instanceof ServerWorld) {
			int index = nbt.getInt("SongIndex");
			List<Song> songs = this.getSongs(nbt);
			Song selectedSong = songs.size() > index ? songs.get(index) : null;
			if (selectedSong == null) {
				// TODO: Free song
				this.playNote(world, user, new Note(1, 16, 0, Instrument.BELL, null));
			} else {
				// TODO: Play song
			}
        }
        return TypedActionResult.success(itemStack);
    }

	protected List<Song> getSongs(NbtCompound nbt){
		List<Song> songs = new ArrayList<>();
		if (nbt.contains("Songs", NbtElement.LIST_TYPE)) {
			NbtList songsNbt = nbt.getList("Songs", NbtElement.COMPOUND_TYPE);
			for (NbtElement songNbt : songsNbt) {
				songs.add(Song.fromNbt((NbtCompound)songNbt));
			}
		}
		return songs;
	}

	protected void playNote(World world, PlayerEntity user, Note note){
		BlockPos pos = user.getBlockPos();
		world.playSound((PlayerEntity)null, pos, note.getInstrument().getSound(), SoundCategory.PLAYERS, 3.0F, note.getPitch());
		world.addParticle(ParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, (double)note.getPitchIndex() / 24.0D, 0.0D, 0.0D);
	}
}
