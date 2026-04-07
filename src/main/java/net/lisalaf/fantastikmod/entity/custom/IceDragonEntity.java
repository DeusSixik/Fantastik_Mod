package net.lisalaf.fantastikmod.entity.custom;

import net.lisalaf.fantastikmod.entity.ai.IceDragonGoal;
import net.lisalaf.fantastikmod.item.ModItems;
import net.lisalaf.fantastikmod.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IceDragonEntity extends Animal implements GeoEntity, FlyingAnimal {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(IceDragonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_EYE_VARIANT = SynchedEntityData.defineId(IceDragonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_FLYING = SynchedEntityData.defineId(IceDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_TAKEOFF = SynchedEntityData.defineId(IceDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_LANDING = SynchedEntityData.defineId(IceDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_FEMALE = SynchedEntityData.defineId(IceDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_AGE = SynchedEntityData.defineId(IceDragonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_GROWTH_STAGE = SynchedEntityData.defineId(IceDragonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_ATTACKING = SynchedEntityData.defineId(IceDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_NAME = SynchedEntityData.defineId(IceDragonEntity.class, EntityDataSerializers.STRING);

    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation FLIGHT_START_ANIMATION = RawAnimation.begin().thenPlay("flight_started");
    private static final RawAnimation FLIGHT_IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle_fly");
    private static final RawAnimation FLIGHT_ANIMATION = RawAnimation.begin().thenLoop("fly");
    private static final RawAnimation LAND_ANIMATION = RawAnimation.begin().thenPlay("land");
    private static final RawAnimation ATTACK_ANIMATION = RawAnimation.begin().thenPlay("attack1");

    private int takeoffAnimationTimer = 0;
    private int landingAnimationTimer = 0;
    private int attackTimer = 0;
    private int furDropTimer = 0;

    private static final int GROWTH_INTERVAL = 24000;
    private static final int FUR_DROP_INTERVAL = 10 * 60 * 20;

    public static final int STAGE_BABY = 0;
    public static final int STAGE_TEEN = 1;
    public static final int STAGE_YOUNG = 2;
    public static final int STAGE_ADULT = 3;

    private DragonPart[] dragonParts;
    private static final int PART_HEAD = 0;
    private static final int PART_BODY = 1;
    private static final int PART_NECK = 2;
    private static final int PART_TAIL = 3;
    private static final int PART_LEFT_WING = 4;
    private static final int PART_RIGHT_WING = 5;
    private static final int PART_LEFT_LEG_FRONT = 6;
    private static final int PART_RIGHT_LEG_FRONT = 7;
    private static final int PART_LEFT_LEG_BACK = 8;
    private static final int PART_RIGHT_LEG_BACK = 9;

    private static final float[] DAMAGE_MULTIPLIERS = {1.5f, 0.8f, 1.0f, 0.7f, 1.2f, 1.2f, 1.0f, 1.0f, 1.0f, 1.0f};

    public static class DragonPart extends PartEntity<IceDragonEntity> {
        private final String partName;
        private final EntityDimensions dimensions;
        private final float damageMultiplier;

        public DragonPart(IceDragonEntity parent, String partName, float width, float height, float damageMultiplier) {
            super(parent);
            this.partName = partName;
            this.dimensions = EntityDimensions.scalable(width, height);
            this.damageMultiplier = damageMultiplier;
            this.refreshDimensions();
        }

        @Override
        public boolean isPickable() { return true; }

        @Override
        protected void readAdditionalSaveData(CompoundTag pCompound) {}

        @Override
        protected void addAdditionalSaveData(CompoundTag pCompound) {}

        @Override
        protected void defineSynchedData() {}

        @Override
        public boolean hurt(DamageSource source, float amount) {
            return !this.isInvulnerableTo(source) && this.getParent().hurt(source, amount * this.damageMultiplier);
        }

        @Override
        public EntityDimensions getDimensions(Pose pose) {
            return this.dimensions;
        }

        @Override
        public boolean shouldBeSaved() { return false; }

        public String getPartName() { return partName; }
    }

    public IceDragonEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
        if (!pLevel.isClientSide()) {
            this.generateRandomName(this.random);
        }
        this.initDragonParts();
        this.refreshDimensions();
    }

    private void initDragonParts() {
        float[][] sizes = getPartSizesForStage(this.getGrowthStage());
        dragonParts = new DragonPart[10];
        dragonParts[PART_HEAD] = new DragonPart(this, "head", sizes[PART_HEAD][0], sizes[PART_HEAD][1], DAMAGE_MULTIPLIERS[PART_HEAD]);
        dragonParts[PART_BODY] = new DragonPart(this, "body", sizes[PART_BODY][0], sizes[PART_BODY][1], DAMAGE_MULTIPLIERS[PART_BODY]);
        dragonParts[PART_NECK] = new DragonPart(this, "neck", sizes[PART_NECK][0], sizes[PART_NECK][1], DAMAGE_MULTIPLIERS[PART_NECK]);
        dragonParts[PART_TAIL] = new DragonPart(this, "tail", sizes[PART_TAIL][0], sizes[PART_TAIL][1], DAMAGE_MULTIPLIERS[PART_TAIL]);
        dragonParts[PART_LEFT_WING] = new DragonPart(this, "left_wing", sizes[PART_LEFT_WING][0], sizes[PART_LEFT_WING][1], DAMAGE_MULTIPLIERS[PART_LEFT_WING]);
        dragonParts[PART_RIGHT_WING] = new DragonPart(this, "right_wing", sizes[PART_RIGHT_WING][0], sizes[PART_RIGHT_WING][1], DAMAGE_MULTIPLIERS[PART_RIGHT_WING]);
        dragonParts[PART_LEFT_LEG_FRONT] = new DragonPart(this, "left_leg_front", sizes[PART_LEFT_LEG_FRONT][0], sizes[PART_LEFT_LEG_FRONT][1], DAMAGE_MULTIPLIERS[PART_LEFT_LEG_FRONT]);
        dragonParts[PART_RIGHT_LEG_FRONT] = new DragonPart(this, "right_leg_front", sizes[PART_RIGHT_LEG_FRONT][0], sizes[PART_RIGHT_LEG_FRONT][1], DAMAGE_MULTIPLIERS[PART_RIGHT_LEG_FRONT]);
        dragonParts[PART_LEFT_LEG_BACK] = new DragonPart(this, "left_leg_back", sizes[PART_LEFT_LEG_BACK][0], sizes[PART_LEFT_LEG_BACK][1], DAMAGE_MULTIPLIERS[PART_LEFT_LEG_BACK]);
        dragonParts[PART_RIGHT_LEG_BACK] = new DragonPart(this, "right_leg_back", sizes[PART_RIGHT_LEG_BACK][0], sizes[PART_RIGHT_LEG_BACK][1], DAMAGE_MULTIPLIERS[PART_RIGHT_LEG_BACK]);
    }

    private float[][] getPartSizesForStage(int stage) {
        float[][] sizes = new float[10][2];
        float scale = this.isFemale() ? 1.1f : 1.0f;

        switch(stage) {
            case STAGE_BABY:
                sizes[PART_HEAD] = new float[]{0.6f, 0.6f};
                sizes[PART_BODY] = new float[]{0.8f, 1.2f};
                sizes[PART_NECK] = new float[]{0.4f, 0.8f};
                sizes[PART_TAIL] = new float[]{0.3f, 1.2f};
                sizes[PART_LEFT_WING] = new float[]{1.0f, 0.2f};
                sizes[PART_RIGHT_WING] = new float[]{1.0f, 0.2f};
                sizes[PART_LEFT_LEG_FRONT] = new float[]{0.3f, 0.5f};
                sizes[PART_RIGHT_LEG_FRONT] = new float[]{0.3f, 0.5f};
                sizes[PART_LEFT_LEG_BACK] = new float[]{0.3f, 0.5f};
                sizes[PART_RIGHT_LEG_BACK] = new float[]{0.3f, 0.5f};
                break;
            case STAGE_TEEN:
                sizes[PART_HEAD] = new float[]{2.0f, 2.0f};
                sizes[PART_BODY] = new float[]{3.0f, 4.0f};
                sizes[PART_NECK] = new float[]{1.5f, 3.0f};
                sizes[PART_TAIL] = new float[]{1.0f, 5.0f};
                sizes[PART_LEFT_WING] = new float[]{4.0f, 1.0f};
                sizes[PART_RIGHT_WING] = new float[]{4.0f, 1.0f};
                sizes[PART_LEFT_LEG_FRONT] = new float[]{1.0f, 2.0f};
                sizes[PART_RIGHT_LEG_FRONT] = new float[]{1.0f, 2.0f};
                sizes[PART_LEFT_LEG_BACK] = new float[]{1.0f, 2.0f};
                sizes[PART_RIGHT_LEG_BACK] = new float[]{1.0f, 2.0f};
                break;
            case STAGE_YOUNG:
                sizes[PART_HEAD] = new float[]{2.5f, 2.5f};
                sizes[PART_BODY] = new float[]{3.5f, 5.0f};
                sizes[PART_NECK] = new float[]{1.8f, 3.5f};
                sizes[PART_TAIL] = new float[]{1.3f, 6.0f};
                sizes[PART_LEFT_WING] = new float[]{5.0f, 1.2f};
                sizes[PART_RIGHT_WING] = new float[]{5.0f, 1.2f};
                sizes[PART_LEFT_LEG_FRONT] = new float[]{1.2f, 2.5f};
                sizes[PART_RIGHT_LEG_FRONT] = new float[]{1.2f, 2.5f};
                sizes[PART_LEFT_LEG_BACK] = new float[]{1.2f, 2.5f};
                sizes[PART_RIGHT_LEG_BACK] = new float[]{1.2f, 2.5f};
                break;
            default:
                sizes[PART_HEAD] = new float[]{3.0f * scale, 3.0f * scale};
                sizes[PART_BODY] = new float[]{4.0f * scale, 6.0f * scale};
                sizes[PART_NECK] = new float[]{2.0f * scale, 4.0f * scale};
                sizes[PART_TAIL] = new float[]{1.5f * scale, 7.0f * scale};
                sizes[PART_LEFT_WING] = new float[]{6.0f * scale, 1.5f * scale};
                sizes[PART_RIGHT_WING] = new float[]{6.0f * scale, 1.5f * scale};
                sizes[PART_LEFT_LEG_FRONT] = new float[]{1.5f * scale, 3.0f * scale};
                sizes[PART_RIGHT_LEG_FRONT] = new float[]{1.5f * scale, 3.0f * scale};
                sizes[PART_LEFT_LEG_BACK] = new float[]{1.5f * scale, 3.0f * scale};
                sizes[PART_RIGHT_LEG_BACK] = new float[]{1.5f * scale, 3.0f * scale};
                break;
        }
        return sizes;
    }

    @Override
    public boolean isMultipartEntity() { return true; }

    @Override
    public PartEntity<?>[] getParts() { return dragonParts; }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        int stage = this.getGrowthStage();
        float scale = this.isFemale() ? 1.1f : 1.0f;
        switch(stage) {
            case STAGE_BABY: return EntityDimensions.scalable(0.8f * scale, 1.2f * scale);
            case STAGE_TEEN: return EntityDimensions.scalable(3.0f * scale, 4.0f * scale);
            case STAGE_YOUNG: return EntityDimensions.scalable(3.5f * scale, 5.0f * scale);
            default: return EntityDimensions.scalable(4.0f * scale, 6.0f * scale);
        }
    }

    @Override
    protected AABB makeBoundingBox() {
        EntityDimensions dims = this.getDimensions(this.getPose());
        return new AABB(-dims.width/2, 0, -dims.width/2, dims.width/2, dims.height, dims.width/2).move(this.getX(), this.getY(), this.getZ());
    }

    private void updatePartPositions() {
        if (dragonParts == null || this.level().isClientSide) return;

        Vec3 rootPos = this.position();
        float yaw = this.getYRot();
        float pitch = this.getXRot();

        float scale = this.isFemale() ? 1.1f : 1.0f;
        float stageScale = 1.0f;
        switch(this.getGrowthStage()) {
            case STAGE_BABY: stageScale = 0.3f; break;
            case STAGE_TEEN: stageScale = 0.7f; break;
            case STAGE_YOUNG: stageScale = 0.9f; break;
            default: stageScale = 1.0f;
        }
        scale *= stageScale;

        double headX = rootPos.x + Math.sin(Math.toRadians(yaw)) * 1.5 * scale;
        double headZ = rootPos.z - Math.cos(Math.toRadians(yaw)) * 1.5 * scale;
        double headY = rootPos.y + 1.0 * scale;
        dragonParts[PART_HEAD].setPos(headX, headY, headZ);
        dragonParts[PART_HEAD].setYRot(yaw);

        double neckX = rootPos.x + Math.sin(Math.toRadians(yaw)) * 1.0 * scale;
        double neckZ = rootPos.z - Math.cos(Math.toRadians(yaw)) * 1.0 * scale;
        double neckY = rootPos.y + 0.8 * scale;
        dragonParts[PART_NECK].setPos(neckX, neckY, neckZ);
        dragonParts[PART_NECK].setYRot(yaw);

        dragonParts[PART_BODY].setPos(rootPos.x, rootPos.y + 0.5 * scale, rootPos.z);
        dragonParts[PART_BODY].setYRot(yaw);

        double tailX = rootPos.x - Math.sin(Math.toRadians(yaw)) * 2.0 * scale;
        double tailZ = rootPos.z + Math.cos(Math.toRadians(yaw)) * 2.0 * scale;
        dragonParts[PART_TAIL].setPos(tailX, rootPos.y + 0.3 * scale, tailZ);
        dragonParts[PART_TAIL].setYRot(yaw + 180);

        double leftWingX = rootPos.x + Math.cos(Math.toRadians(yaw)) * 1.5 * scale;
        double leftWingZ = rootPos.z + Math.sin(Math.toRadians(yaw)) * 1.5 * scale;
        dragonParts[PART_LEFT_WING].setPos(leftWingX, rootPos.y + 0.8 * scale, leftWingZ);
        dragonParts[PART_LEFT_WING].setYRot(yaw - 90);

        double rightWingX = rootPos.x - Math.cos(Math.toRadians(yaw)) * 1.5 * scale;
        double rightWingZ = rootPos.z - Math.sin(Math.toRadians(yaw)) * 1.5 * scale;
        dragonParts[PART_RIGHT_WING].setPos(rightWingX, rootPos.y + 0.8 * scale, rightWingZ);
        dragonParts[PART_RIGHT_WING].setYRot(yaw + 90);

        double leftFrontLegX = rootPos.x + Math.cos(Math.toRadians(yaw)) * 0.8 * scale;
        double leftFrontLegZ = rootPos.z + Math.sin(Math.toRadians(yaw)) * 0.8 * scale;
        dragonParts[PART_LEFT_LEG_FRONT].setPos(leftFrontLegX, rootPos.y, leftFrontLegZ);
        dragonParts[PART_LEFT_LEG_FRONT].setYRot(yaw);

        double rightFrontLegX = rootPos.x - Math.cos(Math.toRadians(yaw)) * 0.8 * scale;
        double rightFrontLegZ = rootPos.z - Math.sin(Math.toRadians(yaw)) * 0.8 * scale;
        dragonParts[PART_RIGHT_LEG_FRONT].setPos(rightFrontLegX, rootPos.y, rightFrontLegZ);
        dragonParts[PART_RIGHT_LEG_FRONT].setYRot(yaw);

        double leftBackLegX = rootPos.x + Math.cos(Math.toRadians(yaw)) * 1.2 * scale;
        double leftBackLegZ = rootPos.z + Math.sin(Math.toRadians(yaw)) * 1.2 * scale;
        dragonParts[PART_LEFT_LEG_BACK].setPos(leftBackLegX, rootPos.y, leftBackLegZ);
        dragonParts[PART_LEFT_LEG_BACK].setYRot(yaw);

        double rightBackLegX = rootPos.x - Math.cos(Math.toRadians(yaw)) * 1.2 * scale;
        double rightBackLegZ = rootPos.z - Math.sin(Math.toRadians(yaw)) * 1.2 * scale;
        dragonParts[PART_RIGHT_LEG_BACK].setPos(rightBackLegX, rootPos.y, rightBackLegZ);
        dragonParts[PART_RIGHT_LEG_BACK].setYRot(yaw);
    }

    @Override
    public void setRemainingFireTicks(int ticks) {
        super.setRemainingFireTicks((int)(ticks * 1.2f));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_FEMALE.equals(key) || DATA_GROWTH_STAGE.equals(key)) {
            this.refreshDimensions();
            this.initDragonParts();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof Player player && !this.level().isClientSide()) {
            if (this.random.nextInt(3) == 0) {
                player.sendSystemMessage(Component.literal("🐉 " + this.getDragonName() + " exclaims: " + getRandomHurtMessage()));
            }
        }
        if (source.is(DamageTypeTags.IS_FIRE)) amount *= 1.5f;
        if (source.getDirectEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();
            if (weapon.isEnchanted()) {
                if (weapon.getEnchantmentLevel(Enchantments.FIRE_ASPECT) > 0) amount *= 1.3f;
                if (weapon.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) amount *= 1.3f;
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new IceDragonGoal.FlyWanderGoal(this));
        this.goalSelector.addGoal(4, new IceDragonGoal.TakeOffGoal(this));
        this.goalSelector.addGoal(3, new IceDragonGoal.LandGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, 0);
        this.entityData.define(DATA_EYE_VARIANT, 0);
        this.entityData.define(DATA_FLYING, false);
        this.entityData.define(DATA_TAKEOFF, false);
        this.entityData.define(DATA_LANDING, false);
        this.entityData.define(DATA_FEMALE, false);
        this.entityData.define(DATA_AGE, 0);
        this.entityData.define(DATA_GROWTH_STAGE, STAGE_BABY);
        this.entityData.define(DATA_ATTACKING, false);
        this.entityData.define(DATA_NAME, "");
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        nav.setCanPassDoors(true);
        return nav;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 600.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FLYING_SPEED, 0.8D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 10.0D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "MainController", 5, event -> {
            if (this.isAttacking()) return event.setAndContinue(ATTACK_ANIMATION);
            if (this.isLanding()) return event.setAndContinue(LAND_ANIMATION);
            if (this.getGrowthStage() == STAGE_BABY) return event.setAndContinue(event.isMoving() ? WALK_ANIMATION : IDLE_ANIMATION);
            if (this.isTakingOff()) return event.setAndContinue(FLIGHT_START_ANIMATION);
            if (this.isFlying()) {
                Vec3 movement = this.getDeltaMovement();
                return event.setAndContinue(movement.horizontalDistance() > 0.1 && Math.abs(movement.y) < 0.3 ? FLIGHT_ANIMATION : FLIGHT_IDLE_ANIMATION);
            }
            return event.setAndContinue(event.isMoving() ? WALK_ANIMATION : IDLE_ANIMATION);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.cache; }

    @Override
    public boolean onGround() { return !this.isFlying() && super.onGround(); }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isTakingOff() || this.isLanding() || this.getGrowthStage() == STAGE_BABY) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            return;
        }
        if (this.isFlying()) {
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D));
        } else {
            super.travel(travelVector);
        }
    }

    public void setFlying(boolean flying) { this.entityData.set(DATA_FLYING, flying); }
    public boolean isTakingOff() { return this.entityData.get(DATA_TAKEOFF); }
    public void setTakingOff(boolean takingOff) {
        this.entityData.set(DATA_TAKEOFF, takingOff);
        if (takingOff) this.takeoffAnimationTimer = 40;
    }
    public boolean isLanding() { return this.entityData.get(DATA_LANDING); }
    public void setLanding(boolean landing) {
        this.entityData.set(DATA_LANDING, landing);
        if (landing) this.landingAnimationTimer = 40;
    }
    public int getVariant() { return this.entityData.get(DATA_VARIANT); }
    public void setVariant(int variant) { this.entityData.set(DATA_VARIANT, variant); }
    public boolean isFemale() { return this.entityData.get(DATA_FEMALE); }
    public void setFemale(boolean female) {
        this.entityData.set(DATA_FEMALE, female);
        this.refreshDimensions();
        this.initDragonParts();
    }
    public int getAge() { return this.entityData.get(DATA_AGE); }
    public void setAge(int age) {
        this.entityData.set(DATA_AGE, age);
        this.updateGrowthStage();
    }
    public int getGrowthStage() { return this.entityData.get(DATA_GROWTH_STAGE); }
    private void setGrowthStage(int stage) {
        this.entityData.set(DATA_GROWTH_STAGE, stage);
        this.refreshDimensions();
        this.initDragonParts();
    }
    public boolean isAttacking() { return this.entityData.get(DATA_ATTACKING); }
    public void setAttacking(boolean attacking) { this.entityData.set(DATA_ATTACKING, attacking); }
    public void setAttackTimer(int timer) {
        this.attackTimer = timer;
        this.setAttacking(timer > 0);
    }
    public boolean isFlying() { return this.entityData.get(DATA_FLYING); }
    public int getEyeVariant() { return this.entityData.get(DATA_EYE_VARIANT); }
    public void setEyeVariant(int variant) { this.entityData.set(DATA_EYE_VARIANT, variant); }

    private void updateGrowthStage() {
        int age = this.getAge();
        if (age < GROWTH_INTERVAL) this.setGrowthStage(STAGE_BABY);
        else if (age < GROWTH_INTERVAL * 2) this.setGrowthStage(STAGE_TEEN);
        else if (age < GROWTH_INTERVAL * 3) this.setGrowthStage(STAGE_YOUNG);
        else this.setGrowthStage(STAGE_ADULT);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("EyeVariant", this.getEyeVariant());
        compound.putBoolean("Flying", this.isFlying());
        compound.putBoolean("Female", this.isFemale());
        compound.putInt("Age", this.getAge());
        compound.putInt("FurDropTimer", this.furDropTimer);
        compound.putString("DragonName", this.getDragonName());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Variant")) this.setVariant(compound.getInt("Variant"));
        if (compound.contains("EyeVariant")) this.setEyeVariant(compound.getInt("EyeVariant"));
        if (compound.contains("Flying")) this.setFlying(compound.getBoolean("Flying"));
        if (compound.contains("Female")) this.setFemale(compound.getBoolean("Female"));
        if (compound.contains("Age")) this.setAge(compound.getInt("Age"));
        if (compound.contains("FurDropTimer")) this.furDropTimer = compound.getInt("FurDropTimer");
        if (compound.contains("DragonName")) this.setDragonName(compound.getString("DragonName"));
        else this.generateRandomName(this.random);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.setVariant(level.getRandom().nextInt(3));
        this.setEyeVariant(level.getRandom().nextInt(5));
        this.setFemale(level.getRandom().nextBoolean());

        int randomStage = level.getRandom().nextInt(4);
        int age = randomStage * GROWTH_INTERVAL;
        this.setAge(age);

        this.generateRandomName(level.getRandom());
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    private void generateRandomName(RandomSource random) {
        String[] names = {"Fafnir", "Jormungand", "Fenrir", "Nidhogg", "Grendel", "Hraesvelgr",
                "Heidrun", "Ratatosk", "Gullinbursti", "Hrimfaxi", "Hel", "Skadi",
                "Freya", "Sigunn", "Brynhild", "Gudrun", "Astrid", "Ingrid", "Sigrid", "Ragnhild"};
        this.setDragonName(names[random.nextInt(names.length)]);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) { return null; }
    @Override
    public boolean canBreed() { return false; }
    @Override
    public boolean isFood(ItemStack stack) { return stack.getItem() == Items.COD || stack.getItem() == Items.SALMON; }

    @Override
    public void tick() {
        super.tick();
        this.updatePartPositions();

        if (!this.level().isClientSide()) {
            if (this.tickCount % 400 == 0 && this.random.nextInt(3) == 0) this.playIdleSound();

            if (this.getGrowthStage() >= STAGE_TEEN) {
                this.furDropTimer++;
                if (this.furDropTimer >= FUR_DROP_INTERVAL) {
                    this.spawnAtLocation(ModItems.FUR_ICE_DRAGON.get(), 1);
                    this.furDropTimer = 0;
                }
            }

            if (this.takeoffAnimationTimer > 0) {
                this.takeoffAnimationTimer--;
                if (this.takeoffAnimationTimer <= 0 && this.isTakingOff()) {
                    this.setTakingOff(false);
                    this.setFlying(true);
                }
            }

            if (this.landingAnimationTimer > 0) {
                this.landingAnimationTimer--;
                if (this.landingAnimationTimer <= 0 && this.isLanding()) {
                    this.setLanding(false);
                    this.setFlying(false);
                }
            }

            if (this.getGrowthStage() == STAGE_BABY && (this.isFlying() || this.isTakingOff() || this.isLanding())) {
                this.setFlying(false);
                this.setTakingOff(false);
                this.setLanding(false);
            }

            if (this.tickCount % 20 == 0 && this.getGrowthStage() < STAGE_ADULT) {
                this.setAge(this.getAge() + 1);
            }

            if (this.attackTimer > 0) {
                this.attackTimer--;
                if (this.attackTimer <= 0) this.setAttacking(false);
            }
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) { return distance < 1048576.0; }
    public boolean isAlwaysRenderNameTag() { return true; }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int lootingLevel, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, lootingLevel, recentlyHit);
        this.spawnAtLocation(ModItems.FUR_ICE_DRAGON.get(), 4 + this.random.nextInt(12));
        this.spawnAtLocation(ModItems.HEART_ICE_DRAGON.get());
    }

    @Override
    protected void dropExperience() {
        if (this.level() instanceof ServerLevel serverLevel) {
            ExperienceOrb.award(serverLevel, this.position(), 250 + this.random.nextInt(30));
        }
    }

    private String getRandomFeedingMessage() {
        String[] messages = {"Thank you for the fish!", "Mmm, fresh fish!", "You're brave to approach so close!",
                "This fish reminds me of the northern seas...", "Unexpected generosity!",
                "How nice not to have to go looking for food..."};
        return messages[this.random.nextInt(messages.length)];
    }

    private String getRandomHurtMessage() {
        String[] messages = {"In a thousand years, no one has treated me like this!", "My years have taught me patience!",
                "Even glaciers remember fewer grievances!", "At my age, such treatment is offensive!",
                "I've seen civilizations, but this is new!", "My longevity is no reason for rudeness!"};
        return messages[this.random.nextInt(messages.length)];
    }

    private void spawnHearts() {
        if (this.level().isClientSide()) {
            for (int i = 0; i < 5; i++) {
                this.level().addParticle(ParticleTypes.HEART,
                        this.getX() + (this.random.nextDouble() - 0.5) * 2.0,
                        this.getY() + 1.0 + this.random.nextDouble() * 2.0,
                        this.getZ() + (this.random.nextDouble() - 0.5) * 2.0, 0, 0.1, 0);
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.isFood(stack)) {
            if (!this.level().isClientSide()) {
                if (this.random.nextInt(2) == 0) {
                    player.sendSystemMessage(Component.literal("🐉 " + this.getDragonName() + " says: " + getRandomFeedingMessage()));
                }
                this.spawnHearts();
                if (!player.getAbilities().instabuild) stack.shrink(1);
                this.usePlayerItem(player, hand, stack);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return super.mobInteract(player, hand);
    }

    public String getDragonName() { return this.entityData.get(DATA_NAME); }
    public void setDragonName(String name) {
        this.entityData.set(DATA_NAME, name);
        this.setCustomName(Component.literal(name));
        this.setCustomNameVisible(true);
    }

    public void playIdleSound() {
        if (!this.level().isClientSide()) {
            SoundEvent[] sounds = {ModSounds.DRAGON_IDLE1.get(), ModSounds.DRAGON_IDLE2.get(), ModSounds.DRAGON_IDLE3.get()};
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    sounds[this.random.nextInt(sounds.length)], this.getSoundSource(), 1.0F, 1.0F);
        }
    }

    public void playAggressiveSound() {
        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    ModSounds.DRAGON_AGGRESSIVE_ROAR.get(), this.getSoundSource(), 1.5F, 1.0F);
        }
    }
}