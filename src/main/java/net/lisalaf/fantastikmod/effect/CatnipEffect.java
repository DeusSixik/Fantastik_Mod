package net.lisalaf.fantastikmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CatnipEffect extends MobEffect {
    public CatnipEffect() {
        super(MobEffectCategory.NEUTRAL, 0x88FF88);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                "91AEAA56-376B-4498-935B-2F7F68070635",
                0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().random.nextInt(20) == 0) {
            double dx = (entity.level().random.nextDouble() - 0.5) * 1.5;
            double dz = (entity.level().random.nextDouble() - 0.5) * 1.5;
            entity.setDeltaMovement(dx, entity.getDeltaMovement().y, dz);

            float yaw = entity.level().random.nextFloat() * 360;
            entity.setYRot(yaw);
        }
        super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}