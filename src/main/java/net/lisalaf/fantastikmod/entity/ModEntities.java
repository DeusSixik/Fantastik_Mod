package net.lisalaf.fantastikmod.entity;


import net.lisalaf.fantastikmod.entity.custom.*;
import net.lisalaf.fantastikmod.fantastikmod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, fantastikmod.MOD_ID);

    public static final RegistryObject<EntityType<KitsuneLightEntity>> KITSUNE_LIGHT =
            ENTITY_TYPES.register("kitsune_light", () -> EntityType.Builder.of(KitsuneLightEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 1.4f).build("kitsune_light"));

    public static final RegistryObject<EntityType<BakenekoEntity>> BAKENEKO =
            ENTITY_TYPES.register("bakeneko", () -> EntityType.Builder.of(BakenekoEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 1.4f).build("bakeneko"));

    public static final RegistryObject<EntityType<IceDragonEntity>> ICE_DRAGON =
            ENTITY_TYPES.register("ice_dragon", () -> EntityType.Builder.of(IceDragonEntity::new, MobCategory.CREATURE)
                    .sized(6.0f, 12.0f)
                    .clientTrackingRange(32)
                    .build("ice_dragon"));

    public static final RegistryObject<EntityType<MoonDeerEntity>> MOON_DEER =
            ENTITY_TYPES.register("moon_deer", () -> EntityType.Builder.of(MoonDeerEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 1.7f).build("moon_deer"));

    public static final RegistryObject<EntityType<BlueButterflyEntity>> BLUE_BUTTERFLY =
            ENTITY_TYPES.register("blue_butterfly", () -> EntityType.Builder.of(BlueButterflyEntity::new, MobCategory.CREATURE)
                    .sized(0.2f, 0.2f)
                    .clientTrackingRange(10)
                    .build("blue_butterfly"));



    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
