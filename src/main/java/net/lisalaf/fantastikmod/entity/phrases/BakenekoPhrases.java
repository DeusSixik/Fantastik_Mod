package net.lisalaf.fantastikmod.entity.phrases;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import java.util.Random;

public class BakenekoPhrases {
    private static final Random RANDOM = new Random();


    private static final String[][] HURT_MESSAGES = {
            {
                    "Meow?! That hurt!",
                    "Why would you do that?",
                    "Ouch! What was that for?",
                    "Hiss! Not nice!",
                    "Mrrow? That wasn't friendly...",
                    "You're mean! Meow!",
                    "Hey! I'm not a toy!",
                    "That's not how you pet a cat!",
                    "How dare you, human?!"
            },
            {
                    "Мяу?! Больно же!",
                    "Зачем ты так?",
                    "Ой! За что?",
                    "Шшш! Нехорошо!",
                    "Мррр? Это было не по-дружески...",
                    "Ты злой! Мяу!",
                    "Эй! Я не игрушка!",
                    "Кошек так не гладят!",
                    "Как ты посмел человечешка!?"
            }
    };

    private static final String[][] FEED_MESSAGES = {
            {
                    "Purrr... delicious!",
                    "Meow! Thank you!",
                    "More? Please? Meow~",
                    "*happy cat sounds*",
            },
            {
                    "Муррр... вкусно!",
                    "Мяу! Спасибо!",
                    "Ещё? Можно? Мяу~",
                    "*довольные кошачьи звуки*",
            }
    };

    private static final String[][] STEAL_FROM_PLAYER_MESSAGES = {
            {
                    "Haha! Mine now! Meow!",
                    "Finders keepers!",
                    "This is mine meow!",
                    "You snooze you lose!",
                    "*mischievous purr*",
                    "Shiny! I take!",
                    "Thanks for the gift!"
            },
            {
                    "Ха-ха! Теперь моё! Мяу!",
                    "Что нашла - то моё!",
                    "Это моё, мяу!",
                    "Кто не спрятал - я не виновата!",
                    "*хитрющее мурлыканье*",
                    "Блестяшка! Забираю!",
                    "Спасибо за подарочек!"
            }
    };

    private static final String[][] STOLEN_FROM_BAKENEKO_MESSAGES = {
            {
                    "Hey! That's mine!",
                    "Give it back! Meow!",
                    "Thief! Hiss!",
                    "How dare you!",
                    "You'll regret this!",
                    "*angry cat noises*",
                    "My precious! Gone!",
                    "I'll remember this!",
                    "How dare you, human?!"
            },
            {
                    "Эй! Это моё!",
                    "Верни! Мяу!",
                    "Воришка! Шшш!",
                    "Как ты посмел!",
                    "Пожалеешь об этом!",
                    "*злые кошачьи звуки*",
                    "Моё сокровище! Пропало!",
                    "Я это запомню!",
                    "Как ты посмел человечешка!?"

            }
    };

    public static String getHurtMessage(Player player) {
        return getMessage(player, HURT_MESSAGES);
    }

    public static String getFeedMessage(Player player) {
        return getMessage(player, FEED_MESSAGES);
    }

    public static String getStealFromPlayerMessage(Player player) {
        return getMessage(player, STEAL_FROM_PLAYER_MESSAGES);
    }

    public static String getStolenFromBakenekoMessage(Player player) {
        return getMessage(player, STOLEN_FROM_BAKENEKO_MESSAGES);
    }

    private static String getMessage(Player player, String[][] messages) {
        boolean isRussian = isRussianPlayer(player);
        int languageIndex = isRussian ? 1 : 0;
        String[] languageMessages = messages[languageIndex];
        return languageMessages[RANDOM.nextInt(languageMessages.length)];
    }

    private static boolean isRussianPlayer(Player player) {
        if (player.level().isClientSide) {
            return net.minecraft.client.Minecraft.getInstance().options.languageCode.startsWith("ru");
        } else {
            if (player instanceof ServerPlayer serverPlayer) {
                String language = serverPlayer.getLanguage();
                return language != null && language.startsWith("ru");
            }
            return false;
        }
    }
}