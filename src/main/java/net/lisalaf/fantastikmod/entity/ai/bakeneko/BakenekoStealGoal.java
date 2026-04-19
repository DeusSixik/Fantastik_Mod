package net.lisalaf.fantastikmod.entity.ai.bakeneko;

import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.lisalaf.fantastikmod.entity.phrases.BakenekoPhrases;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BakenekoStealGoal extends MoveToBlockGoal {
    private final BakenekoEntity bakeneko;
    private boolean hasStolen;

    public BakenekoStealGoal(BakenekoEntity bakeneko, double speedModifier) {

        /*
            Ищем сундук в радиусе 10 блоков.
         */
        super(bakeneko, speedModifier, 10, 2);
        this.bakeneko = bakeneko;
    }

    @Override
    public boolean canUse() {
        /*
             Запускаем цель, только если прошел кулдаун, лапы свободны и кот не в ярости.
         */
        if (this.bakeneko.getStealCooldown() > 0 || this.bakeneko.isHoldingItem() || this.bakeneko.isAngry()) {
            return false;
        }

        /*
            Вероятность того, что кот вообще захочет воровать в этот момент
         */
        if (this.bakeneko.getRandom().nextFloat() > 0.02f) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.hasStolen = false;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        /*
            Проверяем только то, что это ванильный сундук.

            TODO: Мб добавить поддержку модовых хранилищь
         */
        return state.getBlock() instanceof ChestBlock;
    }

    @Override
    public void tick() {
        super.tick();

        /*
             Если кот подошел вплотную к сундуку и еще ничего не украл
         */
        if (this.isReachedTarget() && !this.hasStolen) {
            tryStealFromChest();
            this.hasStolen = true;

            this.bakeneko.setStealCooldown(6000 + this.bakeneko.getRandom().nextInt(6000));
        }
    }

    private void tryStealFromChest() {
        Level level = this.bakeneko.level();
        if (level.isClientSide()) return;

        BlockEntity blockEntity = level.getBlockEntity(this.blockPos);
        if (!(blockEntity instanceof Container container)) return;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);

            if (!stack.isEmpty() && (this.bakeneko.isTreasure(stack) || this.bakeneko.isFood(stack))) {
                ItemStack stolen = stack.copy();
                stolen.setCount(1);

                if (this.bakeneko.tryPickupItem(stolen)) {
                    stack.shrink(1);

                    /*
                        КРИТИЧЕСКИ ВАЖНО: помечаем инвентарь сундука как измененный,
                        иначе игра не сохранит изменения на диск
                     */
                    container.setChanged();

                    this.bakeneko.playSound(SoundEvents.CAT_HISS, 0.8F, 0.7F);

                    /*
                        Ищем игрока-зеваку, чтобы сагриться на него
                     */
                    Player nearestPlayer = level.getNearestPlayer(this.bakeneko, 10.0D);
                    if (nearestPlayer != null && !nearestPlayer.isSpectator() && !nearestPlayer.isCreative()) {
                        nearestPlayer.sendSystemMessage(Component.literal(BakenekoPhrases.getStealFromPlayerMessage(nearestPlayer)));
                        this.bakeneko.setLastThief(nearestPlayer);
                        this.bakeneko.setAngry(true);
                    }
                    return;
                }
            }
        }
    }
}
