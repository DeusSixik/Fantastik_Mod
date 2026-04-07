package net.lisalaf.fantastikmod.entity.custom;

import net.lisalaf.fantastikmod.dialog.DialogScreen;
import net.lisalaf.fantastikmod.dialog.DialogSystem;
import net.lisalaf.fantastikmod.dialog.mobs.KitsuneDialog;
import net.lisalaf.fantastikmod.dialog.mood.MobMood;
import net.lisalaf.fantastikmod.dialog.mood.MoodSystem;
import net.lisalaf.fantastikmod.dialog.mood.impl.KitsuneMood;
import net.lisalaf.fantastikmod.entity.ai.KitsuneLightGoal;
import net.lisalaf.fantastikmod.item.ModItems;
import net.lisalaf.fantastikmod.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.lisalaf.fantastikmod.dialog.Dialog;
import net.lisalaf.fantastikmod.dialog.DialogScreen;
import net.lisalaf.fantastikmod.dialog.mobs.KitsuneDialog;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class KitsuneLightEntity extends Animal implements GeoEntity, MobMood {

    private int sitCooldown = 0;
    private int growTime = 0;
    private final KitsuneMood moodSystem = new KitsuneMood();


    // Константы
    private static final int MIN_TAMING_TOFU = 25;
    private static final int MAX_TAMING_TOFU = 40;


    // Data Parameters
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_EYE_VARIANT = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_HIT_COUNT = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_ANGRY = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_LAUGHING = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_TAMING_PROGRESS = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_TAMED = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_AI_MODE = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_SITTING = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_SLEEPING = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_YAWNING = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SIT_ANIM_PLAYING = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_STAND_ANIM_PLAYING = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SLEEP_ANIM_PLAYING = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_WAKE_ANIM_PLAYING = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_OFFENDED = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> DATA_MOOD = SynchedEntityData.defineId(KitsuneLightEntity.class, EntityDataSerializers.INT);

    // AI Modes
    public static final int AI_FOLLOW = 0;
    public static final int AI_SIT = 1;
    public static final int AI_WANDER = 2;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Таймеры
    private int laughCooldown = 0;
    private int sitTimer = 0;
    private int sleepTimer = 0;
    private int yawnTimer = 0;
    private int animationTimer = 0;
    private int dodgeCooldown = 0;
    private int foxFireCooldown = 0;
    private int magicCooldown = 0;
    private int regenerationCooldown = 0;
    private int runningFromCreeperTicks = 0;

    private UUID ownerUUID;





    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, 0);
        this.entityData.define(DATA_EYE_VARIANT, 0);
        this.entityData.define(DATA_HIT_COUNT, 0);
        this.entityData.define(DATA_IS_ANGRY, false);
        this.entityData.define(DATA_IS_LAUGHING, false);
        this.entityData.define(DATA_TAMING_PROGRESS, 0);
        this.entityData.define(DATA_TAMED, false);
        this.entityData.define(DATA_AI_MODE, AI_FOLLOW);
        this.entityData.define(DATA_IS_SITTING, false);
        this.entityData.define(DATA_IS_SLEEPING, false);
        this.entityData.define(DATA_IS_YAWNING, false);
        this.entityData.define(DATA_SIT_ANIM_PLAYING, false);
        this.entityData.define(DATA_STAND_ANIM_PLAYING, false);
        this.entityData.define(DATA_SLEEP_ANIM_PLAYING, false);
        this.entityData.define(DATA_WAKE_ANIM_PLAYING, false);
        this.entityData.define(DATA_IS_OFFENDED, false);
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
        this.entityData.define(DATA_MOOD, 50);
    }





    // === ОСНОВНЫЕ МЕТОДЫ ===

    @Override
    public void tick() {
        super.tick();
        updateMood();


        if (this.isBaby() && !this.level().isClientSide) {
            growTime++;

            // Плавный рост через scale вместо setAge
            float growthProgress = Math.min(1.0f, (float)growTime / 24000);
            float scale = 0.4f + (0.6f * growthProgress); // от 40% до 100%

            if (growTime >= 24000) {
                this.setAge(0); // Становится взрослой
            }
        }

        if (!this.level().isClientSide && this.tickCount % 200 == 0) {
        }

        updateTimers();
        handleAnimations();



        if (!this.level().isClientSide) {
            handleBehaviors();
            handleAbilities();
        }

        if (!this.level().isClientSide && this.isTamed() &&
                this.getAIMode() == AI_FOLLOW && this.tickCount % 100 == 0) {

            LivingEntity owner = this.getOwner();
            if (owner != null && this.distanceTo(owner) > 16.0F) {
                tryToTeleportToOwner(owner);
            }
        }



        // Строгая блокировка движения
        if (!canMove()) {
            this.getNavigation().stop();
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);

            // Сбрасываем цели для диких кицунэ
            if (!isTamed()) {
                this.setTarget(null);
                this.setAggressive(false);
            }
        }
    }






    private void updateTimers() {
        // Обновление всех таймеров
        if (laughCooldown > 0) laughCooldown--;
        if (dodgeCooldown > 0) dodgeCooldown--;
        if (foxFireCooldown > 0) foxFireCooldown--;
        if (magicCooldown > 0) magicCooldown--;
        if (regenerationCooldown > 0) regenerationCooldown--;
        if (runningFromCreeperTicks > 0) runningFromCreeperTicks--;
        if (animationTimer > 0) animationTimer--;
        if (sitCooldown > 0) sitCooldown--;

        // Сброс злости через 30 секунд
        if (this.tickCount % 600 == 0 && this.isAngry()) {
            this.setAngry(false);
            this.setHitCount(0);
        }
    }

    private void handleAnimations() {
        // Завершение анимаций
        if (animationTimer <= 0) {
            if (isSitAnimPlaying()) {
                setSitAnimPlaying(false);
                setSitting(true); // После анимации садимся
            }
            if (isStandAnimPlaying()) {
                setStandAnimPlaying(false);
                setSitting(false); // После анимации встаем
            }
            if (isSleepAnimPlaying()) {
                setSleepAnimPlaying(false);
                setSleeping(true); // После анимации засыпаем
            }
            if (isWakeAnimPlaying()) {
                setWakeAnimPlaying(false);
                setSleeping(false); // После анимации просыпаемся
            }
        }

        // Обработка зевков
        if (isYawning() && yawnTimer > 0) {
            yawnTimer--;
            if (yawnTimer <= 0) setYawning(false);
        }

        // Обработка смеха
        if (this.laughCooldown > 0) {
            if (this.laughCooldown <= 0) {
                this.setLaughing(false);
            }
            if (this.laughCooldown % 10 == 0 && this.level().isClientSide) {
                this.spawnLaughParticles();
            }
        }
    }

    private void handleBehaviors() {
        // Остановка движения при сидении/сне (более строгая версия)
        if ((isSitting() && !isStandAnimPlaying()) || (isSleeping() && !isWakeAnimPlaying()) ||
                isSitAnimPlaying() || isStandAnimPlaying() || isSleepAnimPlaying() || isWakeAnimPlaying()) {
            this.getNavigation().stop();
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);

            // Дополнительно сбрасываем цели AI
            if (!isTamed()) {
                this.setTarget(null);
            }
        }


        // Автоматическое поведение для диких кицунэ
        if (!isTamed()) {
            handleWildBehavior();
        } else {
            handleTamedBehavior();
        }

        // Обработка сидения
        if (isSitting() && !isStandAnimPlaying()) {
            if (sitTimer > 0) sitTimer--;
            if (sitTimer <= 0 && !isTamed()) {
                startStandAnimation();
            }

            // Случайный зевок
            if (yawnTimer <= 0 && this.random.nextInt(500) == 0 && !isYawning()) {
                setYawning(true);
                yawnTimer = 70;
            }
        }

        // Обработка сна
        if (isSleeping() && !isWakeAnimPlaying()) {
            if (sleepTimer > 0) sleepTimer--;
            if (sleepTimer <= 0) {
                startWakeAnimation();
            }
        }

        if (animationTimer <= 0) {
            if (isSitAnimPlaying()) {
                setSitAnimPlaying(false);
                setSitting(true);
            }
            if (isStandAnimPlaying()) {
                setStandAnimPlaying(false);
                setSitting(false);
            }
            if (isSleepAnimPlaying()) {
                setSleepAnimPlaying(false);
                setSleeping(true);
            }
            if (isWakeAnimPlaying()) {
                setWakeAnimPlaying(false);
                setSleeping(false);
            }
        }

        // Обработка зевков
        if (isYawning() && yawnTimer > 0) {
            yawnTimer--;
            if (yawnTimer <= 0) setYawning(false);
        }

        // Обработка смеха - СБРАСЫВАЕМ ФЛАГ ПО ИСТЕЧЕНИЮ ТАЙМЕРА
        if (isLaughing() && laughCooldown > 0) {
            laughCooldown--;
            if (laughCooldown <= 0) {
                setLaughing(false); // Важно: сбрасываем флаг!
            }

            // Создаем частицы каждые 10 тиков
            if (laughCooldown % 10 == 0 && this.level().isClientSide) {
                this.spawnLaughParticles();
            }
        }
    }



    private void handleWildBehavior() {
        if (!isSitting() && !isSleeping() && !isStandAnimPlaying() && !isWakeAnimPlaying() &&
                !isSitAnimPlaying() && !isSleepAnimPlaying() && sitCooldown <= 0) {

            if (this.random.nextInt(1200) == 0 && this.onGround() && this.getTarget() == null) {
                boolean hasNearbyThreats = !this.level().getEntitiesOfClass(Player.class,
                        this.getBoundingBox().inflate(8.0D)).isEmpty();

                if (!hasNearbyThreats) {
                    if (this.level().isNight() && this.random.nextBoolean()) {
                        startSleepAnimation(); // Ночью спят с анимацией
                    } else {
                        if (this.random.nextInt(7) == 0) {
                            startSitAnimation();
                            sitCooldown = 600;
                        }
                    }
                }
            }
        }
    }


    private void handleTamedBehavior() {
        if (isOffended()) {
            return;
        }
        if ((getAIMode() == AI_FOLLOW || getAIMode() == AI_WANDER) &&
                (isSleeping() || isSitting()) &&
                !isStandAnimPlaying() && !isWakeAnimPlaying()) {

            if (isSleeping()) {
                startWakeAnimation();
            } else if (isSitting()) {
                startStandAnimation();
            }
            return;
        }

        long dayTime = this.level().getDayTime() % 24000;
        boolean isMorning = dayTime >= 0 && dayTime < 12000;

        if (isSleeping() && isMorning && !isWakeAnimPlaying()) {
            startWakeAnimation();
            return;
        }

        if (getAIMode() == AI_SIT && !isSitting() && !isSleeping() &&
                !isSitAnimPlaying() && !isStandAnimPlaying()) {
            startSitAnimation();
            return;
        }

        if (getAIMode() == AI_SIT && isSitting() && !isSleeping() &&
                this.level().isNight() && this.random.nextInt(200) == 0 &&
                !isSleepAnimPlaying() && !isWakeAnimPlaying()) {
            startSleepAnimation();
        }
    }

    private boolean isMorningTime() {
        long dayTime = this.level().getDayTime() % 24000;
        return dayTime >= 0 && dayTime < 12000; // С 6:00 до 18:00
    }


    private void handleAbilities() {
        if (regenerationCooldown <= 0 && this.getHealth() < this.getMaxHealth()) {
            this.heal(1.0F);
            regenerationCooldown = 100;
        }

        if (magicCooldown <= 0) {
            useMagicAbilities();
            magicCooldown = 200;
        }

        // Лисьий огонь
        if (foxFireCooldown <= 0 && isAngry() && getTarget() != null) {
            useFoxFire();
        }

        // Случайные звуки
        if (this.tickCount % 200 == 0 && this.random.nextInt(3) == 0 && !isAngry()) {
            playIdleSound();
        }
        if (isAngry() && this.tickCount % 100 == 0 && this.random.nextInt(2) == 0) {
            playAngrySound();
        }
    }



    // === АНИМАЦИИ ===

    public void startSitAnimation() {
        if (!isSitAnimPlaying() && !isStandAnimPlaying() && !isSitting()) {
            setSitAnimPlaying(true);
            animationTimer = 20;
            getNavigation().stop();
        }
    }

    public void startStandAnimation() {
        if (isSitting()) {
            setStandAnimPlaying(true);
            setSitting(false); // СРАЗУ сбрасываем сидение
            animationTimer = 20;
            getNavigation().stop();
        }
    }

    public void startSleepAnimation() {
        if (isSitting() && !isSleeping() && !isSleepAnimPlaying() && !isWakeAnimPlaying()) {
            setSleepAnimPlaying(true);
            animationTimer = 25;
            getNavigation().stop();
        }
    }

    public void startWakeAnimation() {
        if (isSleeping()) {
            setWakeAnimPlaying(true);
            setSleeping(false); // СРАЗУ сбрасываем сон
            animationTimer = 25;
            getNavigation().stop();
        }
    }

    public void startSleep() {
        if (!isSleeping() && !isSleepAnimPlaying()) {
            if (isSitting() && !isSitAnimPlaying() && !isStandAnimPlaying()) {
                startSleepAnimation(); // Спим из сидячего положения
            } else if (!isSitting() && !isSitAnimPlaying()) {
                // Если не сидит, сначала садится, потом спит
                startSitAnimation();
                sleepTimer = 1200 + random.nextInt(1200);
            }
        }
    }

    // === ИНТЕРАКЦИЯ ===

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND && itemstack.isEmpty()) {
            // Проверяем сон
            if (isSleeping() || isSleepAnimPlaying() || isWakeAnimPlaying()) {
                if (!this.level().isClientSide) {
                    player.displayClientMessage(Component.literal(isRussianPlayer(player) ?
                            "Кицуне спит" : "Kitsune is asleep"), true);
                }
                return InteractionResult.FAIL;
            }

            if (this.isTamed() && moodSystem.getMood() <= -50) {
                if (!this.level().isClientSide) {
                    player.displayClientMessage(Component.literal(isRussianPlayer(player) ?
                            "Кицуне слишком расстроена, чтобы разговаривать" :
                            "Kitsune is too upset to talk"), true);
                }
                return InteractionResult.FAIL;
            }

            boolean canTalk = true;

            if (level().isClientSide) {
                try {
                    Dialog dialog = DialogSystem.getDialog(this);
                    if (dialog != null && dialog.canStart()) {
                        Minecraft.getInstance().setScreen(new DialogScreen(this, dialog));
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        // Для владельца прирученной кицунэ
        if (isTamed() && getOwnerUUID() != null && player.getUUID().equals(getOwnerUUID())) {

            if (hand == InteractionHand.MAIN_HAND &&
                    (itemstack.isEmpty() || itemstack.getItem() == Items.STICK)) {

                if (isSleeping() || isSleepAnimPlaying() || isWakeAnimPlaying()) {
                    if (!this.level().isClientSide) {
                        player.displayClientMessage(Component.literal(isRussianPlayer(player) ?
                                "Кицуне спит, следует разбудить её если вам что-то надо" :
                                "Kitsune is asleep, you should wake her up if you need anything"), true);
                    }
                    return InteractionResult.FAIL;
                }

                // Потребляем палку если используется
                if (itemstack.getItem() == Items.STICK && !player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                cycleAIMode();
                player.displayClientMessage(Component.literal(getAIModeMessage(player)), true);

                // Если переключаем из режима сидения - заставляем встать
                if (getAIMode() != AI_SIT && (isSitting() || isSitAnimPlaying())) {
                    if (isSitAnimPlaying()) {
                        setSitAnimPlaying(false);
                    }
                    startStandAnimation();
                }
                // Если переключаем в режим сидения - садимся
                else if (getAIMode() == AI_SIT && !isSitting() && !isSitAnimPlaying()) {
                    startSitAnimation();
                }

                return InteractionResult.sidedSuccess(level().isClientSide);
            }
        }
        // Приручение и кормление
        else if (itemstack.is(ModItems.TOFU.get()) && !this.isTamed() && !this.isBaby()) {
            return handleTaming(player, itemstack);
        } else if (itemstack.is(ModItems.TOFU.get()) && this.isTamed()) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            if (this.random.nextInt(8) == 0) {
                sendTofuMessage(player);
            }
            if (!level().isClientSide) {
                moodSystem.addMood(2); // +10 к настроению
                syncMoodToClient();
            }
            spawnTamingParticles();
            return InteractionResult.sidedSuccess(level().isClientSide);
        } else if (isFood(itemstack) && this.isBaby()) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            if (this.random.nextInt(3) == 0) {
                growTime += 1200;
            }
            if (this.getHealth() < this.getMaxHealth()) {
                this.heal(2.0F);
            }
            spawnTamingParticles();
            return InteractionResult.sidedSuccess(level().isClientSide);
        } else if (isFood(itemstack) && this.isTamed() && this.getHealth() < this.getMaxHealth()) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            this.heal(4.0F);
            spawnTamingParticles();
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    private boolean isOwner(Player player) {
        try {
            UUID ownerUUID = getOwnerUUID();
            return ownerUUID != null && ownerUUID.equals(player.getUUID());
        } catch (Exception e) {
            return false;
        }
    }





    private void sendTofuMessage(Player player) {
        boolean isRussian = isRussianPlayer(player);

        if (isRussian) {
            String[] messages = {
                    "Вкусно! Принесёшь ещё?",
                    "Ммм, тофу... Мой любимый!",
                    "Хи-хи, спасибо за угощение!",
                    "Такого тофу я ещё не пробовала!",
                    "Ты знаешь, чем меня порадовать.",
                    "Надеюсь, ты припас ещё немного?",
                    "От тофу не откажусь никогда!"
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        } else {
            String[] messages = {
                    "Yummy! Will you bring more?",
                    "Mmm, tofu... My favorite!",
                    "Hehe, thanks for the treat!",
                    "I've never tasted tofu like this!",
                    "You know how to make me happy.",
                    "I hope you saved some more?",
                    "I'll never say no to tofu!"
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        }
    }

    private void cycleAIMode() {
        int newMode = (getAIMode() + 1) % 3;
        setAIMode(newMode);
    }


    private String getAIModeMessage(Player player) {
        boolean isRussian = isRussianPlayer(player);
        switch (getAIMode()) {
            case AI_FOLLOW -> {
                return isRussian ? "Режим: Следование" : "Mode: Following";
            }
            case AI_SIT -> {
                return isRussian ? "Режим: Сидеть" : "Mode: Sitting";
            }
            case AI_WANDER -> {
                return isRussian ? "Режим: Бродить" : "Mode: Wandering";
            }
            default -> {
                return isRussian ? "Режим: Неизвестно" : "Mode: Unknown";
            }
        }
    }

    private InteractionResult handleTaming(Player player, ItemStack itemstack) {
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        int newProgress = getTamingProgress() + 1;
        setTamingProgress(newProgress);
        spawnTamingParticles();

        if ((newProgress >= MIN_TAMING_TOFU && newProgress <= MAX_TAMING_TOFU &&
                random.nextInt(MAX_TAMING_TOFU - newProgress + 1) == 0) ||
                newProgress > MAX_TAMING_TOFU) {
            tame(player);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    // === ПРИРУЧЕНИЕ ===

    private void tame(Player player) {
        setTamed(true);
        setTamingProgress(0);
        setAngry(false);
        setHitCount(0);
        setOwnerUUID(player.getUUID());

        if (!level().isClientSide) {
            moodSystem.setMood(50);
            syncMoodToClient();
        }

        sendTamingMessage(player);
        spawnTamingSuccessParticles();
        playSound(ModSounds.KITSUNE_IDLE1.get(), 1.0F, 1.0F);

        if (!level().isClientSide) {
            this.setTamed(true); // Дублируем для надежности
            this.syncMoodToClient(); // Новый метод (см. ниже)
        }

    }

    private void syncMoodToClient() {
        CompoundTag data = new CompoundTag();
        data.putInt("MoodValue", moodSystem.getMood());
    }

    private void sendTamingMessage(Player player) {
        if (!level().isClientSide) {
            Component message = getTamingMessage(player);
            player.sendSystemMessage(message);
        }
    }

    private Component getTamingMessage(Player player) {
        boolean isRussian = isRussianPlayer(player);
        if (isRussian) {
            String[] messages = {
                    "Думаю, так уж и быть, я сопровожу тебя в твоих приключениях.",
                    "Отныне я следую за тобой, странник.",
                    "Хорошо, я стану твоим спутником.",
                    "Ты заслужил моё доверие. Я с тобой."
            };
            return Component.literal(messages[random.nextInt(messages.length)]);
        } else {
            String[] messages = {
                    "I suppose I'll accompany you on your adventures.",
                    "From now on, I follow you, wanderer.",
                    "Very well, I shall be your companion.",
                    "You have earned my trust. I'm with you."
            };
            return Component.literal(messages[random.nextInt(messages.length)]);
        }
    }


    @Override
    public void die(DamageSource cause) {
        if (!this.level().isClientSide && isTamed()) {
            LivingEntity owner = getOwner();
            if (owner instanceof Player player) {
                Component message = getDeathMessage(player);
                player.sendSystemMessage(message);
            }
        }
        super.die(cause);
    }


    private Component getDeathMessage(Player player) {
        boolean isRussian = isRussianPlayer(player);
        if (isRussian) {
            String[] messages = {
                    "Кицунэ умерла!",
                    "Ваша спутница-кицунэ погибла...",
                    "Кицунэ испустила последний вздох.",
                    "Ваш верный друг покинул этот мир."
            };
            return Component.literal(messages[random.nextInt(messages.length)]);
        } else {
            String[] messages = {
                    "Kitsune has died!",
                    "Your kitsune companion has passed away...",
                    "The kitsune has breathed its last.",
                    "Your faithful friend has left this world."
            };
            return Component.literal(messages[random.nextInt(messages.length)]);
        }
    }

    private boolean isRussianPlayer(Player player) {
        if (player.level().isClientSide) {
            return Minecraft.getInstance().options.languageCode.startsWith("ru");
        } else {
            if (player instanceof ServerPlayer serverPlayer) {
                String language = serverPlayer.getLanguage();
                return language != null && language.startsWith("ru");
            }
            return false;
        }
    }

    private boolean isRussianLanguage(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            String language = serverPlayer.getLanguage();
            return language != null && (language.startsWith("ru_") || language.equals("ru_ru") || language.contains("russian"));
        }
        return false;
    }
    // === АНИМАЦИОННЫЙ КОНТРОЛЛЕР ===

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<KitsuneLightEntity> event) {

        if (isWakeAnimPlaying()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("wake_up"));
            return PlayState.CONTINUE;
        }

        if (isSleepAnimPlaying()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("go_to_sleep"));
            return PlayState.CONTINUE;
        }

        if (isStandAnimPlaying()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("sit_end"));
            return PlayState.CONTINUE;
        }

        if (isSitAnimPlaying()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("sit"));
            return PlayState.CONTINUE;
        }

        if (isSleeping()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("sleep_idle"));
            return PlayState.CONTINUE;
        }

        if (isYawning() && isSitting()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("sit_yawns"));
            return PlayState.CONTINUE;
        }

        if (isLaughing()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("funny"));
            return PlayState.CONTINUE;
        }

        if (isDeadOrDying()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("death"));
            return PlayState.CONTINUE;
        }

        if (swinging) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("attack"));
            return PlayState.CONTINUE;
        }

        if (isSitting()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("sit_idle"));
            return PlayState.CONTINUE;
        }

        if (walkAnimation.isMoving() && canMove()) {
            if (isAngry() || isRunningFromCreeper() || getDeltaMovement().horizontalDistanceSqr() > 0.015D) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("run"));
            } else {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("walk"));
            }
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
        return PlayState.CONTINUE;
    }

    // === AI И ЦЕЛИ ===

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new KitsuneLightGoal.KitsuneEscapeWaterGoal(this, 1.2D));
        this.goalSelector.addGoal(1, new KitsuneLightGoal.AvoidCreeperGoal(this, 1.0D, 1.8D));
        this.goalSelector.addGoal(2, new KitsuneLightGoal.KitsuneEscapeGoal(this, 1.8D));

        // Цели атаки ТОЛЬКО для взрослых
        if (!this.isBaby()) {
            this.goalSelector.addGoal(3, new KitsuneLightGoal.KitsuneAttackGoal(this, 1.2D, true));
            this.goalSelector.addGoal(4, new KitsuneLightGoal.DefendVillagersGoal(this));
            this.goalSelector.addGoal(5, new KitsuneLightGoal.ProtectPlayersGoal(this));
        }

        this.goalSelector.addGoal(6, new KitsuneLightGoal.InteractWithVillagersGoal(this));
        this.goalSelector.addGoal(9, new KitsuneLightGoal.SitGoal(this));
        this.goalSelector.addGoal(8, new KitsuneLightGoal.SleepGoal(this));
        this.goalSelector.addGoal(9, new KitsuneLightGoal.FollowOwnerGoal(this, 1.0D, 3.0F, 10.0F));
        this.goalSelector.addGoal(8, new KitsuneLightGoal.WanderGoal(this, 1.0D));
        this.goalSelector.addGoal(11, new TemptGoal(this, 1.20, Ingredient.of(ModItems.TOFU.get()), false));
        this.goalSelector.addGoal(12, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(13, new KitsuneLightGoal.SocialBehaviorGoal(this));


        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && random.nextInt(3) == 0;
            }
        });

        this.goalSelector.addGoal(15, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(16, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(15, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(15, new LookAtPlayerGoal(this, Animal.class, 6.0F));

        if (!this.isBaby()) {
            this.targetSelector.addGoal(1, new KitsuneLightGoal.KitsuneHurtByTargetGoal(this));
            this.targetSelector.addGoal(2, new KitsuneLightGoal.DefendOwnerGoal(this));
            this.targetSelector.addGoal(3, new KitsuneLightGoal.KitsuneDefendVillagersTargetGoal(this));
        }
    }



    // === ГЕТТЕРЫ И СЕТТЕРЫ ===

    public int getAIMode() { return entityData.get(DATA_AI_MODE); }
    public void setAIMode(int mode) { entityData.set(DATA_AI_MODE, mode); }

    public boolean isSitting() { return entityData.get(DATA_IS_SITTING); }
    public void setSitting(boolean sitting) {
        entityData.set(DATA_IS_SITTING, sitting);
        if (sitting && !isOffended()) {
            sitTimer = 60 + random.nextInt(120);
        } else if (!sitting) {
            sitTimer = 0;
        }
    }

    public boolean isSleeping() { return entityData.get(DATA_IS_SLEEPING); }
    public void setSleeping(boolean sleeping) {
        entityData.set(DATA_IS_SLEEPING, sleeping);
        if (sleeping) {
            sleepTimer = 400 + random.nextInt(400);
        } else {
            sleepTimer = 0;
        }
    }


    public boolean isYawning() { return entityData.get(DATA_IS_YAWNING); }
    public void setYawning(boolean yawning) { entityData.set(DATA_IS_YAWNING, yawning); }

    public boolean isSitAnimPlaying() { return entityData.get(DATA_SIT_ANIM_PLAYING); }
    public void setSitAnimPlaying(boolean playing) { entityData.set(DATA_SIT_ANIM_PLAYING, playing); }

    public boolean isStandAnimPlaying() { return entityData.get(DATA_STAND_ANIM_PLAYING); }
    public void setStandAnimPlaying(boolean playing) { entityData.set(DATA_STAND_ANIM_PLAYING, playing); }

    public boolean isSleepAnimPlaying() { return entityData.get(DATA_SLEEP_ANIM_PLAYING); }
    public void setSleepAnimPlaying(boolean playing) { entityData.set(DATA_SLEEP_ANIM_PLAYING, playing); }

    public boolean isWakeAnimPlaying() { return entityData.get(DATA_WAKE_ANIM_PLAYING); }
    public void setWakeAnimPlaying(boolean playing) { entityData.set(DATA_WAKE_ANIM_PLAYING, playing); }

    public int getHitCount() { return entityData.get(DATA_HIT_COUNT); }
    public void setHitCount(int count) { entityData.set(DATA_HIT_COUNT, count); }

    public boolean isAngry() { return entityData.get(DATA_IS_ANGRY); }
    public void setAngry(boolean angry) { entityData.set(DATA_IS_ANGRY, angry); }

    public boolean isLaughing() { return entityData.get(DATA_IS_LAUGHING); }
    public void setLaughing(boolean laughing) { entityData.set(DATA_IS_LAUGHING, laughing); }

    public int getVariant() { return entityData.get(DATA_VARIANT); }
    public void setVariant(int variant) { entityData.set(DATA_VARIANT, variant); }

    public int getEyeVariant() { return entityData.get(DATA_EYE_VARIANT); }
    public void setEyeVariant(int eyeVariant) { entityData.set(DATA_EYE_VARIANT, eyeVariant); }

    public int getTamingProgress() { return entityData.get(DATA_TAMING_PROGRESS); }
    public void setTamingProgress(int progress) { entityData.set(DATA_TAMING_PROGRESS, progress); }

    public boolean isTamed() { return entityData.get(DATA_TAMED); }
    public void setTamed(boolean tamed) { entityData.set(DATA_TAMED, tamed); }

    public boolean isRunningFromCreeper() { return runningFromCreeperTicks > 0; }
    public void setRunningFromCreeper(boolean running) {
        runningFromCreeperTicks = running ? 40 : 0;
    }



    public int getLaughCooldown() { return laughCooldown; }
    public void triggerLaughAnimation() {
        if (laughCooldown == 0) {
            laughCooldown = 80; // 4 секунды
            setLaughing(true);

            // Воспроизводим звук сразу при запуске анимации
            if (!level().isClientSide) {
                playLaughSound();
            }
        }
    }

    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID).orElse(null);
    }

    public void setOwnerUUID(UUID uuid) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(uuid));
        this.ownerUUID = uuid; // для обратной совместимости
    }
    public LivingEntity getOwner() {
        return ownerUUID != null ? level().getPlayerByUUID(ownerUUID) : null;
    }

    public boolean isOffended() { return entityData.get(DATA_IS_OFFENDED); }
    public void setOffended(boolean offended) {
        entityData.set(DATA_IS_OFFENDED, offended);
        if (offended) {
            // При обиде устанавливаем длительный таймер сидения
            sitTimer = 1200; // 60 секунд
        }
    }

    public int getMood() {
        return entityData.get(DATA_MOOD);
    }

    public void setMood(int mood) {
        int clampedMood = Math.min(100, Math.max(-100, mood));
        entityData.set(DATA_MOOD, clampedMood);

        if (!level().isClientSide) {
            moodSystem.setMood(clampedMood);
        }
    }

    public void addMood(int amount) {
        int current = getMood();
        setMood(current + amount);
    }

    // === АТРИБУТЫ И БАЗОВЫЕ МЕТОДЫ ===

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 18)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        // Всегда можно кормить тофу
        if (stack.is(ModItems.TOFU.get())) {
            return true;
        }

        if (this.isBaby() || this.isTamed()) {
            return stack.is(Items.RABBIT) || stack.is(Items.COOKED_RABBIT) ||
                    stack.is(Items.CHICKEN) || stack.is(Items.COOKED_CHICKEN) ||
                    stack.is(Items.COD) || stack.is(Items.COOKED_COD) ||
                    stack.is(Items.SALMON) || stack.is(Items.COOKED_SALMON) ||
                    stack.is(Items.SWEET_BERRIES) || stack.is(Items.GLOW_BERRIES);
        }

        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        if (reason != MobSpawnType.BREEDING) {
            setVariant(level.getRandom().nextInt(6));
            setEyeVariant(level.getRandom().nextInt(8));

            // Шанс 20% спавна детёнышем из яйца призыва
            if (reason == MobSpawnType.SPAWN_EGG && level.getRandom().nextFloat() < 0.2f) {
                this.setBaby(true);
            }
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("GrowTime", growTime);
        compound.putInt("Variant", getVariant());
        compound.putInt("HitCount", getHitCount());
        compound.putBoolean("IsAngry", isAngry());
        compound.putBoolean("IsLaughing", isLaughing());
        compound.putInt("TamingProgress", getTamingProgress());
        compound.putBoolean("Tamed", isTamed());
        compound.putInt("AIMode", getAIMode());
        compound.putBoolean("Sitting", isSitting());
        compound.putBoolean("Sleeping", isSleeping());
        compound.putBoolean("Yawning", isYawning());
        compound.putInt("EyeVariant", getEyeVariant());
        compound.putBoolean("Offended", isOffended());
        if (ownerUUID != null) {
            compound.putUUID("Owner", ownerUUID);
        }
        compound.putInt("MoodValue", moodSystem.getMood());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("GrowTime")) {
            growTime = compound.getInt("GrowTime");
        }
        if (compound.contains("Variant")) setVariant(compound.getInt("Variant"));
        if (compound.contains("HitCount")) setHitCount(compound.getInt("HitCount"));
        if (compound.contains("IsAngry")) setAngry(compound.getBoolean("IsAngry"));
        if (compound.contains("IsLaughing")) setLaughing(compound.getBoolean("IsLaughing"));
        if (compound.contains("TamingProgress")) setTamingProgress(compound.getInt("TamingProgress"));
        if (compound.contains("Tamed")) setTamed(compound.getBoolean("Tamed"));
        if (compound.contains("AIMode")) setAIMode(compound.getInt("AIMode"));
        if (compound.contains("Sitting")) setSitting(compound.getBoolean("Sitting"));
        if (compound.contains("Sleeping")) setSleeping(compound.getBoolean("Sleeping"));
        if (compound.contains("Yawning")) setYawning(compound.getBoolean("Yawning"));
        if (compound.contains("EyeVariant")) setEyeVariant(compound.getInt("EyeVariant"));
        if (compound.contains("Offended")) setOffended(compound.getBoolean("Offended"));
        if (compound.hasUUID("Owner")) setOwnerUUID(compound.getUUID("Owner"));
        if (!compound.contains("CustomName") && this.getCustomName() == null) {
            setRandomName();
        }
        if (compound.contains("MoodValue")) {
            moodSystem.setMood(compound.getInt("MoodValue"));
        }

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean wasSleeping = this.isSleeping();
        Player attacker = null;
        if (source.getEntity() instanceof Player player) {
            attacker = player;
        }

        int oldHitCount = getHitCount();
        if (source.getEntity() instanceof Player) {
            setHitCount(oldHitCount + 1);
        }

        if (!this.level().isClientSide && amount > 0) {
            resetRestingAnimations();
        }

        if (source.getDirectEntity() instanceof AbstractArrow && dodgeCooldown <= 0) {
            if (random.nextFloat() < 0.3f) {
                dodgeArrow((Projectile) source.getDirectEntity());
                dodgeCooldown = 60;
                return false;
            }
        }
        boolean hurt = super.hurt(source, amount);

        if (!this.level().isClientSide && wasSleeping && attacker != null) {
            sendWakeUpMessage(attacker);
        }

        if (!this.level().isClientSide && this.isTamed() && attacker != null &&
                attacker.getUUID().equals(this.getOwnerUUID()) && !wasSleeping) {

            if (this.random.nextInt(5) == 0) {
                sendOwnerHurtMessage(attacker);
            }
        }

        if (!this.level().isClientSide && !this.isTamed() && attacker != null) {
            if (oldHitCount == 0 && getHitCount() == 1) {
                sendWildHurtMessage(attacker);
            } else if (oldHitCount == 1 && getHitCount() == 2) {
                sendAngryWildMessage(attacker);
                setAngry(true);
            }
        }

        return hurt;
    }


    @Override
    public boolean doHurtTarget(Entity target) {
        if (this.isBaby()) {
            return false;
        }

        if (!this.level().isClientSide) {
            resetRestingAnimations();
        }
        return super.doHurtTarget(target);
    }

    // === ЗВУКИ ===

    public void playIdleSound() {
        if (!level().isClientSide) {
            SoundEvent[] idleSounds = {ModSounds.KITSUNE_IDLE1.get(), ModSounds.KITSUNE_IDLE2.get(),
                    ModSounds.KITSUNE_IDLE3.get(), ModSounds.KITSUNE_IDLE4.get()};
            playSound(idleSounds[random.nextInt(idleSounds.length)], 0.6F, 1.0F);
        }
    }

    public void playLaughSound() {
        if (!level().isClientSide) {
            SoundEvent[] laughSounds = {ModSounds.KITSUNE_LAUGH1.get(), ModSounds.KITSUNE_LAUGH2.get(),
                    ModSounds.KITSUNE_LAUGH3.get()};
            playSound(laughSounds[random.nextInt(laughSounds.length)], 0.8F, 1.0F);
        }
    }

    public void playAngrySound() {
        if (!level().isClientSide) {
            playSound(ModSounds.KITSUNE_ANGRY1.get(), 0.7F, 0.9F + random.nextFloat() * 0.2F);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        SoundEvent[] hurtSounds = {ModSounds.KITSUNE_HURT1.get(), ModSounds.KITSUNE_HURT2.get()};
        return hurtSounds[random.nextInt(hurtSounds.length)];
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.KITSUNE_HURT1.get();
    }

    // === СПОСОБНОСТИ (сохранены авторские эффекты) ===

    private void dodgeArrow(Projectile arrow) {
        if (level().isClientSide) return;
        Vec3 arrowDirection = arrow.getDeltaMovement().normalize();
        Vec3 dodgeDirection = new Vec3(-arrowDirection.z, 0, arrowDirection.x).normalize();
        if (random.nextBoolean()) dodgeDirection = dodgeDirection.scale(-1);
        setDeltaMovement(dodgeDirection.x * 1.5D, 0.3D, dodgeDirection.z * 1.5D);
        spawnDodgeParticles();
    }

    private void useFoxFire() {
        if (level().isClientSide || getTarget() == null) return;
        LivingEntity target = getTarget();
        double distance = distanceToSqr(target);
        if (distance >= 16.0D && distance <= 144.0D && random.nextFloat() < 0.2f) {
            spawnFoxFire(target);
            foxFireCooldown = 100;
        }
    }

    private void useMagicAbilities() {
        if (level().isClientSide) return;

        this.createExplosionProtectionField();

        List<Creeper> nearbyCreepers = this.level().getEntitiesOfClass(Creeper.class,
                this.getBoundingBox().inflate(10.0D));

        if (!nearbyCreepers.isEmpty()) {
            this.protectFromCreepers();
        }

        this.healAllies();

        this.applySelfBuffs();
    }

    private void protectFromCreepers() {
        List<Creeper> nearbyCreepers = this.level().getEntitiesOfClass(Creeper.class,
                this.getBoundingBox().inflate(12.0D));

        for (Creeper creeper : nearbyCreepers) {
            boolean shouldProtect = creeper.isIgnited() ||
                    creeper.getSwellDir() > 0 ||
                    this.distanceToSqr(creeper) < 36.0D;

            if (shouldProtect) {
                List<KitsuneLightEntity> nearbyKitsune = this.level().getEntitiesOfClass(
                        KitsuneLightEntity.class, this.getBoundingBox().inflate(16.0D));

                for (KitsuneLightEntity kitsune : nearbyKitsune) {
                    if (!kitsune.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
                        kitsune.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1));
                    }
                    if (!kitsune.hasEffect(MobEffects.REGENERATION)) {
                        kitsune.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
                    }
                }
                this.spawnMagicParticles();

                if (!this.level().isClientSide) {
                    this.playSound(ModSounds.KITSUNE_IDLE1.get(), 0.8F, 1.2F);
                }
                break;
            }
        }
    }

    private void healAllies() {
        if (this.getHealth() < this.getMaxHealth() * 0.7f) {
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));
        }
        List<KitsuneLightEntity> nearbyKitsune = this.level().getEntitiesOfClass(
                KitsuneLightEntity.class, this.getBoundingBox().inflate(10.0D));

        for (KitsuneLightEntity kitsune : nearbyKitsune) {
            if (kitsune.getHealth() < kitsune.getMaxHealth() * 0.5f) {
                kitsune.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
            }
        }
    }

    private void applySelfBuffs() {
        if (this.random.nextFloat() < 0.1f) {
            MobEffect[] possibleBuffs = {
                    MobEffects.MOVEMENT_SPEED,
                    MobEffects.DAMAGE_BOOST,
                    MobEffects.DAMAGE_RESISTANCE
            };

            MobEffect selectedBuff = possibleBuffs[this.random.nextInt(possibleBuffs.length)];
            this.addEffect(new MobEffectInstance(selectedBuff, 300, 0));
        }
    }

    private void createExplosionProtectionField() {
        List<Creeper> nearbyCreepers = this.level().getEntitiesOfClass(Creeper.class,
                this.getBoundingBox().inflate(12.0D)); // Увеличиваем радиус

        for (Creeper creeper : nearbyCreepers) {
            boolean shouldProtect = creeper.isIgnited() ||
                    creeper.getSwellDir() > 0 ||
                    this.getTarget() == creeper ||
                    this.distanceToSqr(creeper) < 25.0D; // 5 блоков

            if (shouldProtect) {
                CompoundTag persistentData = creeper.getPersistentData();
                persistentData.putBoolean("KitsuneProtectedExplosion", true);
                persistentData.putLong("KitsuneProtectionTime", this.level().getGameTime() + 300);
            }
        }
    }

    // === ЧАСТИЦЫ ===

    private void spawnTamingParticles() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 7; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                        this.getRandomX(1.0D),
                        this.getRandomY() + 0.5D,
                        this.getRandomZ(1.0D),
                        d0, d1, d2);
            }
        }
    }

    private void spawnTamingSuccessParticles() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 15; ++i) {
                this.level().addParticle(ParticleTypes.FIREWORK,
                        this.getRandomX(1.5D),
                        this.getRandomY() + 1.0D,
                        this.getRandomZ(1.5D),
                        this.random.nextGaussian() * 0.05D,
                        this.random.nextGaussian() * 0.05D + 0.2D,
                        this.random.nextGaussian() * 0.05D);
            }
        }
    }



    private void spawnDodgeParticles() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(ParticleTypes.CLOUD,
                        this.getRandomX(0.5D),
                        this.getRandomY(),
                        this.getRandomZ(0.5D),
                        0, 0.1, 0);
            }
        }
    }

    private void spawnFoxFire(LivingEntity target) {
        if (this.level().isClientSide) return;

        for(int i = 0; i < 3; ++i) {
            double dx = target.getX() - this.getX();
            double dy = target.getY(0.5) - this.getY(0.5);
            double dz = target.getZ() - this.getZ();

            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        this.getX() + (dx * 0.25),
                        this.getY(0.5) + (dy * 0.25),
                        this.getZ() + (dz * 0.25),
                        5, 0.2, 0.2, 0.2, 0.05);
            }
        }

        target.hurt(this.damageSources().mobAttack(this), 3.0F);
        target.setSecondsOnFire(5);

        this.playSound(ModSounds.KITSUNE_ANGRY1.get(), 1.0F, 1.5F);
    }

    private void spawnLaughParticles() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 3; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;

                this.level().addParticle(
                        ParticleTypes.HAPPY_VILLAGER,
                        this.getRandomX(1.0D),
                        this.getRandomY() + 0.5D,
                        this.getRandomZ(1.0D),
                        d0, d1, d2
                );
            }
        }
    }

    private void spawnMagicParticles() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 10; ++i) {
                this.level().addParticle(ParticleTypes.ENCHANT,
                        this.getRandomX(1.5D),
                        this.getRandomY() + 0.5D,
                        this.getRandomZ(1.5D),
                        0, 0.1, 0);
            }
        }
    }

    private void spawnHealingParticles(KitsuneLightEntity target) {
        if (this.level().isClientSide) {
            for(int i = 0; i < 5; ++i) {
                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                        target.getRandomX(1.0D),
                        target.getRandomY() + 0.5D,
                        target.getRandomZ(1.0D),
                        0, 0.1, 0);
            }
        }
    }

    private void spawnBuffParticles() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(ParticleTypes.ELECTRIC_SPARK,
                        this.getRandomX(1.0D),
                        this.getRandomY() + 0.5D,
                        this.getRandomZ(1.0D),
                        0, 0.05, 0);
            }
        }
    }

    public boolean canMove() {
        boolean cannotMove = isSitting() ||
                isSleeping() ||
                isSitAnimPlaying() ||
                isStandAnimPlaying() ||
                isSleepAnimPlaying() ||
                isWakeAnimPlaying() ||
                isDeadOrDying() ||
                !isAlive() ||
                isNoAi() ||
                isOffended();

        return !cannotMove;
    }



    public void resetRestingAnimations() {
        setSitting(false);
        setSleeping(false);
        setSitAnimPlaying(false);
        setStandAnimPlaying(false);
        setSleepAnimPlaying(false);
        setWakeAnimPlaying(false);
        setYawning(false);

        sitTimer = 0;
        sleepTimer = 0;
        animationTimer = 0;
        yawnTimer = 0;
        this.getNavigation().stop();
    }


    @Override
    public boolean canUpdate() {
        return super.canUpdate() && !isSleeping();
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (isSitting() || isSleeping() || isSitAnimPlaying() || isStandAnimPlaying() ||
                isSleepAnimPlaying() || isWakeAnimPlaying()) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            return;
        }
        super.travel(travelVector);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return !isSitting() && !isSleeping() && super.causeFallDamage(distance, damageMultiplier, source);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (isSitting() || isSleeping() || isSitAnimPlaying() || isStandAnimPlaying() ||
                isSleepAnimPlaying() || isWakeAnimPlaying()) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            this.getNavigation().stop();
        }
    }

    private void tryToTeleportToOwner(LivingEntity owner) {
        for (int i = 0; i < 10; i++) {
            double x = owner.getX() + (this.random.nextDouble() - 0.5) * 8;
            double y = owner.getY() + this.random.nextInt(3) - 1;
            double z = owner.getZ() + (this.random.nextDouble() - 0.5) * 8;

            if (this.canTeleportTo(new BlockPos((int)x, (int)y, (int)z))) {
                this.teleportTo(x, y, z);
                this.getNavigation().stop();
                spawnTeleportParticles();
                break;
            }
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        return this.level().isEmptyBlock(pos) &&
                this.level().isEmptyBlock(pos.above()) &&
                this.level().getBlockState(pos.below()).isSolid();
    }

    private void spawnTeleportParticles() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 15; i++) {
                this.level().addParticle(ParticleTypes.PORTAL,
                        this.getRandomX(1.0), this.getRandomY(), this.getRandomZ(1.0),
                        (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(),
                        (this.random.nextDouble() - 0.5) * 2.0);
            }
        }
    }

    @Override
    protected void dropExperience() {
        if (this.shouldDropExperience()) {
            this.dropExperience(5 + this.random.nextInt(6));
        }
    }

    private void dropExperience(int experience) {
        super.dropExperience();
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int lootingLevel, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, lootingLevel, recentlyHit);

        if (this.random.nextFloat() < 0.7f) {
            this.spawnAtLocation(ModItems.GEMKITSUNE.get());
        }
    }

    @Override
    public Component getDisplayName() {
        if (this.hasCustomName()) {
            return this.getCustomName();
        }
        return Component.translatable("entity.fantastikmod.kitsune_light");
    }

    public boolean isTame() {
        return this.isTamed();
    }

    public void setTame(boolean tame) {
        this.setTamed(tame);
        if (tame && this.getOwnerUUID() != null) {
            this.setOwnerUUID(this.getOwnerUUID()); // Просто подтверждаем
        }
    }

    private static final String[][] KITSUNE_NAMES = {
            {"Akari", "Акари"}, {"Yuki", "Юки"}, {"Hikari", "Хикари"}, {"Kiko", "Кико"}, {"Sakura", "Сакура"},
            {"Ren", "Рэн"}, {"Kai", "Кай"}, {"Haru", "Хару"}, {"Aiko", "Айко"}, {"Mitsuki", "Мицуки"},
            {"Kitsune", "Кицуне"}, {"Yoru", "Ёру"}, {"Hoshi", "Хоси"}, {"Sora", "Сора"}, {"Kaze", "Кадзэ"},
            {"Tsuki", "Цуки"}, {"Hana", "Хана"}, {"Rin", "Рин"}, {"Kuma", "Кума"}, {"Tora", "Тора"},
            {"Shiro", "Сиро"}, {"Kuro", "Куро"}, {"Momo", "Момо"}, {"Botan", "Ботан"}, {"Fuji", "Фудзи"},
            {"Asa", "Аса"}, {"Yume", "Юмэ"}, {"Kokoro", "Кокоро"}, {"Aoi", "Аой"}, {"Midori", "Мидори"},
            {"Akane", "Аканэ"}, {"Sumire", "Сумирэ"}, {"Ran", "Ран"}, {"Shinju", "Синдзю"}, {"Kaguya", "Кагуя"},
            {"Tamamo", "Тамамо"}, {"Kuzunoha", "Кудзуноха"}, {"Nogitsune", "Ногицунэ"}, {"Myobu", "Мёбу"}
    };

    public KitsuneLightEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        setRandomName();
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.hasCustomName();
    }

    @Override
    public boolean hasCustomName() {
        return this.getCustomName() != null && !this.getCustomName().getString().isEmpty();
    }

    private void setRandomName() {

        if (this.getCustomName() == null) {
            String[] namePair = KITSUNE_NAMES[this.random.nextInt(KITSUNE_NAMES.length)];
            String name = shouldUseRussian() ? namePair[1] : namePair[0];
            this.setCustomName(Component.literal(name));
        } else {
        }
    }

    private boolean shouldUseRussian() {
        if (this.level().isClientSide) {
            return Minecraft.getInstance().options.languageCode.startsWith("ru");
        } else {

            Player nearestPlayer = this.level().getNearestPlayer(this, 10);
            return nearestPlayer != null && isRussianPlayer(nearestPlayer);
        }
    }

    private void sendMessage(Player player, String message) {
        String kitsuneName = this.getCustomName() != null ? this.getCustomName().getString() : "Кицуне";
        String fullMessage = kitsuneName + ": " + message;
        player.sendSystemMessage(Component.literal(fullMessage));
        System.out.println("=== ОТЛАДКА: Сообщение отправлено: " + fullMessage + " ===");
    }



    private void sendNameTagRejectionMessage(Player player) {
        System.out.println("=== ОТЛАДКА: sendNameTagRejectionMessage вызван ===");

        if (!this.level().isClientSide) {
            boolean isRussian = isRussianPlayer(player);
            System.out.println("Язык русский: " + isRussian);

            Component message;
            if (this.isTamed()) {
                String[] messages = isRussian ?
                        new String[]{
                                "Моё имя было даровано, а не изменено.",
                                "Я ношу это имя с гордостью, зачем его менять?",
                                "Это имя стало частью моей души.",
                                "Меня назвали так не просто так.",
                                "Я привыкла к этому имени.",
                                "Пусть всё остаётся как есть."
                        } :
                        new String[]{
                                "My name was given, not changed.",
                                "I wear this name with pride, why change it?",
                                "This name has become part of my soul.",
                                "I was named this way for a reason.",
                                "I've grown accustomed to this name.",
                                "Let things remain as they are."
                        };
                message = Component.literal(messages[this.random.nextInt(messages.length)]);
            } else {
                String[] messages = isRussian ?
                        new String[]{
                                "Смертные не дают имён духам.",
                                "Ты не имеешь права нарекать меня.",
                                "Моё имя известно лишь ветру и луне."
                        } :
                        new String[]{
                                "Mortals do not name spirits.",
                                "You have no right to name me.",
                                "My name is known only to the wind and moon."
                        };
                message = Component.literal(messages[this.random.nextInt(messages.length)]);
            }

            // Отправляем сообщение игроку
            player.sendSystemMessage(message);
            System.out.println("Сообщение отправлено: " + message.getString());
        }
    }

    private void sendWakeUpMessage(Player player) {
        boolean isRussian = isRussianPlayer(player);

        if (isRussian) {
            String[] messages = {
                    "Уже утро? Кажется, я проспала всего пару минут...",
                    "Мне снилось, что я стала великой небесной лисой... Зачем мешаешь?",
                    "Ай! Я же только прилегла... Ты всегда знаешь, когда меня разбудить.",
                    "Ты всегда знаешь, когда меня разбудить в самый интересный момент сна...",
                    "Неужели я так крепко уснула? Кажется, пролетела целая вечность...",
                    "Кажется, я проспала всё на свете... Спасибо что разбудил!",
                    "Я так глубоко заснула, что чуть не потерялась в своих снах...",
                    "Как же хорошо было спать... Жаль, что всё хорошее когда-то кончается."
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        } else {
            String[] messages = {
                    "Morning already? I feel like I only slept a couple minutes...",
                    "I dreamed I became a great celestial fox... Why disturb me?",
                    "Ouch! I just lay down... You always know when to wake me.",
                    "You always know when to wake me at the most interesting part of my dream...",
                    "Did I really fall asleep so deeply? Feels like an eternity passed...",
                    "I think I slept through everything... Thanks for waking me up!",
                    "I fell so deeply asleep, I almost got lost in my dreams...",
                    "It was so nice to sleep... Too bad all good things must end."
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        }
    }

    private void sendWildHurtMessage(Player player) {
        boolean isRussian = isRussianPlayer(player);

        if (isRussian) {
            String[] messages = {
                    "Эй, это невежливо - бить незнакомцев!",
                    "Ой! А мы разве знакомы настолько близко?",
                    "Не ожидала такого грубого приветствия...",
                    "Есть более цивилизованные способы познакомиться!",
                    "Какой неприятный сюрприз... Я ведь ничего тебе не сделала.",
                    "Не думала, что наша встреча начнётся с тумака...",
                    "Вот уж действительно - нежданный комплимент!"
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        } else {
            String[] messages = {
                    "Hey, it's not polite to hit strangers!",
                    "Ouch! Are we really that close already?",
                    "Didn't expect such a rude greeting...",
                    "There are more civilized ways to introduce yourself!",
                    "What an unpleasant surprise... I haven't done anything to you.",
                    "I didn't think our meeting would start with a punch...",
                    "Now that's what I call an unexpected compliment!"
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        }
    }

    private void sendAngryWildMessage(Player player) {
        boolean isRussian = isRussianPlayer(player);

        if (isRussian) {
            String[] messages = {
                    "Вот и всё! Ты сам напросился на ответный удар!",
                    "Дважды бить без причины? Теперь ты получишь по заслугам!",
                    "Хорошо, раз ты настаиваешь... Принимай ответ!",
                    "Предупреждала же! Теперь не жалуйся на последствия.",
                    "Ну всё, игра началась! Посмотрим, кто кого...",
                    "Ты перешёл черту! Теперь я не буду сдерживаться!",
                    "Два предупреждения достаточно. Готовься к бою!"
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        } else {
            String[] messages = {
                    "That's it! You asked for a fight!",
                    "Hitting me twice for no reason? Now you'll get what you deserve!",
                    "Well, since you insist... Take this!",
                    "I warned you! Don't complain about the consequences now.",
                    "Alright, game on! Let's see who's tougher...",
                    "You crossed the line! I won't hold back anymore!",
                    "Two warnings are enough. Prepare for battle!"
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        }
    }

    private void sendOwnerHurtMessage(Player player) {
        boolean isRussian = isRussianPlayer(player);

        if (isRussian) {
            String[] messages = {
                    "За что?.. Я же тебе доверяла...",
                    "Больно! Разве так поступают с друзьями?",
                    "Эй, я на твоей стороне, помнишь?",
                    "В каждом ударе - разбитая частица доверия...",
                    "Я помню, как ты давал мне тофу, а теперь... это?",
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        } else {
            String[] messages = {
                    "Why?.. I trusted you...",
                    "That hurts! Is this how you treat friends?",
                    "Hey, I'm on your side, remember?",
                    "In every blow - a broken piece of trust...",
                    "I remember when you gave me tofu, and now... this?",
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        }
    }

    private void sendAngryTofuMessage(Player player) {
        boolean isRussian = isRussianPlayer(player);

        if (isRussian) {
            String[] messages = {
                    "Без свежего тофу я даже слушать тебя не буду!",
                    "Хмф! Мое сердце залечит только самый нежный тофу... И никак иначе!",
                    "Ты ранил не только меня, но и нашу связь... Тофу может всё исправить, но не сразу.",
                    "Я приму твои извинения... в виде тофу. И никак иначе!",
                    "Моя лисья гордость ранена! Потребуется особый тофу чтобы её исцелить.",
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        } else {
            String[] messages = {
                    "Without fresh tofu, I won't even listen to you!",
                    "Hmph! Only the most delicate tofu can heal my heart... Nothing else!",
                    "You hurt not only me but our bond... Tofu can fix everything, but not immediately.",
                    "I'll accept your apology... in the form of tofu. No other way!",
                    "My fox pride is wounded! It will take special tofu to heal it.",
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        }
    }

    private void sendForgivenessMessage(Player player) {
        boolean isRussian = isRussianPlayer(player);

        if (isRussian) {
            String[] messages = {
                    "Ладно... я тебя прощаю. Но больше так не делай!",
                    "Этот тофу такой же нежный, как и должен быть... Прощаю.",
                    "Моя лисья гордость смягчается... Ты искупил свою вину.",
                    "Вкусно! Всё прощено!",
                    "Ты помнишь, что важно для меня... Это значит многое. Прощаю.",
                    "Как же приятно, когда тебя понимают... Спасибо за тофу и извинения."
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        } else {
            String[] messages = {
                    "Okay... I forgive you. But don't do it again!",
                    "This tofu is as delicate as it should be... I forgive you.",
                    "My fox pride softens... You've redeemed yourself.",
                    "Delicious! All is forgiven!",
                    "You remember what's important to me... That means a lot. I forgive you.",
                    "How nice to be understood... Thank you for the tofu and apologies."
            };
            player.sendSystemMessage(Component.literal(messages[random.nextInt(messages.length)]));
        }
    }
    @Override
    public MoodSystem getMoodSystem() {
        return moodSystem;
    }

    @Override
    public String getMobTypeName() {
        return "kitsune_light";
    }

    @Override
    public String getOwnerName() {
        if (this.getOwner() != null) {
            return this.getOwner().getDisplayName().getString();
        }
        return "";
    }

    private void updateMood() {
        if (!this.level().isClientSide) {
            moodSystem.updateMood(this.level(), this);
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }



}