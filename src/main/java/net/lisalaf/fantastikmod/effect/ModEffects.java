package net.lisalaf.fantastikmod.effect;

import net.lisalaf.fantastikmod.fantastikmod;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, fantastikmod.MOD_ID);

    public static final RegistryObject<MobEffect> INARI_BLESSING = MOB_EFFECTS.register("inari_blessing",
            InariBlessingEffect::new);
    public static final RegistryObject<MobEffect> CATNIP_EFFECT = MOB_EFFECTS.register("catnip_effect",
            () -> new CatnipEffect());

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}