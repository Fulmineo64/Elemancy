package dev.fulmineo.elemancy.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.fulmineo.elemancy.data.Note;
import dev.fulmineo.elemancy.data.Song;
import net.minecraft.block.enums.Instrument;
import net.minecraft.nbt.NbtCompound;

public class BoneBell extends AbstractBell {
	protected Instrument instrument = Instrument.BANJO;

	public BoneBell(Settings settings) {
        super(settings, Instrument.XYLOPHONE);
    }

	public List<Song> getSongs(NbtCompound nbt){
		List<Song> songs = new ArrayList<>();
		songs.add(new Song(
			Arrays.asList(
				new Note(0, 2),
				new Note(1, 4),
				new Note(1, 0, 1),
				new Note(1, 10)
			)
		));
		songs.add(new Song(
			Arrays.asList(
				new Note(0, 6),
				new Note(1, 8)
			)
		));
		return songs;
	}

}
