package net.lisalaf.fantastikmod.entity.client;

import net.lisalaf.fantastikmod.entity.custom.IceDragonEntity;
import net.lisalaf.fantastikmod.entity.custom.KitsuneLightEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@SuppressWarnings("removal")
public class KitsuneLightModel extends GeoModel<KitsuneLightEntity> {
    private boolean renderingEyes = false;

    @Override
    public ResourceLocation getModelResource(KitsuneLightEntity entity) {
        return new ResourceLocation("fantastikmod", "geo/kitsune_light.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KitsuneLightEntity entity) {
        int variant = entity.getVariant();
        return new ResourceLocation("fantastikmod",
                "textures/entity/kitsune/kitsune_light_" + variant + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(KitsuneLightEntity entity) {
        return new ResourceLocation("fantastikmod", "animations/kitsune_light.animation.json");
    }

    public ResourceLocation getEyeTexture(KitsuneLightEntity entity) {
        int eyeVariant = entity.getEyeVariant() % 7;
        return new ResourceLocation("fantastikmod",
                "textures/entity/kitsune/eyes/kitsune_eyes_" + eyeVariant + ".png");
    }
    public void setRenderingEyes(boolean renderingEyes) {
        this.renderingEyes = renderingEyes;
    }
}