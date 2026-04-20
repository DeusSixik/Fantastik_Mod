
package net.lisalaf.fantastikmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.lisalaf.fantastikmod.entity.custom.IceDragonEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@SuppressWarnings("removal")
public class IceDragonRenderer extends GeoEntityRenderer<IceDragonEntity> {

    private Boolean hasOptifineCache = null;

    private boolean hasOptifine() {
        if (hasOptifineCache == null) {
            try {
                // Простая проверка
                Class.forName("optifine.Installer");
                hasOptifineCache = true;
            } catch (ClassNotFoundException e) {
                hasOptifineCache = false;
            }
        }
        return hasOptifineCache;
    }

    public IceDragonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new IceDragonModel());
        this.shadowRadius = 8.0f;
    }
    // ОТКЛЮЧАЕМ стандартный culling для огромных драконов
    @Override
    public boolean shouldRender(IceDragonEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        // Всегда рендерить если дракон в пределах 1024 блоков
        double distanceSqr = livingEntity.distanceToSqr(camX, camY, camZ);
        return distanceSqr < 1024.0 * 1024.0;
    }


    @Override
    public void render(IceDragonEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();

        float scale;
        int growthStage = entity.getGrowthStage();

        switch(growthStage) {
            case IceDragonEntity.STAGE_BABY:
                scale = 0.2f;
                break;
            case IceDragonEntity.STAGE_TEEN:
                scale = 1.7f;
                break;
            case IceDragonEntity.STAGE_YOUNG:
                scale = 2.3f;
                break;
            case IceDragonEntity.STAGE_ADULT:
            default:
                scale = entity.isFemale() ? 4.0f : 3.5f;
                break;
        }

        poseStack.scale(scale, scale, scale);

        // Анимация наклона в полете
        if (entity.isFlying()) {
            Vec3 motion = entity.getDeltaMovement();
            if (motion.horizontalDistance() > 0.1) {
                float roll = (float) Math.atan2(motion.y, motion.horizontalDistance()) * (180F / (float) Math.PI);
                poseStack.mulPose(Axis.ZP.rotationDegrees(roll * 0.1f));
            }
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();

        if (!hasOptifine()){
            renderEyes(entity, poseStack, bufferSource, partialTick, packedLight);
        }


    }

    private void renderEyes(IceDragonEntity entity, PoseStack poseStack,
                            MultiBufferSource bufferSource, float partialTick, int packedLight) {
        IceDragonModel model = (IceDragonModel) getGeoModel();
        ResourceLocation eyeTexture = model.getEyeTexture(entity);

        if (eyeTexture == null) return;

        poseStack.pushPose();

        // ПРИМЕНЯЕМ ТОЛЬКО ОСНОВНОЙ МАСШТАБ (без дублирования трансформаций)
        float scale;
        int growthStage = entity.getGrowthStage();

        switch(growthStage) {
            case IceDragonEntity.STAGE_BABY:
                scale = 0.2f;
                break;
            case IceDragonEntity.STAGE_TEEN:
                scale = 1.7f;
                break;
            case IceDragonEntity.STAGE_YOUNG:
                scale = 2.3f;
                break;
            case IceDragonEntity.STAGE_ADULT:
            default:
                scale = entity.isFemale() ? 4.0f : 3.5f;
                break;
        }
        poseStack.scale(scale, scale, scale);

        RenderType eyeRenderType = RenderType.eyes(eyeTexture);
        VertexConsumer eyeBuffer = bufferSource.getBuffer(eyeRenderType);

        BakedGeoModel bakedModel = model.getBakedModel(model.getModelResource(entity));

        super.reRender(bakedModel, poseStack, bufferSource, entity, eyeRenderType,
                eyeBuffer, partialTick, 15728880, getPackedOverlay(entity, 0),
                1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    // Альтернативный подход - рендерим глаза в основном методе
    @Override
    protected void applyRotations(IceDragonEntity entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(entity, poseStack, ageInTicks, rotationYaw, partialTick);

        // Масштабирование в applyRotations
        float scale;
        if (entity.isBaby()) {
            scale = 1.5f;
        } else {
            scale = entity.isFemale() ? 4.0f : 3.5f;
        }
        poseStack.scale(scale, scale, scale);
    }


    public float getShadowRadius(IceDragonEntity entity) {
        if (entity.isBaby()) {
            return 2.0f;
        } else {
            return entity.isFemale() ? 5.0f : 4.5f;
        }
    }



    private void renderHitbox(PoseStack poseStack, VertexConsumer buffer, PartEntity<?> part, float partialTick) {
        AABB aabb = part.getBoundingBox().move(-part.getX(), -part.getY(), -part.getZ());
        LevelRenderer.renderLineBox(poseStack, buffer, aabb, 0.0F, 1.0F, 0.0F, 1.0F);
    }

    private void renderHitbox(PoseStack poseStack, VertexConsumer buffer, IceDragonEntity entity, float partialTick) {
        AABB aabb = entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ());
        LevelRenderer.renderLineBox(poseStack, buffer, aabb, 1.0F, 0.0F, 0.0F, 1.0F);
    }
}