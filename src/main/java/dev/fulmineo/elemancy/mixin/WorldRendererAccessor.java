package dev.fulmineo.elemancy.mixin;

import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Many thanks to https://github.com/rwilliaise/schmucks for this part

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
    @Accessor
    BufferBuilderStorage getBufferBuilders();
}
