package dev.fulmineo.elemancy.data;

import net.minecraft.util.math.Direction;

public enum Directions {
	DOWN,
	UP,
	FORWARD,
	BACKWARD,
	LEFT,
	RIGHT;

	public Direction getDirection(Direction startingDirection){
		int index = this.ordinal();
		Direction[] directions = Direction.values();
		if (index < 2) {
			return directions[index];
		}
		switch (index) {
			case 2: {
				return startingDirection;
			}
			case 3: {
				return startingDirection.getOpposite();
			}
			case 4: {
				return startingDirection.rotateYCounterclockwise();
			}
			case 5: {
				return startingDirection.rotateYClockwise();
			}
		}
		return null;
	}
}
