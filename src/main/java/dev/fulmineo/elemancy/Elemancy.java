package dev.fulmineo.elemancy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.fulmineo.elemancy.item.BoneBell;

public class Elemancy implements ModInitializer {

	public static Logger LOGGER = LogManager.getLogger();

    // Identifiers

    public static final String MOD_ID = "elemancy";

	public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID,"group"), () -> new ItemStack(Registry.ITEM.get(new Identifier("minecraft","bell"))));

    // Entities

    // Items

	public static final Item BONE_BELL = new BoneBell(new FabricItemSettings().maxCount(1).group(GROUP));

    @Override
    public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "bone_bell"), BONE_BELL);
    }

	public static void info(String message){
        LOGGER.log(Level.INFO, message);
    }
}