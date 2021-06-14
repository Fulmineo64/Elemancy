package dev.fulmineo.elemancy.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.fulmineo.elemancy.data.Action;
import dev.fulmineo.elemancy.data.DirectionType;
import dev.fulmineo.elemancy.data.Directions;
import dev.fulmineo.elemancy.data.Element;
import dev.fulmineo.elemancy.data.Note;
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
				new Note(0, 6, new Action(Action.Type.DIRECTION_TYPE, DirectionType.DYNAMIC.ordinal())),
				new Note(4, 8, new Action(Action.Type.MOVE, 1)),
				new Note(4, 10, new Action(Action.Type.ADD, 4)),
				new Note(4, 0, new Action(Action.Type.SONG, 1)),
				new Note(2000, 12, new Action(Action.Type.ELEMENT, Element.EARTH.ordinal())),
				new Note(4, 14, new Action(Action.Type.ELEMENT, Element.EARTH.ordinal()))
			)
		));
		songs.add(new Song(
			Arrays.asList(
				new Note(0, 8, new Action(Action.Type.DIRECTION, Directions.UP.ordinal())),
				new Note(4, 10, new Action(Action.Type.ADD, 4)),
				new Note(0, 8, new Action(Action.Type.DIRECTION, Directions.LEFT.ordinal())),
				new Note(4, 10, new Action(Action.Type.ADD, 4)),
				new Note(0, 8, new Action(Action.Type.DIRECTION, Directions.DOWN.ordinal())),
				new Note(4, 10, new Action(Action.Type.ADD, 4))
			)
		));
		return songs;
	}

}
