package dev.fulmineo.elemancy.data;

import net.minecraft.item.ItemStack;

public interface ElemancyPlayerEntity {
	public void startPlayingSong(ItemStack bellStack, int songIndex);
	public ElemancySongManager getSongManager();
}
