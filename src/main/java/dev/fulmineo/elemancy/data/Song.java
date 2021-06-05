package dev.fulmineo.elemancy.data;

import java.util.List;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class Song {
	public List<Note> notes;
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
		this.noteIndex = 0;
		this.previous = null;
	}

	public Note getNote() {
		if (this.noteIndex == this.notes.size()) return null;
		return this.notes.get(this.noteIndex);
	}

	public void nextNote(){
		this.noteIndex++;
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
