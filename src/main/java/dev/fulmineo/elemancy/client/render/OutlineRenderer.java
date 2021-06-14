package dev.fulmineo.elemancy.client.render;

import java.util.List;

import dev.fulmineo.elemancy.data.ElemancyPlayerEntity;
import dev.fulmineo.elemancy.data.ElemancySongManager;
import dev.fulmineo.elemancy.mixin.WorldRendererAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

// Many thanks to https://github.com/rwilliaise/schmucks for this part
// https://github.com/rwilliaise/schmucks/blob/879c0e56df1c1ac4729432dffd5b6dda9b54eb22/src/main/java/com/alotofletters/schmucks/client/render/ControlWandWhitelistRenderer.java#L29

public class OutlineRenderer {
	public static boolean onBlockOutline(WorldRenderContext worldRenderContext, HitResult result) {
		MinecraftClient instance = MinecraftClient.getInstance();
        ClientPlayerEntity player = instance.player;

		ElemancySongManager songManager = ((ElemancyPlayerEntity)player).getSongManager();
		if (songManager.isActive()){
			ClientWorld world = worldRenderContext.world();

			WorldRendererAccessor accessor = (WorldRendererAccessor) worldRenderContext.worldRenderer();
			BufferBuilderStorage storage = accessor.getBufferBuilders();
			VertexConsumerProvider.Immediate immediate = storage.getEntityVertexConsumers();
			VertexConsumer consumer = immediate.getBuffer(RenderLayer.getLines());
			Vec3d cameraPos = worldRenderContext.camera().getPos();
			float shade = (float) ((Math.sin(Math.toRadians((world.getTime() + worldRenderContext.tickDelta()) * 15)) + 1) * 0.5);

			VoxelShape shape = VoxelShapes.fullCube();

			List<BlockPos> positions = songManager.getPositions();

			for (BlockPos pos : positions){
				double x = pos.getX() - cameraPos.getX();
				double y = pos.getY() - cameraPos.getY();
				double z = pos.getZ() - cameraPos.getZ();
				worldRenderContext.matrixStack().push();
				worldRenderContext.matrixStack().translate(x, y, z);
				shape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ) ->
					WorldRenderer.drawBox(
						worldRenderContext.matrixStack(),
						consumer,
						minX,
						minY,
						minZ,
						maxX,
						maxY,
						maxZ,
						1,
						1,
						1,
						shade
					)
				);
				worldRenderContext.matrixStack().pop();
			}
		}
		return true;
	}

	public static void outlineBlock(WorldRenderContext worldRenderContext, ClientWorld world, BlockPos pos, ClientPlayerEntity player) {

	}
}
