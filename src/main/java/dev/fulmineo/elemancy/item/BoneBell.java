package dev.fulmineo.elemancy.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.fulmineo.elemancy.data.DirectionType;
import dev.fulmineo.elemancy.data.Note;
import dev.fulmineo.elemancy.data.NoteAction;
import dev.fulmineo.elemancy.data.Song;
import net.minecraft.block.enums.Instrument;
import net.minecraft.nbt.NbtCompound;

public class BoneBell extends Bell {
	public BoneBell(Settings settings) {
        super(settings, Instrument.XYLOPHONE);
    }

	public List<Song> getSongs(NbtCompound nbt){
		List<Song> songs = new ArrayList<>();
		songs.add(new Song(
			Arrays.asList(
				new Note(0, 2, new NoteAction(NoteAction.Type.DIRECTION_TYPE, DirectionType.DYNAMIC.ordinal())),
				new Note(4, 4, new NoteAction(NoteAction.Type.ADD, 1)),
				new Note(4, 0, new NoteAction(NoteAction.Type.SONG, 1)),
				new Note(4, 10, new NoteAction(NoteAction.Type.ADD, 1)),
				new Note(20000, 12)
			)
		));
		songs.add(new Song(
			Arrays.asList(
				new Note(0, 6, new NoteAction(NoteAction.Type.ADD, 1)),
				new Note(4, 8, new NoteAction(NoteAction.Type.ADD, 1))
			)
		));
		return songs;
	}

}
