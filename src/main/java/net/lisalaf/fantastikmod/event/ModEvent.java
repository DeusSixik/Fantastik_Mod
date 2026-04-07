package net.lisalaf.fantastikmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.lisalaf.fantastikmod.block.ModBlocks;
import net.lisalaf.fantastikmod.effect.ModEffects;
import net.lisalaf.fantastikmod.entity.custom.BakenekoEntity;
import net.lisalaf.fantastikmod.fantastikmod;
import net.lisalaf.fantastikmod.item.BookHelper;
import net.lisalaf.fantastikmod.item.ModItems;
import net.lisalaf.fantastikmod.villager.ModVillagers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.player.Player;

import java.util.List;

@Mod.EventBusSubscriber(modid = fantastikmod.MOD_ID)
public class ModEvent {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event){
        if(event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> tredes = event.getTrades();

            // Level 1
            tredes.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.STRAWBERRY.get(), 8),
                    10, 4, 0.02f));

            tredes.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.RICE.get(), 8),
                    10, 3, 0.02f));
            tredes.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.TEA_SEEDS.get(), 2),
                    10, 4, 0.02f));

            tredes.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.STRAWBERRY_SEEDS.get(), 4),
                    10, 3, 0.02f));

        }

        if (event.getType() == ModVillagers.TEA_MASTER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> tredes = event.getTrades();
            // Level 1
            tredes.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.TEA_LEAFS_GREEN.get(), 8),
                    5, 8, 0.02f));
            tredes.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.TEA_LEAFS_BLACK.get(), 8),
                    5, 8, 0.02f));
            tredes.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(ModItems.TEA_LEAFS_GREEN.get(), 3),
                    10, 2, 0.02f));
            tredes.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(ModItems.TEA_LEAFS_BLACK.get(), 3),
                    10, 2, 0.02f));
            tredes.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(ModItems.TEA_SEEDS.get(), 3),
                    10, 5, 0.02f));
            tredes.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.TEA_LEAF.get(), 7),
                    new ItemStack(Items.EMERALD,1),
                    10, 7, 0.02f));
            tredes.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.TEA_LEAFS_BLACK.get(), 5),
                    new ItemStack(Items.EMERALD,1),
                    10, 8, 0.02f));
            tredes.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.TEA_LEAFS_GREEN.get(), 5),
                    new ItemStack(Items.EMERALD,1),
                    10, 8, 0.02f));

            // Level 2
            tredes.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.MATCHA_POWDER.get(), 4),
                    5, 10, 0.02f));
            tredes.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.MATCHA_POWDER.get(), 3),
                    new ItemStack(Items.EMERALD,1),
                    6, 10, 0.02f));
            tredes.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(ModItems.DOUGH_MOCHI.get(), 3),
                    5, 10, 0.02f));

            // Level 3
            tredes.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(ModItems.STRAWBERRY_MOCHI.get(), 2),
                    7, 12, 0.02f));
            tredes.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(ModItems.MOCHI.get(), 3),
                    7, 12, 0.02f));
            tredes.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(ModItems.MATCHA_MOCHI.get(), 2),
                    7, 12, 0.02f));
            tredes.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.MATCHA_TEA.get(), 1),
                    5, 12, 0.03f));
            tredes.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.GREEN_TEA.get(), 1),
                    5, 12, 0.03f));
            tredes.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.BLACK_TEA.get(), 1),
                    5, 12, 0.03f));
        }


    }



    @SubscribeEvent
    public static void  addCustomWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 3),
                new ItemStack(ModBlocks.MOON_LILY.get(), 1),
                10, 2, 0.3f));

        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 5),
                new ItemStack(ModBlocks.MOON_SAPLING.get(), 1),
                10, 2, 0.3f));

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 12),
                new ItemStack(ModItems.GEMKITSUNE.get(), 1),
                3, 10, 0.5f));

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 20),
                new ItemStack(ModItems.FUR_ICE_DRAGON.get(), 1),
                3, 10, 0.5f));
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() == ModItems.NOTE_1.get() ||
                stack.getItem() == ModItems.NOTE_2.get() ||
                stack.getItem() == ModItems.NOTE_3.get() ||
                stack.getItem() == ModItems.NOTE_4.get() ||
                stack.getItem() == ModItems.NOTE_5.get() ||
                stack.getItem() == ModItems.NOTE_6.get()) {

            ItemStack book = BookHelper.getCorrespondingBook(stack.getItem());
            if (!book.isEmpty()) {
                event.getEntity().setItemInHand(event.getHand(), book);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            String mobId = event.getEntity().getEncodeId();

            net.lisalaf.fantastikmod.dialog.quest.QuestSystem.getActiveQuests()
                    .forEach((uuid, quest) -> {
                    });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;

        Player player = event.player;
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean hasCatnip = mainHand.getItem() == ModItems.CATNIP.get() ||
                offHand.getItem() == ModItems.CATNIP.get() ||
                mainHand.getItem() == ModItems.DRIED_CATNIP.get() ||
                offHand.getItem() == ModItems.DRIED_CATNIP.get();

        if (hasCatnip) {
            for (Cat cat : player.level().getEntitiesOfClass(Cat.class,
                    player.getBoundingBox().inflate(16))) {
                cat.getNavigation().moveTo(player, 1.2);
                cat.getLookControl().setLookAt(player, 30, 30);
            }
            for (BakenekoEntity bakeneko : player.level().getEntitiesOfClass(BakenekoEntity.class,
                    player.getBoundingBox().inflate(16))) {
                bakeneko.getNavigation().moveTo(player, 1.2);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack itemInHand = player.getItemInHand(event.getHand());
        var target = event.getTarget();

        if ((target instanceof Cat || target instanceof BakenekoEntity) &&
                (itemInHand.getItem() == ModItems.CATNIP.get() ||
                        itemInHand.getItem() == ModItems.DRIED_CATNIP.get())) {

            if (!player.level().isClientSide && target instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(ModEffects.CATNIP_EFFECT.get(), 600, 0));
                if (living instanceof BakenekoEntity bakeneko) {
                    bakeneko.setTarget(null);
                }
                if (!player.isCreative()) {
                    itemInHand.shrink(1);
                }
                for (int i = 0; i < 10; i++) {
                    player.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                            target.getX(), target.getY() + 0.5, target.getZ(),
                            0, 0.1, 0);
                }
                target.playSound(SoundEvents.CAT_EAT, 1.0F, 1.0F);
            }
            event.setCanceled(true);
        }
    }
}
