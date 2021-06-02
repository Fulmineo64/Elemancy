package dev.fulmineo.elemancy.data;

import net.minecraft.block.enums.Instrument;
import net.minecraft.nbt.NbtCompound;

public class Note {
	private int executionIndex;
	private int pitchIndex;
	private int rowIndex;
	private Instrument instrument;
	private Element element;

	public Note(int executionIndex, int pitchIndex, int rowIndex, Instrument instrument, Element element) {
		this.executionIndex = executionIndex;
		this.pitchIndex = pitchIndex;
		this.rowIndex = rowIndex;
		this.instrument = instrument;
		this.element = element;
	}

	private Note(NbtCompound nbt) {
		this.executionIndex = nbt.getInt("ExecutionIndex");
		this.pitchIndex = nbt.getInt("PitchIndex");
		this.rowIndex = nbt.getInt("RowIndex");
		if (nbt.contains("Instrument")) {
			this.instrument = Instrument.values()[nbt.getInt("Instrument")];
		}
		if (nbt.contains("Element")) {
			this.element = Element.values()[nbt.getInt("Element")];
		}
	}

	public float getPitch() {
		return (float)Math.pow(2.0D, (double)(this.pitchIndex - 12) / 12.0D);
	}

	public int getPitchIndex() {
		return this.pitchIndex;
	}

	public Instrument getInstrument() {
		return this.instrument;
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putInt("ExecutionIndex", this.executionIndex);
		nbt.putInt("PitchIndex", this.pitchIndex);
		nbt.putInt("RowIndex", this.rowIndex);
		nbt.putInt("Instrument", this.instrument.ordinal());
		nbt.putInt("Element", this.element.ordinal());
		return nbt;
	}

	public static Note fromNbt(NbtCompound nbt) {
		return new Note(nbt);
	}
}
