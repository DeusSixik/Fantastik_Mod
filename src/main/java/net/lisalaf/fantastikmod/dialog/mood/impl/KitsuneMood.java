package net.lisalaf.fantastikmod.dialog.mood.impl;

import net.lisalaf.fantastikmod.dialog.mood.MoodSystem;
import net.lisalaf.fantastikmod.entity.custom.KitsuneLightEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class KitsuneMood extends MoodSystem {
    private long lastSitTime = 0;
    private int wanderingTime = 0;
    private long lastGiftTime = 0;
    private long lastUpdateTime = 0;
    private boolean wasSitting = false;

    @Override
    public void updateMood(Level level, LivingEntity entity) {
        if (!(entity instanceof KitsuneLightEntity kitsune)) return;

        long currentTime = level.getGameTime();
        
        if (currentTime - lastUpdateTime < 200) return;
        lastUpdateTime = currentTime;

        if (kitsune.isSitting()) {
            if (!wasSitting) {
                lastSitTime = currentTime;
                wasSitting = true;
            }

            if (currentTime - lastSitTime > 600) {
                addMood(-1);
                lastSitTime = currentTime;
            }
        } else {
            wasSitting = false;
            lastSitTime = 0;
        }

        boolean isMoving = kitsune.getDeltaMovement().horizontalDistanceSqr() > 0.001D;
        if (isMoving && !kitsune.isSitting() && !kitsune.isSleeping()) {
            wanderingTime++;
            if (wanderingTime >= 600) {
                addMood(2);
                wanderingTime = 0;
            }
        } else {
            wanderingTime = Math.max(0, wanderingTime - 1);
        }

        if (currentTime % 1200 == 0) {
            if (moodValue < 0) {
                addMood(1);
            }
        }
    }

    @Override
    public void addMood(int amount) {
        int oldValue = moodValue;
        super.addMood(amount);
        if (Math.abs(amount) > 0) {
        }
    }

    @Override
    public void setMood(int value) {
        int oldValue = moodValue;
        super.setMood(value);
    }
}