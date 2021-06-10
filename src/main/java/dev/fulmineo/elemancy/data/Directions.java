package dev.fulmineo.elemancy.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;

public enum Directions {
	DOWN,
	UP,
	FORWARD,
	BACKWARD,
	LEFT,
	RIGHT;

	public Direction getDirection(PlayerEntity player){
		int index = this.ordinal();
		Direction[] directions = Direction.values();
		if (index < 2) {
			return directions[index];
		}
		Direction facing = player.getHorizontalFacing();
		switch (index) {
			case 2: {
				return facing;
			}
			case 3: {
				return facing.rotateYCounterclockwise();
			}
			case 4: {
				return facing.getOpposite();
			}
			case 5: {
				return facing.rotateYClockwise();
			}
		}
		return null;
	}
}
