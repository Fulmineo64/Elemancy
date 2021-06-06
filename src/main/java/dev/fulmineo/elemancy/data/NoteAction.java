package dev.fulmineo.elemancy.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;

public class NoteAction {
	private Type type;
	private int value;
	private Direction direction;

	public NoteAction(Type type, int value) {
		this.type = type;
		this.value = value;
	}

	public void setType(Type type){
		this.type = type;
	}

	public void setValue(int value){
		this.value = value;
	}

	public void setDirection(Direction direction){
		this.direction = direction;
	}

	public Type getType(){
		return this.type;
	}

	public int getValue(){
		return this.value;
	}

	public Direction setDirection(){
		return this.direction;
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putInt("Type", this.type.ordinal());
		nbt.putInt("Value", this.value);
		return nbt;
	}

	public static NoteAction fromNbt(NbtCompound nbt) {
		if (nbt.contains("Action", NbtElement.COMPOUND_TYPE)) {
			NbtCompound action = nbt.getCompound("Action");
			return new NoteAction(Type.values()[action.getInt("Type")], action.getInt("Value"));
		} else {
			return null;
		}
	}

	public enum Type {
		ELEMENT,
		INSTRUMENT,
		SONG,
		ADD,
		REMOVE,
		EXTEND
	}
}
