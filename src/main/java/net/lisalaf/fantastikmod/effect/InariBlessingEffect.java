package net.lisalaf.fantastikmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;

public class InariBlessingEffect extends MobEffect {
    private static final int RADIUS_LEVEL_1 = 6;
    private static final int RADIUS_LEVEL_2 = 16;
    private static final double SPEED_LEVEL_1 = 2.0;
    private static final double SPEED_LEVEL_2 = 2.5;

    public InariBlessingEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD700);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return;

        Level level = entity.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        int radius = (amplifier == 0) ? RADIUS_LEVEL_1 : RADIUS_LEVEL_2;
        double speedMultiplier = (amplifier == 0) ? SPEED_LEVEL_1 : SPEED_LEVEL_2;

        BlockPos center = entity.blockPosition();

        if (level.getGameTime() % 20 == 0) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    for (int y = -3; y <= 3; y++) {
                        BlockPos cropPos = center.offset(x, y, z);
                        BlockState state = level.getBlockState(cropPos);
                        Block block = state.getBlock();

                        if (block instanceof CropBlock crop) {
                            int currentAge = crop.getAge(state);
                            int maxAge = crop.getMaxAge();
                            if (currentAge < maxAge) {
                                float growthChance = (float) (1.0 / (40.0 / speedMultiplier));

                                if (level.random.nextFloat() < growthChance) {
                                    BlockState newState = crop.getStateForAge(currentAge + 1);
                                    level.setBlock(cropPos, newState, 3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}