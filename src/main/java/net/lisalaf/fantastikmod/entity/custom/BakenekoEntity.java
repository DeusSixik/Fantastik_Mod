package net.lisalaf.fantastikmod.entity.custom;

import lombok.Getter;
import lombok.Setter;
import net.lisalaf.fantastikmod.dialog.DialogScreen;
import net.lisalaf.fantastikmod.dialog.mobs.BakenekoDialog;
import net.lisalaf.fantastikmod.effect.ModEffects;
import net.lisalaf.fantastikmod.entity.ModEntities;
import net.lisalaf.fantastikmod.entity.ai.bakeneko.*;
import net.lisalaf.fantastikmod.entity.phrases.BakenekoPhrases;
import net.lisalaf.fantastikmod.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

@Setter
@Getter
public class BakenekoEntity extends Animal implements GeoEntity {

    // ==================== АНИМАЦИИ И ДАННЫЕ ====================
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Boolean> DATA_STANDING = SynchedEntityData.defineId(BakenekoEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_ANIMATION_STATE = SynchedEntityData.defineId(BakenekoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_HAS_ITEM = SynchedEntityData.defineId(BakenekoEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_ANGRY = SynchedEntityData.defineId(BakenekoEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(BakenekoEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_MALE = SynchedEntityData.defineId(BakenekoEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_BABY = SynchedEntityData.defineId(BakenekoEntity.class, EntityDataSerializers.BOOLEAN);

    // Состояния анимации
    public static final int ANIMATION_IDLE = 0;
    public static final int ANIMATION_WALK = 1;
    public static final int ANIMATION_STAND_IDLE = 2;
    public static final int ANIMATION_STAND_WALK = 3;
    public static final int ANIMATION_ATTACK = 4;
    public static final int ANIMATION_SIT_IDLE = 5;
    public static final int ANIMATION_SIT_WASH = 6;
    public static final int ANIMATION_SLEEP = 7;
    public static final int ANIMATION_SLEEP2 = 8;
    public static final int ANIMATION_SLEEP3 = 9;

    // Размеры (мальчики на 10% больше)
    private static final float BASE_WIDTH = 0.6f;
    private static final float BASE_HEIGHT = 0.6f;
    private static final float MALE_SCALE = 1.9f;
    private static final float STANDING_WIDTH = 0.8f;
    private static final float STANDING_HEIGHT = 1.2f;
    private static final float BABY_SCALE = 0.5f;

    // Таймеры и состояния
    private int standCooldown = 0;
    private int animationChangeCooldown = 0;

    @Getter
    @Setter
    private int stealCooldown = 0;

    @Setter
    @Getter
    private int growthTime = 0;
    private int dropCooldown = 0;
    private int angerTime = 0;
    private ItemStack heldItem = ItemStack.EMPTY;
    private final net.minecraft.world.SimpleContainer inventory = new net.minecraft.world.SimpleContainer(9);
    private Player lastThief = null;
    private int sitDuration = 0;
    private int sleepDuration = 0;
    private BlockPos sittingPos = null;
    private BlockPos sleepSpot = null;

    @Getter
    private boolean isSitting = false;

    @Getter
    private boolean isSleeping = false;
    private int loveCooldown = 0;

    // Время роста детёныша (20 минут = 24000 тиков)
    private static final int GROWTH_TIME = 24000;

    // Шанс размножения 10%
    private static final float BREEDING_CHANCE = 0.1f;

    // Варианты окраса
    private static final double[] VARIANT_SPAWN_CHANCES = {
            0.30,  // variant 0 - 30%
            0.24,  // variant 1 - 24%
            0.10,  // variant 2 - 10%
            0.20,  // variant 3 - 20%
            0.01,  // variant 4 - 1%
            0.15   // variant 5 - 15%
    };

    public BakenekoEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.refreshDimensions();
    }

    // ==================== СИНХРОНИЗАЦИЯ ДАННЫХ ====================
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_STANDING, false);
        this.entityData.define(DATA_ANIMATION_STATE, ANIMATION_IDLE);
        this.entityData.define(DATA_HAS_ITEM, false);
        this.entityData.define(DATA_IS_ANGRY, false);
        this.entityData.define(DATA_VARIANT, 0);
        this.entityData.define(DATA_IS_MALE, false);
        this.entityData.define(DATA_IS_BABY, false);
    }

    public boolean isMale() {
        return entityData.get(DATA_IS_MALE);
    }

    public void setMale(boolean male) {
        entityData.set(DATA_IS_MALE, male);
    }

    public boolean isBabyEntity() {
        return entityData.get(DATA_IS_BABY);
    }

    public void setBabyEntity(boolean baby) {
        entityData.set(DATA_IS_BABY, baby);
        refreshDimensions();
    }

    // ==================== ХИТБОКСЫ И РАЗМЕРЫ ====================
    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
        if (!this.level().isClientSide) {
            updateHitbox();
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        /*
            Включаем анимацию атаки
         */
        this.setAnimationState(ANIMATION_ATTACK);

        /*
            Шипим
         */
        this.playSound(SoundEvents.CAT_HISS, 1.0F, 0.5F);

        /*
            Вызываем ванильный расчет урона (взятый из атрибутов)
         */
        return super.doHurtTarget(target);
    }

    private void updateHitbox() {
        float width = getCurrentWidth();
        float height = getCurrentHeight();

        this.setBoundingBox(new AABB(
                this.getX() - width / 2,
                this.getY(),
                this.getZ() - width / 2,
                this.getX() + width / 2,
                this.getY() + height,
                this.getZ() + width / 2
        ));
    }

    private float getCurrentWidth() {
        float width = BASE_WIDTH;

        // Детёныши в 2 раза меньше
        if (isBabyEntity()) {
            width *= BABY_SCALE;
        }
        // Мальчики на 10% больше
        else if (isMale()) {
            width *= MALE_SCALE;
        }

        // В режиме стояния ширина увеличивается
        if (isStanding()) {
            width = STANDING_WIDTH;
            if (isMale() && !isBabyEntity()) {
                width *= MALE_SCALE;
            }
        }

        return width;
    }

    private float getCurrentHeight() {
        float height = BASE_HEIGHT;

        if (isBabyEntity()) {
            height *= BABY_SCALE;
        } else if (isMale()) {
            height *= MALE_SCALE;
        }

        if (isStanding()) {
            height = STANDING_HEIGHT;
            if (isMale() && !isBabyEntity()) {
                height *= MALE_SCALE;
            }
        }

        return height;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return EntityDimensions.fixed(getCurrentWidth(), getCurrentHeight());
    }

    private void forceHitboxUpdate() {
        Vec3 oldPos = this.position();
        this.reapplyPosition();
        this.setPos(oldPos);
        this.setBoundingBox(this.getDimensions(this.getPose()).makeBoundingBox(this.position()));
    }

    // ==================== РОСТ ДЕТЁНЫША ====================
    private void updateGrowth() {
        if (!this.level().isClientSide && isBabyEntity()) {
            int growth = getGrowthTime() + 1;
            setGrowthTime(growth);

            if (growth >= GROWTH_TIME) {
                setBabyEntity(false);
                setGrowthTime(0);
                refreshDimensions();
            }
        }
    }

    // ==================== РАЗМНОЖЕНИЕ ====================
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.SALMON) || stack.is(Items.TROPICAL_FISH) ||
                stack.is(Items.PUFFERFISH) || stack.is(Items.COOKED_COD) || stack.is(Items.COOKED_SALMON) ||
                stack.is(Items.BEEF) || stack.is(Items.COOKED_BEEF) || stack.is(Items.PORKCHOP) ||
                stack.is(Items.COOKED_PORKCHOP) || stack.is(Items.CHICKEN) || stack.is(Items.COOKED_CHICKEN) ||
                stack.is(Items.MUTTON) || stack.is(Items.COOKED_MUTTON) || stack.is(Items.RABBIT) ||
                stack.is(Items.COOKED_RABBIT) || stack.is(Items.MILK_BUCKET) || stack.is(Items.SPIDER_EYE) ||
                stack.is(Items.FERMENTED_SPIDER_EYE);
    }

    @Override
    public boolean canBreed() {
        return loveCooldown <= 0 && !isBabyEntity();
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        BakenekoEntity baby = ModEntities.BAKENEKO.get().create(level);
        if (baby != null) {
            baby.setMale(level.random.nextBoolean());
            baby.setBabyEntity(true);
            baby.setGrowthTime(0);
            baby.setVariant(level.random.nextInt(6));
        }
        return baby;
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
        setBabyEntity(age < 0);
    }

    private InteractionResult handleBreeding(Player player, ItemStack itemstack) {
        if (!this.canBreed()) {
            return InteractionResult.PASS;
        }

        BakenekoEntity partner = findMate();

        if (partner != null && partner.canBreed()) {
            if (this.random.nextFloat() < BREEDING_CHANCE) {
                AgeableMob baby = this.getBreedOffspring((ServerLevel) this.level(), partner);
                if (baby != null) {
                    baby.setBaby(true);
                    baby.setPos(this.getX(), this.getY(), this.getZ());
                    this.level().addFreshEntity(baby);

                    this.loveCooldown = 6000;
                    partner.loveCooldown = 6000;

                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    this.level().broadcastEntityEvent(this, (byte) 18);
                    partner.level().broadcastEntityEvent(partner, (byte) 18);

                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    private BakenekoEntity findMate() {
        List<BakenekoEntity> nearby = this.level().getEntitiesOfClass(BakenekoEntity.class,
                this.getBoundingBox().inflate(8.0D));

        for (BakenekoEntity other : nearby) {
            if (other != this && !other.isBabyEntity() && other.isMale() != this.isMale()) {
                return other;
            }
        }
        return null;
    }

    public void wakeUp() {
        if (isSleeping || isSitting) {
            setSitting(false, null);
            setSleeping(false, null);
            stopSittingSleepingAnimation();
        }
    }

    public void forceWakeUp() {
        if (isSitting || isSleeping) {
            setSitting(false, null);
            setSleeping(false, null);
            stopSittingSleepingAnimation();
            this.getNavigation().stop();
        }
    }


    // ==================== ОСНОВНОЙ ТИК ====================
    @Override
    protected void customServerAiStep() {
        /*
            В Minecraft лучше использовать customServerAiStep() для логики ИИ вместо tick(),
            так как он вызывается только на сервере и только когда моб активен.
         */
        super.customServerAiStep();

        updateGrowth();
        updateTimers();

        if (standCooldown <= 0 && this.random.nextFloat() < 0.001f && !isAngry() && !isBabyEntity()) {
            toggleStandingMode();
            standCooldown = 200 + random.nextInt(800);
        }

        if (this.level().isRaining() && this.level().canSeeSky(this.blockPosition())) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        } else {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        }

        /*
            Затухание агрессии
         */
        if (lastThief != null && this.distanceToSqr(lastThief) > 100.0D && this.tickCount % 100 == 0) {
            angerTime -= 100;
            if (angerTime <= 0) {
                setAngry(false);
                lastThief = null;
            }
        }

        if (loveCooldown > 0) loveCooldown--;
    }

    @Override
    public void tick() {
        super.tick();

        if (animationChangeCooldown == 1) {
            refreshDimensions();
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        /*
            Как только клиент узнает, что кот встал/сел/вырос, он мгновенно обновит хитбокс
         */
        if (DATA_STANDING.equals(key) || DATA_IS_BABY.equals(key) || DATA_IS_MALE.equals(key)) {
            this.refreshDimensions();
        }
    }

    private void updateTimers() {
        if (standCooldown > 0) standCooldown--;
        if (animationChangeCooldown > 0) animationChangeCooldown--;
        if (stealCooldown > 0) stealCooldown--;
        if (dropCooldown > 0) dropCooldown--;
        if (angerTime > 0) angerTime--;

        if (angerTime <= 0 && isAngry()) setAngry(false);

        if (animationChangeCooldown <= 0 && getAnimationState() == ANIMATION_ATTACK) {
            setAnimationState(ANIMATION_IDLE);
        }

        if (isSitting && sitDuration > 0) {
            sitDuration--;
            if (sitDuration <= 0) {
                setSitting(false, null);
                stopSittingSleepingAnimation();
            }
        }

        if (isSleeping && sleepDuration > 0) {
            sleepDuration--;
            if (sleepDuration <= 0) {
                setSleeping(false, null);
                stopSittingSleepingAnimation();
            }
        }
    }

    public int getItemPriority(ItemStack stack) {
        if (isTreasure(stack)) return 90;
        if (isFood(stack)) return 80;
        if (stack.is(Items.STRING) || stack.is(Items.FEATHER) || stack.is(Items.BONE) || stack.is(Items.LEATHER))
            return 50;
        return 40;
    }

    public boolean wantsToPickUp(ItemStack stack) {
        /*
            Игнорируем гнилую плоть и ядовитую картошку
         */
        if (stack.is(Items.ROTTEN_FLESH) || stack.is(Items.POISONOUS_POTATO)) return false;

        /*
            Если инвентарь полон, берем только еду или сокровища
         */
        return !isInventoryFull() || isTreasure(stack) || isFood(stack);
    }

    private boolean isTreasure(ItemStack stack) {
        return stack.is(Items.EMERALD) || stack.is(Items.DIAMOND) || stack.is(Items.GOLD_INGOT) ||
                stack.is(Items.IRON_INGOT) || stack.is(Items.LAPIS_LAZULI) || stack.is(Items.REDSTONE) ||
                stack.is(Items.QUARTZ) || stack.is(Items.AMETHYST_SHARD);
    }

    public boolean tryPickupItem(ItemStack itemStack) {
        if (itemStack.is(Items.ROTTEN_FLESH) || itemStack.is(Items.POISONOUS_POTATO)) return false;
        if (isInventoryFull() && !isTreasure(itemStack) && !isFood(itemStack)) return false;

        if (this.heldItem.isEmpty()) {
            this.heldItem = itemStack.copy();
            this.heldItem.setCount(1);
            setHasItem(true);
            itemStack.shrink(1);
            return true;
        } else if (addToInventory(itemStack.copy())) {
            itemStack.shrink(1);
            return true;
        }
        return false;
    }

    private boolean addToInventory(ItemStack itemStack) {
        itemStack.setCount(1);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slot = inventory.getItem(i);
            if (slot.isEmpty()) {
                inventory.setItem(i, itemStack);
                return true;
            }
            if (ItemStack.isSameItemSameTags(slot, itemStack) && slot.getCount() < slot.getMaxStackSize()) {
                slot.grow(1);
                return true;
            }
        }
        return false;
    }

    public boolean isInventoryFull() {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).isEmpty()) return false;
        }
        return true;
    }

    public void dropLeastValuableItem() {
        int worstSlot = -1;
        int worstPriority = Integer.MAX_VALUE;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                int priority = getItemPriority(stack);
                if (priority < worstPriority) {
                    worstPriority = priority;
                    worstSlot = i;
                }
            }
        }

