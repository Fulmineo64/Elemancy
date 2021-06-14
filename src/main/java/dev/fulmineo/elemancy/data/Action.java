package dev.fulmineo.elemancy.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class Action {
	protected Type type;
	protected int value;

	public Action(Type type) {
		this.type = type;
	}

	public Action(Type type, int value) {
		this.type = type;
		this.value = value;
	}

	public void setType(Type type){
		this.type = type;
	}

	public void setValue(int value){
		this.value = value;
	}

	public Type getType(){
		return this.type;
	}

	public int getValue(){
		return this.value;
	}

	private float getBaseManaCost() {
		switch (this.type) {
			case ADD: {
				return 2;
			}
			case ELEMENT: {
				return 10;
			}
			case MOVE: {
				return 2;
			}
			case REMOVE: {
				return 2;
			}
			default: {
				return 0;
			}
		}
	}

	public float getManaCost() {
		if (this.type == Type.ADD || this.type == Type.MOVE || this.type == Type.REMOVE){
			return this.getBaseManaCost() * this.value;
		}
		return this.getBaseManaCost();
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putInt("Type", this.type.ordinal());
		nbt.putInt("Value", this.value);
		return nbt;
	}

	public static Action fromNbt(NbtCompound nbt) {
		if (nbt.contains("Action", NbtElement.COMPOUND_TYPE)) {
			NbtCompound action = nbt.getCompound("Action");
			return new Action(Type.values()[action.getInt("Type")], action.getInt("Value"));
		} else {
			return null;
		}
	}

	public enum Type {
		ELEMENT,
		INSTRUMENT,
		SONG,
		DIRECTION_TYPE,
		DIRECTION,
		MOVE,
		ADD,
		REMOVE
	}
}
