package net.lisalaf.fantastikmod.entity.ai.bakeneko;

import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class BakenekoInventoryGoal extends Goal {
    private final BakenekoEntity bakeneko;

    public BakenekoInventoryGoal(BakenekoEntity bakeneko) {
        this.bakeneko = bakeneko;
    }

    @Override
    public boolean canUse() {
        /*
            Проверяем инвентарь только раз в 100 тиков (5 секунд)
         */
        if (this.bakeneko.tickCount % 100 != 0) return false;

        /*
            Не мешаем коту, если он спит, сидит или воюет
         */
        if (this.bakeneko.isSleeping() || this.bakeneko.isSitting() || this.bakeneko.isAngry()) return false;

        return true;
    }

    @Override
    public void start() {
        /*
            Логика поедания еды из лап
         */
        ItemStack held = this.bakeneko.getHeldItem();
        if (!held.isEmpty() && this.bakeneko.isFood(held)) {
            if (this.bakeneko.getHealth() < this.bakeneko.getMaxHealth()) {
                this.bakeneko.eatHeldFood();
            } else if (this.bakeneko.getRandom().nextFloat() < 0.4f) {
                this.bakeneko.dropHeldItem();
            }
            return;
        }

        /*
            Логика выбрасывания мусора
         */
        if (this.bakeneko.getDropCooldown() <= 0 && this.bakeneko.isInventoryFull()) {
            if (this.bakeneko.getRandom().nextFloat() < 0.2f) { // 20% шанс выкинуть мусор раз в 5 сек
                this.bakeneko.dropLeastValuableItem();
                this.bakeneko.setDropCooldown(200);
            }
        }
    }
}
