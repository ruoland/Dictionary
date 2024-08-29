package org.ruoland.dictionary.dictionary.gui.dev;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;
import org.ruoland.dictionary.dictionary.gui.DebugScreen;

public class EntityScreen extends DebugScreen {
    private Entity entity;
    public EntityScreen(Component pTitle, Entity entity) {
        super(pTitle);
        this.entity = entity;
    }
    private float xRot = 0;
    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        // 엔티티를 렌더링할 위치 설정
        int x = width / 2;
        int y = height / 2;

        // PoseStack 얻기
        PoseStack poseStack = pGuiGraphics.pose();

        // 엔티티 렌더링 준비
        poseStack.pushPose();
        poseStack.translate(x, y, 50);
        poseStack.scale(40, 40, 40);
        poseStack.mulPose(new Quaternionf().rotationZ((float) Math.PI));
        xRot+=0.01F;
        poseStack.mulPose(new Quaternionf().rotationXYZ(0, xRot, 0));
        // EntityRenderDispatcher를 사용하여 엔티티 렌더링
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, pPartialTick, poseStack, pGuiGraphics.bufferSource(), 15728880);
        entityRenderDispatcher.setRenderShadow(true);

        poseStack.popPose();
    }
}
