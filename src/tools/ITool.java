package tools;

import grid.BoxGrid;
import grid.Position;

/**
 * Interface for all tools in the game.
 * Every tool must have a name and a way to be applied to the grid.
 */
public interface ITool {
    
    String getName();

    /**
     * Applies the tool's effect to the specified position on the grid.
     * @param grid The game grid.
     * @param pos The target position selected by the player.
     * @throws Exception specific exceptions depending on the tool (e.g., BoxAlreadyFixedException).
     */
    void use(BoxGrid grid, Position pos) throws Exception;
}