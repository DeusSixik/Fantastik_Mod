package net.lisalaf.fantastikmod.entity.ai.bakeneko;

import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.EnumSet;
import java.util.List;

public class BakenekoPickupItemGoal extends Goal {
    private final BakenekoEntity bakeneko;
    private final double speedModifier;
    private ItemEntity targetItem;

    public BakenekoPickupItemGoal(BakenekoEntity bakeneko, double speedModifier) {
        this.bakeneko = bakeneko;
        this.speedModifier = speedModifier;

        /*
            Флаг MOVE означает, что пока кот идет за предметом,
            другие цели не смогут перехватить управление его ногами
         */
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        /*
            Проверяем раз в секунду (20 тиков), чтобы не грузить сервер.
         */
        if (this.bakeneko.tickCount % 20 != 0) return false;

        /*
            Если лапы заняты, спит, сидит или злится - игнорируем лут
         */
        if (this.bakeneko.isHoldingItem() || this.bakeneko.isSitting() ||
                this.bakeneko.isSleeping() || this.bakeneko.isAngry()) {
            return false;
        }

        /*
            Получаем только те предметы, которые кот В ПРИНЦИПЕ готов подобрать.
         */
        List<ItemEntity> items = this.bakeneko.level().getEntitiesOfClass(
                ItemEntity.class,
                this.bakeneko.getBoundingBox().inflate(8.0D),
                item -> !item.isRemoved() && item.getItem().getCount() > 0 &&
                        this.bakeneko.wantsToPickUp(item.getItem())
        );

        if (items.isEmpty()) {
            return false;
        }

        /*
             Сортируем только полезные предметы (обычно это 1-2 сущности, сортировка пройдет мгновенно)
         */
        items.sort((a, b) -> Integer.compare(
                this.bakeneko.getItemPriority(b.getItem()),
                this.bakeneko.getItemPriority(a.getItem())
        ));

        this.targetItem = items.get(0);
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        /*
            Продолжаем идти за предметом, если он еще существует, и мы его не подобрали
         */
        return this.targetItem != null
                && this.targetItem.isAlive()
                && !this.bakeneko.isHoldingItem()
                && !this.bakeneko.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.bakeneko.getNavigation().moveTo(this.targetItem, this.speedModifier);
    }

    @Override
    public void tick() {
        if (this.targetItem == null || !this.targetItem.isAlive()) return;

        /*
            Кот обновляет путь до предмета (вдруг он скатился с горки)
         */
        this.bakeneko.getLookControl().setLookAt(this.targetItem, 30.0F, 30.0F);

        if (this.bakeneko.tickCount % 10 == 0) {
            this.bakeneko.getNavigation().moveTo(this.targetItem, this.speedModifier);
        }

        /*
             Если подошли вплотную (меньше 1.5 блоков) - подбираем
         */
        if (this.bakeneko.distanceToSqr(this.targetItem) < 2.25D) {
            if (this.bakeneko.tryPickupItem(this.targetItem.getItem())) {
                this.targetItem.discard();
                this.bakeneko.playSound(SoundEvents.ITEM_PICKUP, 0.5F, 1.0F);
                this.targetItem = null; // Заканчиваем Goal
            }
        }
    }

    @Override
    public void stop() {
        this.targetItem = null;
        this.bakeneko.getNavigation().stop();
    }
}