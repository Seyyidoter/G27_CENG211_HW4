package tools;

import grid.BoxGrid;
import grid.Position;

/**
 * Abstract base class for special tools found inside boxes.
 * Holds the name of the tool.
 */
public abstract class SpecialTool implements ITool {

    private final String name;

    protected SpecialTool(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    
    @Override
    public abstract void use(BoxGrid grid, Position pos, char targetLetter) throws Exception;
}
