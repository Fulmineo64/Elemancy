package dev.fulmineo.elemancy.data;

import java.util.ArrayList;
import java.util.List;

import dev.fulmineo.elemancy.item.Bell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class ElemancySongManager {
	private ItemStack bellStack;
	private Song song;
	private PlayerEntity player;
	private List<Vec3d> relativePostions = new ArrayList<Vec3d>();
	private DirectionType directionType;
	private Direction direction;
	private Vec3d offset = Vec3d.ZERO;
	private Element element;
	private Direction startingDirection;
	private float manaCost;

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

				this.manaCost = 0;
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
		this.manaCost = this.getManaCost(note);
		// TODO: Check if enough mana is present, if not stop the song
		if (this.manaCost > 100) {
			this.stop();
			return;
		}
		this.playNote(note);
		if (note.action != null){
			int value = note.action.getValue();
			switch (note.action.getType()) {
				case ELEMENT: {
					this.addElement(Element.values()[value]);
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
						if (!this.relativePostions.contains(this.offset)) {
							this.relativePostions.add(this.offset);
						}
						this.move(1);
					}
					break;
				}
				case REMOVE: {
					for (int i = 0; i < value; i++){
						this.relativePostions.remove(this.offset);
						this.move(1);
					}
					break;
				}
				case DIRECTION_TYPE: {
					this.directionType = DirectionType.values()[value];
					break;
				}
				case DIRECTION: {
					this.direction = Directions.values()[value].getDirection(this.startingDirection);
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

	private void addElement(Element element){
		if (this.element == null) {
			this.element = element;
		} else {
			this.castSpell(this.element, element);
			this.element = null;
		}
	}

	private float getManaCost(Note note) {
		return note.action.getManaCost();
	}

	private void castSpell(Element primaryElement, Element sencondaryElement) {
		if (primaryElement == Element.EARTH && sencondaryElement == Element.EARTH) {
			List<BlockPos> positions = this.getPositions();
			for (BlockPos position: positions) {
				// TODO: Replace grass and other no-collision blocks!
				this.placeBlockAtPosition(Blocks.DIRT.getDefaultState(), position);
			}
		}
	}

	private void placeBlockAtPosition(BlockState state, BlockPos position){
		BlockState blockState = this.player.world.getBlockState(position);
		if (blockState.isAir()) {
			this.player.world.setBlockState(position, state, Block.NOTIFY_ALL);
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

	public List<BlockPos> getPositions() {
		BlockPos playerPosition = player.getBlockPos();
		List<BlockPos> positions = new ArrayList<BlockPos>();
		Direction lookingDirection = this.player.getHorizontalFacing();
		for (Vec3d relativePosition : this.relativePostions){
			if (this.directionType == DirectionType.DYNAMIC && this.startingDirection != lookingDirection) {
				Vec3d rotatedPosition = relativePosition.rotateY((this.startingDirection.asRotation() - lookingDirection.asRotation()) * ((float)Math.PI/180));
				positions.add(playerPosition.add(rotatedPosition.x, rotatedPosition.y, rotatedPosition.z));
			} else {
				positions.add(playerPosition.add(relativePosition.x, relativePosition.y, relativePosition.z));
			}
		}
		return positions;
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
		this.startingDirection = this.direction;
	}

	public void stop() {
		this.bellStack = null;
		this.song = null;
		this.direction = null;
		this.offset = Vec3d.ZERO;
		this.relativePostions.clear();
	}

	public boolean isActive(){
		return this.song != null;
	}
}