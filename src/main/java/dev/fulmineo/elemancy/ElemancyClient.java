package dev.fulmineo.elemancy;

import dev.fulmineo.elemancy.client.render.OutlineRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.api.EnvType;

@Environment(EnvType.CLIENT)
public class ElemancyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
		WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(OutlineRenderer::onBlockOutline);
    }
}