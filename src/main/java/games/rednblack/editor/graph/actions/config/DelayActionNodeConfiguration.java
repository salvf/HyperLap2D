package games.rednblack.editor.graph.actions.config;

import games.rednblack.editor.graph.GraphNodeInputImpl;
import games.rednblack.editor.graph.GraphNodeOutputImpl;
import games.rednblack.editor.graph.actions.ActionFieldType;
import games.rednblack.editor.graph.config.NodeConfigurationImpl;

import static games.rednblack.editor.graph.actions.ActionFieldType.Action;

public class DelayActionNodeConfiguration extends NodeConfigurationImpl<ActionFieldType> {

    public DelayActionNodeConfiguration() {
        super("Action", "Delay", "Action");

        addNodeInput(
                new GraphNodeInputImpl<ActionFieldType>("delay", "Delay", true, ActionFieldType.Float));

        addNodeInput(
                new GraphNodeInputImpl<ActionFieldType>("delayed", "Action", false, Action));

        addNodeOutput(
                new GraphNodeOutputImpl<>("action", "Action", Action));
    }
}