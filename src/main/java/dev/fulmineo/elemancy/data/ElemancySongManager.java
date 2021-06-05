package dev.fulmineo.elemancy.data;

import dev.fulmineo.elemancy.Elemancy;
import dev.fulmineo.elemancy.item.Bell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ElemancySongManager {
	private ItemStack bellStack;
	private Song song;
	private PlayerEntity player;


	public ElemancySongManager(PlayerEntity player){
		this.player = player;
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
		if (this.song == null) return null;
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
			// TODO
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
			}
		}
		note = this.getNote();
		if (note != null) {
			this.handleNote(note);
		}
	}

	private void playNote(Note note){
		Elemancy.info("note = "+note.getPitchIndex()+" tick = "+this.player.world.getTime());
		if (note.getPitchIndex() > 0){
			Bell bell = this.getBell();
			bell.playNote(this.player, note, bell.getCurrentInstrument());
		}
	}

	public Bell getBell() {
		return ((Bell)this.bellStack.getItem());
	}

	public void play(ItemStack bellStack, int songIndex) {
		this.bellStack = bellStack;
		this.song = ((Bell)this.bellStack.getItem()).getSongs(this.bellStack.getOrCreateTag()).get(songIndex);
	}

	public void stop() {
		this.bellStack = null;
		this.song = null;
	}
}