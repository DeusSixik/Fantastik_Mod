package net.lisalaf.fantastikmod.dialog;

import net.minecraft.network.chat.Component;

import java.util.*;

public class Dialog {
    protected final Map<String, DialogNode> nodes = new HashMap<>();
    protected DialogNode currentNode;
    protected String currentDialogId;
    protected Random random;

    public Dialog() {
        this.random = new Random();
        setupDialog();
        if (currentNode == null && !nodes.isEmpty()) {
            currentNode = nodes.get("start");
            currentDialogId = "start";
        }
    }

    protected void setupDialog() {
    }

    protected void addNode(String id, DialogNode node) {
        nodes.put(id, node);
        if (currentNode == null) {
            currentNode = node;
            currentDialogId = id;
        }
    }

    public DialogNode getCurrentNode() {
        DialogNode dynamicNode = getDynamicNode(currentDialogId);
        if (dynamicNode != null) {
            return dynamicNode;
        }
        return currentNode;
    }

    protected DialogNode getDynamicNode(String nodeId) {
        return null;
    }

    public void selectOption(int optionIndex) {
        DialogNode node = getCurrentNode();

        if (node != null && optionIndex < node.getOptions().size()) {
            DialogOption selectedOption = node.getOptions().get(optionIndex);
            String nextNodeId = selectedOption.getNextNodeId();
            currentDialogId = nextNodeId;
            currentNode = nodes.get(nextNodeId);
        }
    }

    public List<DialogOption> getCurrentOptions() {
        DialogNode node = getCurrentNode();
        if (node != null) {
            return node.getOptions();
        }
        return new ArrayList<>();
    }

    public boolean isFinished() {
        DialogNode node = getCurrentNode();
        return node == null || node.getOptions().isEmpty();
    }

    public void reset() {
        currentNode = nodes.get("start");
        currentDialogId = "start";
    }

    public Component getRandomStory() {
        String[] storyKeys = {
                "dialog.kitsune.story1", "dialog.kitsune.story2", "dialog.kitsune.story3",
                "dialog.kitsune.story4", "dialog.kitsune.story5", "dialog.kitsune.story6"
        };
        String randomKey = storyKeys[this.random.nextInt(storyKeys.length)];
        return Component.translatable(randomKey);
    }

    public boolean canStart() {
        return nodes.containsKey("start") && nodes.get("start") != null;
    }

    public String getCurrentDialogId() {
        return currentDialogId;
    }
}