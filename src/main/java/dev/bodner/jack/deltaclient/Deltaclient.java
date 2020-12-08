package dev.bodner.jack.deltaclient;

import dev.bodner.jack.deltaclient.client.DeltaclientClient;
import grondag.frex.api.event.WorldRenderEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Random;

public class Deltaclient implements ModInitializer {
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Override
    public void onInitialize() {

    }
//    public void testRender(){
//        Vec3i vec = new Vec3i(0,100,0);
//        BlockPos pos = new BlockPos(vec);
//        BlockPos blockPos = BlockPos.Mutable.ORIGIN.toImmutable();
//        ChunkRendererRegion chunkRendererRegion = ChunkRendererRegion.create(client.world, blockPos.add(-1, -1, -1), blockPos.add(16, 16, 16), 1);
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bufferBuilder = tessellator.getBuffer();
//        client.getBlockRenderManager().renderBlock(Blocks.GRASS_BLOCK.getDefaultState(),pos, chunkRendererRegion ,new MatrixStack(), bufferBuilder,false, new Random());
//    }


}
