package net.lisalaf.fantastikmod.dialog;

import net.minecraft.network.chat.Component;
import java.util.ArrayList;
import java.util.List;

public class DialogNode {
    private final Component npcText;
    private final List<DialogOption> options;
    private final String id;

    public DialogNode(Component npcText, String id) {
        this.npcText = npcText;
        this.options = new ArrayList<>();
        this.id = id;
    }

    public DialogNode(Component npcText) {
        this(npcText, "default");
    }

    public DialogNode addOption(Component playerText, String nextNodeId) {
        options.add(new DialogOption(playerText, nextNodeId));
        return this;
    }

    public Component getNpcText() {
        return npcText;
    }

    public List<DialogOption> getOptions() {
        return options;
    }

    public List<DialogOption> getCurrentOptions() {
        return options;
    }

    public String getId() {
        return id;
    }
}