        if (worstSlot != -1) {
            ItemStack toDrop = inventory.getItem(worstSlot).copy();
            inventory.setItem(worstSlot, ItemStack.EMPTY);

            Vec3 lookVec = this.getLookAngle();
            double x = this.getX() + lookVec.x * 2.0;
            double z = this.getZ() + lookVec.z * 2.0;

            ItemEntity itemEntity = new ItemEntity(this.level(), x, this.getY() + 0.5, z, toDrop);
            itemEntity.setDeltaMovement(lookVec.x * 0.5, 0.3, lookVec.z * 0.5);
            this.level().addFreshEntity(itemEntity);
        } else if (!this.heldItem.isEmpty()) {
            dropHeldItem();
        }
    }

    public void dropHeldItem() {
        if (!this.heldItem.isEmpty()) {
            spawnAtLocation(this.heldItem.copy());
            this.heldItem = ItemStack.EMPTY;
            setHasItem(false);
        }
    }

    public void eatHeldFood() {
        if (!this.heldItem.isEmpty() && isFood(this.heldItem)) {
            this.heal(4.0F);
            this.heldItem.shrink(1);
            if (this.heldItem.isEmpty()) setHasItem(false);
            playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
        }
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource damageSource, int lootingMultiplier, boolean allowDrops) {
        super.dropCustomDeathLoot(damageSource, lootingMultiplier, allowDrops);
        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                this.spawnAtLocation(itemstack);
            }
        }
    }

    public void takeFoodFromPlayer(Player player) {
        if (!this.level().isClientSide) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (isFood(stack) && !stack.isEmpty()) {
                    stack.shrink(1);
                    this.heal(4.0F);
                    player.sendSystemMessage(Component.translatable("chat.bakeneko.feed"));
                    this.playSound(SoundEvents.GENERIC_EAT, 0.5F, 1.0F);
                    break;
                }
            }
        }
    }

    public void hitPlayer(Player player) {
        if (!this.level().isClientSide) {
            player.hurt(player.damageSources().mobAttack(this), 3.0F);
            player.sendSystemMessage(BakenekoPhrases.getHurtMessage());
            this.playSound(SoundEvents.CAT_HISS, 1.0F, 0.5F);
        }
    }

    // ==================== ВЗАИМОДЕЙСТВИЕ С ИГРОКОМ ====================
    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.isEmpty() && !player.isShiftKeyDown() && this.isHoldingItem()) {
            if (!this.level().isClientSide) {
                if (!player.getInventory().add(this.heldItem.copy())) {
                    player.drop(this.heldItem.copy(), false);
                }
                this.heldItem = ItemStack.EMPTY;
                setHasItem(false);
                playSound(SoundEvents.CAT_AMBIENT, 0.5F, 1.0F);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // Диалог на Shift + ПКМ
        if (player.isShiftKeyDown()) {
            if (this.level().isClientSide) {
                BakenekoDialog dialog = new BakenekoDialog(this, player);
                Minecraft.getInstance().setScreen(new DialogScreen(this, dialog));
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        ItemStack itemInHand = player.getItemInHand(hand);

        if (itemInHand.getItem() == ModItems.CATNIP.get() ||
                itemInHand.getItem() == ModItems.DRIED_CATNIP.get()) {
            wakeUp();


            if (!this.level().isClientSide) {
                this.addEffect(new MobEffectInstance(ModEffects.CATNIP_EFFECT.get(), 600, 0));

                if (!player.isCreative()) {
                    itemInHand.shrink(1);
                }

                this.level().playSound(null, this.blockPosition(), SoundEvents.CAT_EAT, SoundSource.NEUTRAL, 1.0F, 1.0F);

                for (int i = 0; i < 8; i++) {
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                            this.getX(), this.getY() + 0.5, this.getZ(),
                            0, 0.1, 0);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (isFood(itemstack) && !isBabyEntity()) {
            InteractionResult result = handleBreeding(player, itemstack);
            if (result != InteractionResult.PASS) return result;
        }

        if (isFood(itemstack)) {
            return feed(player, itemstack);
        }

        if (isAngry() && player == lastThief) {
            player.hurt(this.damageSources().mobAttack(this), 2.0F);
            return InteractionResult.FAIL;
        }

        if (!itemstack.isEmpty()) {
            return giveItemToBakeneko(player, itemstack);
        }
        if (itemstack.isEmpty() && this.isHoldingItem()) {
            return takeHeldItemFromBakeneko(player);
        }

        return super.mobInteract(player, hand);
    }

    private void spawnCatnipParticles() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 10; i++) {
                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                        this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0),
                        0, 0.1, 0);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        wakeUp();
        if (source.getEntity() instanceof Player player) {
            setAngry(true);
            lastThief = player;
            angerTime = 600;
            if (!isBabyEntity() && !level().isClientSide) {
                setTarget(player);
            }
        }
        return super.hurt(source, amount);
    }

    private InteractionResult takeHeldItemFromBakeneko(Player player) {
        if (!this.heldItem.isEmpty()) {
            if (!player.getInventory().add(this.heldItem.copy())) {
                player.drop(this.heldItem.copy(), false);
            }
            this.heldItem = ItemStack.EMPTY;
            setHasItem(false);
            playSound(SoundEvents.CAT_AMBIENT, 0.5F, 1.0F);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult giveItemToBakeneko(Player player, ItemStack itemstack) {
        if (tryPickupItem(itemstack.copy())) {
            if (!player.getAbilities().instabuild) itemstack.shrink(1);
            playCatSound();
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult feed(Player player, ItemStack itemstack) {
        if (this.getHealth() < this.getMaxHealth()) {
            this.heal(4.0F);
        }

        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        if (!this.level().isClientSide) {
            player.sendSystemMessage(BakenekoPhrases.getFeedMessage());
        }

        playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
        playCatSound();

        if (this.level().isClientSide) {
            for (int i = 0; i < 5; i++) {
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.HEART,
                        this.getRandomX(0.5D), this.getRandomY() + 0.5D, this.getRandomZ(0.5D),
                        0, 0.1D, 0);
            }
        }

        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    // ==================== ЗВУКИ ====================
    private void playCatSound() {
        SoundEvent[] sounds = {SoundEvents.CAT_AMBIENT, SoundEvents.CAT_PURR, SoundEvents.CAT_PURREOW, SoundEvents.CAT_HISS};
        playSound(sounds[random.nextInt(sounds.length)], 0.5F, 0.8F + random.nextFloat() * 0.4F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isAngry() ? SoundEvents.CAT_HISS : SoundEvents.CAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.CAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    // ==================== ИММУНИТЕТЫ ====================
    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.getEffect() != MobEffects.POISON && super.canBeAffected(effect);
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) { }

    // ==================== ЦЕЛИ AI ====================
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D) {
            @Override
            public boolean canUse() {
                return !BakenekoEntity.this.isAngry() && super.canUse();
            }
        });

        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.addGoal(3, new BakenekoEscapeWaterGoal(this));
        this.goalSelector.addGoal(4, new BakenekoFleeRainGoal(this));

        /*
            Логика пробуждения котика ради еды
         */
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.2D, Ingredient.of(ModItems.CATNIP.get(), ModItems.DRIED_CATNIP.get()), false) {
            @Override
            public void start() {
                super.start();
                BakenekoEntity.this.wakeUp();
            }
        });

        /*
            true означает, что кот будет закрывать за собой дверь (если нужно, чтобы оставлял открытой - ставь false)
         */
        this.goalSelector.addGoal(6, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(7, new BakenekoSleepGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new BakenekoSitGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new BakenekoStealGoal(this, 1.1D));
        this.goalSelector.addGoal(10, new BakenekoPickupItemGoal(this, 1.2D));
        this.targetSelector.addGoal(11, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                player -> player.getMainHandItem().getItem() == ModItems.CATNIP.get() ||
                        player.getOffhandItem().getItem() == ModItems.CATNIP.get() ||
                        player.getMainHandItem().getItem() == ModItems.DRIED_CATNIP.get() ||
                        player.getOffhandItem().getItem() == ModItems.DRIED_CATNIP.get()));
        this.goalSelector.addGoal(12, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(13, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(14, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(15, new BakenekoInventoryGoal(this));

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false,
                player -> isAngry() && player == lastThief));

        ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
    }

    // ==================== АНИМАЦИИ ====================
    public void startSittingAnimation() {
        setAnimationState(ANIMATION_SIT_IDLE);
    }

    public void startSleepingAnimation() {
        setAnimationState(ANIMATION_SLEEP + random.nextInt(3));
    }

    public void stopSittingSleepingAnimation() {
        setAnimationState(isStanding() ? ANIMATION_STAND_IDLE : ANIMATION_IDLE);
    }

    public void toggleStandingMode() {
        setStanding(!isStanding());
        refreshDimensions();
        playSound(SoundEvents.CAT_PURR, 0.5F, isStanding() ? 0.8F : 1.2F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "MainController", 0, this::handleAnimations));
    }

    private PlayState handleAnimations(AnimationState<BakenekoEntity> event) {
        /*
             Сначала проверяем "жесткие" серверные состояния (Атака, Сон, Сидение)
         */
        int state = getAnimationState();
        if (state == ANIMATION_ATTACK) return event.setAndContinue(RawAnimation.begin().thenPlay("attack"));
        if (state == ANIMATION_SIT_IDLE) return event.setAndContinue(RawAnimation.begin().thenLoop("sit_idle"));
        if (state == ANIMATION_SIT_WASH) return event.setAndContinue(RawAnimation.begin().thenPlay("sit_wash"));
        if (state == ANIMATION_SLEEP) return event.setAndContinue(RawAnimation.begin().thenLoop("sleep"));
        if (state == ANIMATION_SLEEP2) return event.setAndContinue(RawAnimation.begin().thenLoop("sleep2"));
        if (state == ANIMATION_SLEEP3) return event.setAndContinue(RawAnimation.begin().thenLoop("sleep3"));

        /*
             Если кот ничего особенного не делает, клиент САМ плавно переключает ходьбу и простой
         */
        boolean isMoving = event.isMoving();

        if (isStanding()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop(isMoving ? "stands_paws_walk" : "stands_paws_idle"));
        } else {
            return event.setAndContinue(RawAnimation.begin().thenLoop(isMoving ? "walk" : "idle"));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // ==================== АТРИБУТЫ ====================
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR, 4.0D);
    }

    // ==================== СОХРАНЕНИЕ ДАННЫХ ====================
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Standing", isStanding());
        compound.putInt("AnimationState", getAnimationState());
        compound.putBoolean("HasItem", isHoldingItem());
        compound.putBoolean("Angry", isAngry());
        compound.putInt("Variant", getVariant());
        compound.putBoolean("IsMale", isMale());
        compound.putBoolean("IsBaby", isBabyEntity());
        compound.putInt("GrowthTime", getGrowthTime());
        if (sittingPos != null) compound.putLong("SittingPos", sittingPos.asLong());
        if (sleepSpot != null) compound.putLong("SleepingPos", sleepSpot.asLong());
        if (!this.heldItem.isEmpty()) compound.put("HeldItem", this.heldItem.save(new CompoundTag()));

        net.minecraft.nbt.ListTag items = new net.minecraft.nbt.ListTag();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (!inventory.getItem(i).isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) i);
                inventory.getItem(i).save(tag);
                items.add(tag);
            }
        }
        compound.put("Items", items);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Standing")) setStanding(compound.getBoolean("Standing"));
        if (compound.contains("AnimationState")) setAnimationState(compound.getInt("AnimationState"));
        if (compound.contains("HasItem")) setHasItem(compound.getBoolean("HasItem"));
        if (compound.contains("Angry")) setAngry(compound.getBoolean("Angry"));
        if (compound.contains("Variant")) setVariant(compound.getInt("Variant"));
        if (compound.contains("IsMale")) setMale(compound.getBoolean("IsMale"));
        if (compound.contains("IsBaby")) setBabyEntity(compound.getBoolean("IsBaby"));
        if (compound.contains("GrowthTime")) setGrowthTime(compound.getInt("GrowthTime"));
        if (compound.contains("SittingPos")) sittingPos = BlockPos.of(compound.getLong("SittingPos"));
        if (compound.contains("SleepingPos")) sleepSpot = BlockPos.of(compound.getLong("SleepingPos"));
        if (compound.contains("HeldItem")) this.heldItem = ItemStack.of(compound.getCompound("HeldItem"));

        if (compound.contains("Items")) {
            net.minecraft.nbt.ListTag items = compound.getList("Items", net.minecraft.nbt.Tag.TAG_COMPOUND);
            for (int i = 0; i < items.size(); i++) {
                CompoundTag tag = items.getCompound(i);
                int slot = tag.getByte("Slot") & 255;
                if (slot < inventory.getContainerSize()) inventory.setItem(slot, ItemStack.of(tag));
            }
        }
        refreshDimensions();
    }

    // ==================== СПАВН ====================
    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
                                        @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        if (this.random.nextFloat() < 0.3f) setStanding(true);
        setVariant(getRandomVariant(level.getRandom()));
        setMale(level.getRandom().nextBoolean());
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    private int getRandomVariant(RandomSource random) {
        double val = random.nextDouble();
        double cum = 0;
        for (int i = 0; i < VARIANT_SPAWN_CHANCES.length; i++) {
            cum += VARIANT_SPAWN_CHANCES[i];
            if (val < cum) return i;
        }
        return 0;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, @NotNull MobSpawnType spawnType) {
        BlockPos pos = this.blockPosition();
        BlockState belowState = level.getBlockState(pos.below());

        if (belowState.getFluidState().is(FluidTags.WATER)) {
            return false;
        }

        return super.checkSpawnRules(level, spawnType);
    }

    // ==================== ГЕТТЕРЫ/СЕТТЕРЫ ====================
    public void setSitting(boolean sitting, @Nullable BlockPos pos) {
        this.isSitting = sitting;
        this.sittingPos = pos;
        if (sitting) {
            this.sitDuration = 400 + random.nextInt(1000);
            setAnimationState(ANIMATION_SIT_IDLE);
            getNavigation().stop();
        } else this.sitDuration = 0;
    }

    public void setSleeping(boolean sleeping, @Nullable BlockPos pos) {
        this.isSleeping = sleeping;
        this.sleepSpot = pos;
        if (sleeping) {
            this.sleepDuration = 600 + random.nextInt(1800);
            setAnimationState(ANIMATION_SLEEP + random.nextInt(3));
            getNavigation().stop();
        } else this.sleepDuration = 0;
    }

    public boolean isStanding() {
        return entityData.get(DATA_STANDING);
    }

    public void setStanding(boolean standing) {
        entityData.set(DATA_STANDING, standing);
    }

    public int getAnimationState() {
        return entityData.get(DATA_ANIMATION_STATE);
    }

    public void setAnimationState(int state) {
        entityData.set(DATA_ANIMATION_STATE, state);
    }

    public boolean isHoldingItem() {
        return entityData.get(DATA_HAS_ITEM);
    }

    public void setHasItem(boolean hasItem) {
        entityData.set(DATA_HAS_ITEM, hasItem);
    }

    public boolean isAngry() {
        return entityData.get(DATA_IS_ANGRY);
    }

    public void setAngry(boolean angry) {
        entityData.set(DATA_IS_ANGRY, angry);
        if (angry) angerTime = 400;
    }

    public int getVariant() {
        return entityData.get(DATA_VARIANT);
    }

    public void setVariant(int variant) {
        entityData.set(DATA_VARIANT, variant);
    }

    public ItemStack getHeldItem() {
        return heldItem.copy();
    }
}