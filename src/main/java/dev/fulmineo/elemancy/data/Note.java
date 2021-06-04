package dev.fulmineo.elemancy.data;

import net.minecraft.block.enums.Instrument;
import net.minecraft.nbt.NbtCompound;

public class Note {
	private int delayTicks;
	private int pitchIndex;
	// TODO: Replace the various properties with this one!
	// private NoteAction action;
	private Integer songIndex;
	private Element element;
	private Instrument instrument;

	public Note(int delayTicks, int pitchIndex) {
		this(delayTicks, pitchIndex, null);
	}

	public Note(int delayTicks, int pitchIndex, Integer songIndex) {
		this(delayTicks, pitchIndex, songIndex, null);
	}

	public Note(int delayTicks, int pitchIndex, Integer songIndex, Element element) {
		this(delayTicks, pitchIndex, songIndex, element, null);
	}

	public Note(int delayTicks, int pitchIndex, Integer songIndex, Element element, Instrument instrument) {
		this.delayTicks = delayTicks;
		this.pitchIndex = pitchIndex;
		this.songIndex = songIndex;
		this.element = element;
		this.instrument = instrument;
	}

	private Note(NbtCompound nbt) {
		this.delayTicks = nbt.getInt("DelayTicks");
		this.pitchIndex = nbt.getInt("PitchIndex");
		if (nbt.contains("SongIndex")) {
			this.songIndex = nbt.getInt("SongIndex");
		}
		if (nbt.contains("Instrument")) {
			this.instrument = Instrument.values()[nbt.getInt("Instrument")];
		}
		if (nbt.contains("Element")) {
			this.element = Element.values()[nbt.getInt("Element")];
		}
	}

	public int getDelay() {
		return this.delayTicks;
	}

	public float getPitch() {
		return (float)Math.pow(2.0D, (double)(this.pitchIndex - 12) / 12.0D);
	}

	public int getPitchIndex() {
		return this.pitchIndex;
	}

	public Integer getSongIndex() {
		return this.songIndex;
	}

	public Instrument getInstrument() {
		return this.instrument;
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putInt("DelayTicks", this.delayTicks);
		nbt.putInt("PitchIndex", this.pitchIndex);
		if (this.songIndex != null) {
			nbt.putInt("SongIndex", this.songIndex);
		}
		nbt.putInt("Instrument", this.instrument.ordinal());
		nbt.putInt("Element", this.element.ordinal());
		return nbt;
	}

	public static Note fromNbt(NbtCompound nbt) {
		return new Note(nbt);
	}
}
