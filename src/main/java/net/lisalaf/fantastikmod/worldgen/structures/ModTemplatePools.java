package net.lisalaf.fantastikmod.worldgen.structures;

import com.mojang.datafixers.util.Pair;
import net.lisalaf.fantastikmod.fantastikmod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

@SuppressWarnings("removal")
public class ModTemplatePools {
    public static final ResourceKey<StructureTemplatePool> TEA_HOUSE_START = ResourceKey.create(
            Registries.TEMPLATE_POOL,
            new ResourceLocation(fantastikmod.MOD_ID, "tea_house/start")
    );

    public static final ResourceKey<StructureTemplatePool> MOON_RUIN_HOUSE_START = ResourceKey.create(
            Registries.TEMPLATE_POOL,
            new ResourceLocation(fantastikmod.MOD_ID, "moon_ruin_house/start")
    );

    public static final ResourceKey<StructureTemplatePool> TOWN_CENTER_START = ResourceKey.create(
            Registries.TEMPLATE_POOL,
            new ResourceLocation(fantastikmod.MOD_ID, "town_center/start")
    );

    public static final ResourceKey<StructureTemplatePool> MOON_RUIN_CHURCH_START = ResourceKey.create(
            Registries.TEMPLATE_POOL,
            new ResourceLocation(fantastikmod.MOD_ID, "moon_ruin_church/start")
    );

    public static final ResourceKey<StructureTemplatePool> INARI_SMALL_SHRINE_START = ResourceKey.create(
            Registries.TEMPLATE_POOL,
            new ResourceLocation(fantastikmod.MOD_ID, "inari_small_shrine/start")
    );

    public static void bootstrap(BootstapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> templatePoolRegistry = context.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<StructureProcessorList> processorListRegistry = context.lookup(Registries.PROCESSOR_LIST);

        Holder<StructureTemplatePool> emptyPool = templatePoolRegistry.getOrThrow(
                ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation("empty"))
        );

        context.register(TEA_HOUSE_START, new StructureTemplatePool(
                emptyPool,
                List.of(
                        Pair.of(
                                SinglePoolElement.single(
                                        fantastikmod.MOD_ID + ":tea_house",
                                        processorListRegistry.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST,new ResourceLocation("empty")))
                                ),
                                1
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(MOON_RUIN_HOUSE_START, new StructureTemplatePool(
                emptyPool,
                List.of(
                        Pair.of(
                                SinglePoolElement.single(
                                        fantastikmod.MOD_ID + ":moon_ruin_house",
                                        processorListRegistry.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("empty")))
                                ),
                                1
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(TOWN_CENTER_START, new StructureTemplatePool(
                emptyPool,
                List.of(
                        Pair.of(
                                SinglePoolElement.single(
                                        fantastikmod.MOD_ID + ":town_center",
                                        processorListRegistry.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("empty")))
                                ),
                                1
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(MOON_RUIN_CHURCH_START, new StructureTemplatePool(
                emptyPool,
                List.of(
                        Pair.of(
                                SinglePoolElement.single(
                                        fantastikmod.MOD_ID + ":moon_ruin_church",
                                        processorListRegistry.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("empty")))
                                ),
                                1
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));

        context.register(INARI_SMALL_SHRINE_START, new StructureTemplatePool(
                emptyPool,
                List.of(
                        Pair.of(
                                SinglePoolElement.single(
                                        fantastikmod.MOD_ID + ":inari_small_shrine",
                                        processorListRegistry.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("empty")))
                                ),
                                1
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));
    }
}