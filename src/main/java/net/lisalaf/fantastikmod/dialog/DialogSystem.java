package net.lisalaf.fantastikmod.dialog;

import net.lisalaf.fantastikmod.dialog.mobs.KitsuneDialog;
import net.lisalaf.fantastikmod.dialog.mobs.MoonDeerDialog;
import net.lisalaf.fantastikmod.dialog.mobs.WildKitsuneDialog;
import net.lisalaf.fantastikmod.dialog.mobs.WildMoonDeerDialog;
import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.lisalaf.fantastikmod.entity.custom.KitsuneLightEntity;
import net.lisalaf.fantastikmod.entity.custom.MoonDeerEntity;
import net.lisalaf.fantastikmod.dialog.mobs.BakenekoDialog;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DialogSystem {
    private static final Map<Class<? extends Entity>, Function<Entity, Dialog>> DIALOG_REGISTRY = new HashMap<>();
    private static final Map<Class<? extends Entity>, BiFunction<Entity, Player, Dialog>> DIALOG_WITH_PLAYER_REGISTRY = new HashMap<>();

    private static Player currentPlayer;

    public static void registerDialogs() {
        registerDialog(MoonDeerEntity.class, entity -> {
            if (entity instanceof MoonDeerEntity deer) {
                return deer.isTamed() ? new MoonDeerDialog() : new WildMoonDeerDialog();
            }
            return new WildMoonDeerDialog();
        });

        registerDialog(KitsuneLightEntity.class, entity -> {
            if (entity instanceof KitsuneLightEntity kitsune) {
                return kitsune.isTamed() ? new KitsuneDialog() : new WildKitsuneDialog();
            }
            return new WildKitsuneDialog();
        });


    }

    public static void registerDialog(Class<? extends Entity> entityClass, Function<Entity, Dialog> dialogFunction) {
        DIALOG_REGISTRY.put(entityClass, dialogFunction);
    }

    public static void registerDialogWithPlayer(Class<? extends Entity> entityClass, BiFunction<Entity, Player, Dialog> dialogFunction) {
        DIALOG_WITH_PLAYER_REGISTRY.put(entityClass, dialogFunction);
    }

    public static boolean hasDialog(Entity entity) {
        return DIALOG_REGISTRY.containsKey(entity.getClass()) || DIALOG_WITH_PLAYER_REGISTRY.containsKey(entity.getClass());
    }

    public static Dialog getDialog(Entity entity) {
        return getDialog(entity, null);
    }

    public static Dialog getDialog(Entity entity, Player player) {
        BiFunction<Entity, Player, Dialog> withPlayerFunc = DIALOG_WITH_PLAYER_REGISTRY.get(entity.getClass());
        if (withPlayerFunc != null && player != null) {
            return withPlayerFunc.apply(entity, player);
        }

        Function<Entity, Dialog> function = DIALOG_REGISTRY.get(entity.getClass());
        return function != null ? function.apply(entity) : null;
    }

    public static void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
}