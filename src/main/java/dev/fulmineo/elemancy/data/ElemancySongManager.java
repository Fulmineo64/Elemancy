package dev.fulmineo.elemancy.data;

import java.util.ArrayList;
import java.util.List;

import dev.fulmineo.elemancy.Elemancy;
import dev.fulmineo.elemancy.item.Bell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ElemancySongManager {
	private ItemStack bellStack;
	private Song song;
	private PlayerEntity player;
	private List<Vec3d> controlledRelativePos = new ArrayList<Vec3d>();
	private DirectionType directionType;
	private Direction direction;
	private Vec3d offset = Vec3d.ZERO;

	public ElemancySongManager(LivingEntity player){
		this.player = (PlayerEntity)player;
	}

	public void tick() {
		if (this.song != null) {
			if (this.player.getStackInHand(Hand.MAIN_HAND) == this.bellStack || this.player.getStackInHand(Hand.OFF_HAND) == this.bellStack){
				Note note = this.getNote();
				if (note == null) {
					this.stop();
					return;
				}

				note.tick();
				this.handleNote(note);
			} else {
				this.stop();
			}
		}
	}

	private Note getNote() {
		if (this.song == null) {
			this.stop();
			return null;
		}
		Note note = this.song.getNote();
		if (note == null) {
			this.song = this.song.previous;
			return this.getNote();
		}
		return note;
	}

	private void handleNote(Note note){
		if (note.hasRemainingTicks()) return;
		this.song.nextNote();
		this.playNote(note);
		if (note.action != null){
			int value = note.action.getValue();
			switch (note.action.getType()) {
				case ELEMENT: {
					break;
				}
				case INSTRUMENT: {
					break;
				}
				case SONG: {
					Song song = this.getBell().getSongs(this.bellStack.getOrCreateTag()).get(value);
					if (song != null) {
						song.previous = this.song;
						this.song = song;
					}
					break;
				}
				case ADD: {
					for (int i = 0; i < value; i++){
						this.controlledRelativePos.add(this.offset);
						this.move(1);
					}
					break;
				}
				case DIRECTION_TYPE: {
					this.directionType = DirectionType.values()[value];
					break;
				}
				case DIRECTION: {
					this.direction = Directions.values()[value].getDirection(player);
					break;
				}
				case MOVE: {
					this.move(value);
					break;
				}
			}
		}
		note = this.getNote();
		if (note != null) {
			this.handleNote(note);
		}
	}

	private void move(int value) {
		this.offset = this.offset.add((double)(this.direction.getOffsetX() * value), (double)(this.direction.getOffsetY() * value), (double)(this.direction.getOffsetZ() * value));
	}

	private void playNote(Note note){
		if (note.getPitchIndex() > 0){
			Bell bell = this.getBell();
			bell.playNote(this.player, note, bell.getCurrentInstrument());
		}
	}

	public Bell getBell() {
		return ((Bell)this.bellStack.getItem());
	}

	public List<Vec3d> getRelativePos() {
		return this.controlledRelativePos;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public DirectionType getDirectionType() {
		return this.directionType;
	}

	public void play(ItemStack bellStack, int songIndex) {
		this.stop();
		this.bellStack = bellStack;
		this.song = ((Bell)this.bellStack.getItem()).getSongs(this.bellStack.getOrCreateTag()).get(songIndex);
		this.direction = player.getHorizontalFacing();
	}

	public void stop() {
		this.bellStack = null;
		this.song = null;
		this.direction = null;
		this.offset = Vec3d.ZERO;
		this.controlledRelativePos.clear();
	}

	public boolean isActive(){
		return this.song != null;
	}
}