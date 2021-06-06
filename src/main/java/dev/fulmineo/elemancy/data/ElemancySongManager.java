package dev.fulmineo.elemancy.data;

import java.util.ArrayList;
import java.util.List;

import dev.fulmineo.elemancy.item.Bell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class ElemancySongManager {
	private ItemStack bellStack;
	private Song song;
	private PlayerEntity player;
	private List<Vec3d> controlledRelativePos = new ArrayList<Vec3d>();

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
			switch (note.action.getType()) {
				case ELEMENT: {
					break;
				}
				case INSTRUMENT: {
					break;
				}
				case SONG: {
					Integer songIndex = note.action.getValue();
					if (songIndex != null) {
						Song song = this.getBell().getSongs(this.bellStack.getOrCreateTag()).get(songIndex);
						if (song != null) {
							song.previous = this.song;
							this.song = song;
						}
					}
					break;
				}
				case ADD: {
					this.controlledRelativePos.add(new Vec3d(0, 0, 0));
				}
			}
		}
		note = this.getNote();
		if (note != null) {
			this.handleNote(note);
		}
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

	public void play(ItemStack bellStack, int songIndex) {
		this.bellStack = bellStack;
		this.song = ((Bell)this.bellStack.getItem()).getSongs(this.bellStack.getOrCreateTag()).get(songIndex);
	}

	public void stop() {
		this.bellStack = null;
		this.song = null;
		this.controlledRelativePos.clear();
	}

	public boolean isActive(){
		return this.song != null;
	}
}