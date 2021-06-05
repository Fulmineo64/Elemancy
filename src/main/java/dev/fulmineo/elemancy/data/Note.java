package dev.fulmineo.elemancy.data;

import net.minecraft.nbt.NbtCompound;

public class Note {
	private int delayTicks;
	private int pitchIndex;
	private int currentTicks;
	public NoteAction action;

	public Note(int delayTicks, int pitchIndex) {
		this(delayTicks, pitchIndex, null);
	}

	public Note(int delayTicks, int pitchIndex, NoteAction action) {
		this.delayTicks = delayTicks;
		this.pitchIndex = pitchIndex;
		this.action = action;
	}

	private Note(NbtCompound nbt) {
		this.delayTicks = nbt.getInt("DelayTicks");
		this.pitchIndex = nbt.getInt("PitchIndex");
		this.action = NoteAction.fromNbt(nbt);
	}

	public boolean hasRemainingTicks() {
		return this.delayTicks > this.currentTicks;
	}

	public void tick() {
		this.currentTicks++;
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

	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putInt("DelayTicks", this.delayTicks);
		nbt.putInt("PitchIndex", this.pitchIndex);
		if (this.action != null){
			nbt.put("Action", this.action.writeNbt(new NbtCompound()));
		}
		return nbt;
	}

	public static Note fromNbt(NbtCompound nbt) {
		return new Note(nbt);
	}
}
