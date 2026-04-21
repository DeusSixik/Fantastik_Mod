package net.lisalaf.fantastikmod.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.lisalaf.fantastikmod.item.ModArmorMaterials;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("removal")
public class ModArmorItem extends ArmorItem {

    // ID модификаторов Аттрибутов
    private static final UUID HEALTH_HELMET_UUID = UUID.fromString("4d068fb2-dda5-4545-8df3-e1fa1a7a5c9c");
    private static final UUID HEALTH_CHEST_UUID  = UUID.fromString("db889cd8-752b-4c11-9b2d-6f440e43e9fc");
    private static final UUID HEALTH_LEGS_UUID   = UUID.fromString("5e82f7e5-f6e2-4e9a-bacd-d3a0549367cd");
    private static final UUID HEALTH_BOOTS_UUID  = UUID.fromString("e3dc180f-a536-49c0-a750-15f4b155da7d");

    private static final Map<ArmorMaterial, List<MobEffectInstance>> MATERIAL_TO_EFFECTS_MAP = Map.of(
            ModArmorMaterials.FUR_ICE_DRAGON, List.of(
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 0, false, false, true),
                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80, 0, false, false, true)
            ),
            ModArmorMaterials.GEM_MOON, List.of(
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 1, false, false, true),
                    new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 1, false, false, true)
            )
    );

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        final Multimap<Attribute, AttributeModifier> attributes = super.getDefaultAttributeModifiers(equipmentSlot);

        /*
             Проверяем материал и слот
         */
        if (this.getMaterial() == ModArmorMaterials.AURIPIGMENT && equipmentSlot == this.getType().getSlot()) {
            final ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(attributes);

            final double healthBonus;
            final UUID uuid;

            /*
                Выдаем уникальный UUID и количество сердец для каждой части
                Для соблюдения оригинальной логики мы выдаём по 0.5 сердца чтобы в суме было 2 сердца за каждый сет брони
             */
            switch (this.getType()) {
                case HELMET -> {
                    healthBonus = 1;
                    uuid = HEALTH_HELMET_UUID;
                }
                case CHESTPLATE -> {
                    healthBonus = 1;
                    uuid = HEALTH_CHEST_UUID;
                }
                case LEGGINGS -> {
                    healthBonus = 1;
                    uuid = HEALTH_LEGS_UUID;
                }
                case BOOTS -> {
                    healthBonus = 1;
                    uuid = HEALTH_BOOTS_UUID;
                }
                default -> {
                    uuid = null;
                    healthBonus = 0;
                }
            }

            /*
                Добавляем атрибут, если UUID был назначен
             */
            if (uuid != null) {
                builder.put(Attributes.MAX_HEALTH, new AttributeModifier(
                        uuid,
                        "Armor health boost",
                        healthBonus,
                        AttributeModifier.Operation.ADDITION
                ));
            }

            return builder.build();
        }

        return attributes;
    }

    public ModArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (world.isClientSide()) {
            return;
        }

        /*
            ПРОВЕРКА СЕТА
         */
        if (hasFullSuitOfArmorOn(player)) {
            for (Map.Entry<ArmorMaterial, List<MobEffectInstance>> entry : MATERIAL_TO_EFFECTS_MAP.entrySet()) {
                final ArmorMaterial material = entry.getKey();

                if (hasCorrectArmorOn(material, player)) {
                    final List<MobEffectInstance> effects = entry.getValue();

                    if (material == ModArmorMaterials.GEM_MOON) {
                        if (isNightTime(world))
                            applyEffects(player, effects);
                    }
                    else if (material == ModArmorMaterials.FUR_ICE_DRAGON) {
                        applyEffects(player, effects);

                        /*
                             Чтобы лед не создавался 4 раза за тик (от шлема, груди, штанов и ботинок),
                             вызываем этот код только когда тик идет от ботинок
                         */
                        if (stack == player.getInventory().getArmor(0)) {
                            applyIceWalkEffectFast(player, world);
                        }
                    }
                    else {
                        applyEffects(player, effects);
                    }
                }
            }
        }

        /*
            БРОНЯ ИЗ СЕРЕБРА
            Ищим нежить раз в 2 секунду
         */
        if (this.getMaterial() == ModArmorMaterials.SILVER && player.tickCount % 10 == 0) {
            handleSilverEffect(world, player);
        }
    }

    private void handleSilverEffect(Level world, Player player) {
        int silverPieces = 0;
        final NonNullList<ItemStack> itemStacks = player.getInventory().armor;
        for (int i = 0; i < itemStacks.size(); i++) {
            if (itemStacks.get(i).getItem() instanceof ModArmorItem modArmor && modArmor.getMaterial() == ModArmorMaterials.SILVER) {
                silverPieces++;
            }
        }

        if (silverPieces > 0) {
            final double radius = silverPieces;

            /*
                Ищем существ вокруг
             */
            final AABB searchBox = player.getBoundingBox().inflate(radius);
            final List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, searchBox, this::isUndeadOrMonster);

            for (int i = 0; i < entities.size(); i++) {
                final LivingEntity entity = entities.get(i);
                if (entity instanceof PathfinderMob pathfinderMob) {
                    pathfinderMob.getNavigation().stop();

                    /*
                        Быстрое вычисление вектора отступления
                     */
                    double dx = entity.getX() - player.getX();
                    double dz = entity.getZ() - player.getZ();

                    /*
                         Если они стоят в одной точке, задаем небольшое случайное направление
                     */
                    if (dx == 0 && dz == 0) {
                        dx = world.random.nextDouble() - 0.5;
                        dz = world.random.nextDouble() - 0.5;
                    }

                    /*
                        Просто сдвигаем сущность в обратную сторону от игрока
                     */
                    final BlockPos fleePos = BlockPos.containing(entity.getX() + dx * 2, entity.getY(), entity.getZ() + dz * 2);
                    pathfinderMob.getNavigation().moveTo(fleePos.getX(), fleePos.getY(), fleePos.getZ(), 1.2);
                }

                /*
                    Использование distanceToSqr быстрее, чем distanceTo
                 */
                if (entity.distanceToSqr(player) < 1.0) {
                    entity.hurt(player.damageSources().magic(), silverPieces * 0.5F);
                }
            }
        }
    }

    private void applyEffects(Player player, List<MobEffectInstance> effects) {
        for (int i = 0; i < effects.size(); i++) {
            final MobEffectInstance effect = effects.get(i);
            /*
                Проверяем, висит ли уже этот эффект на игроке
             */
            final MobEffectInstance currentEffect = player.getEffect(effect.getEffect());
            if (currentEffect == null || currentEffect.getDuration() <= 300) {
                player.addEffect(new MobEffectInstance(
                        effect.getEffect(),
                        effect.getDuration(),
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.isVisible(),
                        effect.showIcon()
                ));
            }
        }
    }

    private boolean isNightTime(Level world) {
        long time = world.getDayTime() % 24000;
        return time >= 13000 && time <= 23000;
    }

    private boolean isUndeadOrMonster(LivingEntity entity) {
        return entity instanceof Monster || entity.getMobType() == MobType.UNDEAD;
    }

    private boolean hasFullSuitOfArmorOn(Player player) {
        final NonNullList<ItemStack> armor = player.getInventory().armor;
        for (int i = 0; i < armor.size(); i++) {
            if (armor.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCorrectArmorOn(ArmorMaterial material, Player player) {
        final NonNullList<ItemStack> armor = player.getInventory().armor;
        for (int i = 0; i < armor.size(); i++) {
            if (!(armor.get(i).getItem() instanceof ArmorItem armorItem) || armorItem.getMaterial() != material) {
                return false;
            }
        }
        return true;
    }

    private void applyIceWalkEffectFast(Player player, Level level) {
        if (!player.onGround()) return;

        final int minX = Mth.floor(player.getX() - 0.3);
        final int maxX = Mth.floor(player.getX() + 0.3);
        final int minZ = Mth.floor(player.getZ() - 0.3);
        final int maxZ = Mth.floor(player.getZ() + 0.3);

        final int py = player.getBlockY() - 1;

        final BlockState frostedIce = Blocks.FROSTED_ICE.defaultBlockState();
        final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        final BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                mutablePos.set(x, py, z);
                abovePos.set(x, py + 1, z);

                final BlockState state = level.getBlockState(mutablePos);
                if (state.is(Blocks.WATER) && state.getFluidState().isSource() && level.isEmptyBlock(abovePos)) {
                    level.setBlockAndUpdate(mutablePos, frostedIce);
                    level.scheduleTick(mutablePos, Blocks.FROSTED_ICE, level.random.nextInt(40) + 40);
                }
            }
        }
    }
}
