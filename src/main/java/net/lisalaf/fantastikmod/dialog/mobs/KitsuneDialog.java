package net.lisalaf.fantastikmod.dialog.mobs;

import net.lisalaf.fantastikmod.dialog.Dialog;
import net.lisalaf.fantastikmod.dialog.DialogNode;
import net.lisalaf.fantastikmod.dialog.DialogOption;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class KitsuneDialog extends Dialog {

    private String currentLegend = "";
    private String currentStory = "";
    private boolean legendFixed = false;
    private boolean storyFixed = false;

    @Override
    protected void setupDialog() {
        // Начальный узел
        addNode("start", new DialogNode(
                Component.translatable("dialog.kitsune_light.greeting"), "start"
        ).addOption(Component.translatable("dialog.player.ask_missed"), "ask_missed")
                .addOption(Component.translatable("dialog.player.ask_activities"), "ask_activities")
                .addOption(Component.translatable("dialog.player.ask_legend"), "ask_legend")
                .addOption(Component.translatable("dialog.player.ask_personal"), "ask_personal")
                .addOption(Component.translatable("dialog.player.goodbye"), "end"));

        addNode("ask_missed", new DialogNode(
                Component.translatable("dialog.kitsune_light.ask_missed"), "ask_missed"
        ).addOption(Component.translatable("dialog.player.ask_activities"), "ask_activities")
                .addOption(Component.translatable("dialog.player.ask_legend"), "ask_legend")
                .addOption(Component.translatable("dialog.player.ask_personal"), "ask_personal")
                .addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("ask_activities", new DialogNode(
                Component.translatable("dialog.kitsune_light.ask_activities"), "ask_activities"
        ).addOption(Component.translatable("dialog.player.ask_missed"), "ask_missed")
                .addOption(Component.translatable("dialog.player.ask_legend"), "ask_legend")
                .addOption(Component.translatable("dialog.player.ask_personal"), "ask_personal")
                .addOption(Component.translatable("dialog.player.back"), "start"));

        // Узел выбора легенды
        addNode("ask_legend", new DialogNode(
                Component.translatable("dialog.kitsune_light.ask_legend"), "ask_legend"
        ).addOption(Component.translatable("dialog.player.yes_legend"), "show_legend")
                .addOption(Component.translatable("dialog.player.no_legend"), "start")
                .addOption(Component.translatable("dialog.player.back"), "start"));

        // Узел выбора истории
        addNode("ask_personal", new DialogNode(
                Component.translatable("dialog.kitsune_light.ask_personal"), "ask_personal"
        ).addOption(Component.translatable("dialog.player.yes_personal"), "show_story")
                .addOption(Component.translatable("dialog.player.no_personal"), "start")
                .addOption(Component.translatable("dialog.player.back"), "start"));


        addNode("show_legend", new DialogNode(Component.empty(), "show_legend"));
        addNode("show_story", new DialogNode(Component.empty(), "show_story"));

        // ОТВЕТЫ НА ЛЕГЕНДЫ
        addNode("legend1_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.legend1_response"), "legend1_response"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("legend2_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.legend2_response"), "legend2_response"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("legend3_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.legend3_response"), "legend3_response"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("legend4_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.legend4_response"), "legend4_response"
        ).addOption(Component.translatable("dialog.player.legend4_followup"), "legend4_followup")
                .addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("legend4_followup", new DialogNode(
                Component.translatable("dialog.kitsune_light.legend4_followup"), "legend4_followup"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("legend5_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.legend5_response"), "legend5_response"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("legend6_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.legend6_response"), "legend6_response"
        ).addOption(Component.translatable("dialog.player.legend6_serious"), "legend6_serious")
                .addOption(Component.translatable("dialog.player.legend6_silent"), "legend6_silent")
                .addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("legend6_serious", new DialogNode(
                Component.translatable("dialog.kitsune_light.legend6_serious"), "legend6_serious"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("legend6_silent", new DialogNode(
                Component.translatable("dialog.kitsune_light.legend6_silent"), "legend6_silent"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        // ОТВЕТЫ НА ИСТОРИИ
        addNode("story1_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.story1_response"), "story1_response"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("story2_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.story2_response"), "story2_response"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("story3_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.story3_response"), "story3_response"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("story4_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.story4_response"), "story4_response"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("story5_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.story5_response"), "story5_response"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("story6_response", new DialogNode(
                Component.translatable("dialog.kitsune_light.story6_response"), "story6_response"
        ).addOption(Component.translatable("dialog.player.back"), "start"));

        addNode("end", new DialogNode(
                Component.translatable("dialog.kitsune_light.farewell"), "end"
        ));
    }

    @Override
    protected DialogNode getDynamicNode(String nodeId) {
        if ("show_legend".equals(nodeId)) {
            return getDynamicLegendNode();
        } else if ("show_story".equals(nodeId)) {
            return getDynamicStoryNode();
        }
        return null;
    }

    private DialogNode getDynamicLegendNode() {
        if (!legendFixed) {
            currentLegend = getRandomLegendType();
            legendFixed = true;
            System.out.println("ФИКСИРУЕМ легенду: " + currentLegend);
        }

        DialogNode node = new DialogNode(Component.translatable("dialog.kitsune_light." + currentLegend), "show_legend");

        switch (currentLegend) {
            case "legend1":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_legend1"), "legend1_response");
                break;
            case "legend2":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_legend2"), "legend2_response");
                break;
            case "legend3":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_legend3"), "legend3_response");
                break;
            case "legend4":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_legend4"), "legend4_response");
                break;
            case "legend5":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_legend5"), "legend5_response");
                break;
            case "legend6":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_legend6"), "legend6_response");
                break;
        }
        node.addOption(Component.translatable("dialog.player.back"), "start");

        return node;
    }

    private DialogNode getDynamicStoryNode() {
        if (!storyFixed) {
            currentStory = getRandomStoryType();
            storyFixed = true;
            System.out.println("ФИКСИРУЕМ историю: " + currentStory);
        }

        DialogNode node = new DialogNode(Component.translatable("dialog.kitsune_light." + currentStory), "show_story");

        switch (currentStory) {
            case "personal1":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_story1"), "story1_response");
                break;
            case "personal2":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_story2"), "story2_response");
                break;
            case "personal3":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_story3"), "story3_response");
                break;
            case "personal4":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_story4"), "story4_response");
                break;
            case "personal5":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_story5"), "story5_response");
                break;
            case "personal6":
                node.addOption(Component.translatable("dialog.player.ask_kitsune_light_story6"), "story6_response");
                break;
        }
        node.addOption(Component.translatable("dialog.player.back"), "start");

        return node;
    }

    private String getRandomLegendType() {
        String[] legendTypes = {"legend1", "legend2", "legend3", "legend4", "legend5", "legend6"};
        int index = this.random.nextInt(legendTypes.length);
        return legendTypes[index];
    }

    private String getRandomStoryType() {
        String[] storyTypes = {"personal1", "personal2", "personal3", "personal4", "personal5", "personal6"};
        int index = this.random.nextInt(storyTypes.length);
        return storyTypes[index];
    }

    @Override
    public void reset() {
        super.reset();
        currentLegend = "";
        currentStory = "";
        legendFixed = false;
        storyFixed = false;
    }
}