package net.lisalaf.fantastikmod.entity.client;

import net.lisalaf.fantastikmod.entity.custom.IceDragonEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

@SuppressWarnings("removal")
public class IceDragonModel extends GeoModel<IceDragonEntity> {

    @Override
    public ResourceLocation getModelResource(IceDragonEntity entity) {
        if (entity.getGrowthStage() == IceDragonEntity.STAGE_BABY) {
            return new ResourceLocation("fantastikmod", "geo/ice_dragon_baby.geo.json");
        }

        String gender = entity.isFemale() ? "female" : "male";
        return new ResourceLocation("fantastikmod", "geo/ice_dragon_" + gender + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(IceDragonEntity entity) {
        int variant = entity.getVariant();
        String texturePath = "textures/entity/dragon/ice_dragon_" + variant + ".png";

        return new ResourceLocation("fantastikmod", texturePath);
    }

    @Override
    public ResourceLocation getAnimationResource(IceDragonEntity entity) {
        return new ResourceLocation("fantastikmod", "animations/ice_dragon.animation.json");
    }



    public ResourceLocation getEyeTexture(IceDragonEntity entity) {
        int eyeVariant = entity.getEyeVariant() % 5;
        return new ResourceLocation("fantastikmod",
                "textures/entity/dragon/eyes/ice_dragon_eyes_" + eyeVariant + ".png");
    }
}