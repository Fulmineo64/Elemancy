package dev.fulmineo.elemancy.data;

import java.util.List;

import dev.fulmineo.elemancy.Elemancy;
import dev.fulmineo.elemancy.item.AbstractBell;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class Song {
	public List<Note> notes;
	public int delayTicks;
	public int noteIndex;
	public boolean done;
	public Song previous;

	public Song(){}

	public Song(List<Note> notes){
		this.notes = notes;
	}

	private Song(NbtCompound nbt) {
		if (nbt.contains("Notes", NbtElement.LIST_TYPE)) {
			NbtList notesNbt = nbt.getList("Notes", NbtElement.COMPOUND_TYPE);
			for (NbtElement noteNbt : notesNbt) {
				notes.add(Note.fromNbt((NbtCompound)noteNbt));
			}
		}
	}

	public void reset() {
		this.delayTicks = 0;
		this.noteIndex = 0;
		this.previous = null;
	}

	public Song tick(Entity user, ItemStack bellItemStack, boolean handleDelay) {
		if (this.done) {
			return null;
		} else {
			Elemancy.info("message");
			Note note = this.getNextNote();
			if (handleDelay) {
				if (note == null) {
					this.delayTicks++;
				} else {
					this.delayTicks = 0;
				}
			}
			// TODO: Double the mana cost for each note played on the same tick, this should persist between song. The act of changing a song should not be doubled.
			while (note != null) {
				AbstractBell bell = ((AbstractBell)bellItemStack.getItem());
				bell.playNote(user, note);
				Integer songIndex = note.getSongIndex();
				if (songIndex != null) {
					Song song = bell.getSongs(bellItemStack.getOrCreateTag()).get(songIndex);
					if (song != null) {
						song.previous = this;
						return song.tick(user, bellItemStack, false);
					}
				}
				note = this.getNextNote();
			}
			if (this.done ) {
				return this.previous != null ? this.previous.tick(user, bellItemStack, false) : null;
			} else {
				return this;
			}
		}
	}

	public Note getNextNote() {
		if (!this.done){
			Note note = this.notes.get(this.noteIndex);
			if (note.getDelay() == this.delayTicks) {
				Elemancy.info("note="+note.getPitchIndex());
				this.noteIndex++;
				this.done = this.noteIndex == this.notes.size();
				return note;
			}
		}
		return null;
	}

	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList notesNbt = new NbtList();
		for (Note note : this.notes) {
			notesNbt.add(note.writeNbt(new NbtCompound()));
		}
		nbt.put("Notes", notesNbt);
		return nbt;
	}

	public static Song fromNbt(NbtCompound nbt) {
		return new Song(nbt);
	}
}
