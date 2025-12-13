package boxes;

import grid.Direction;

/**
 * FixedBox:
 * - Cannot be rolled (does nothing on roll)
 * - Stops domino-effect propagation (handled in BoxGrid)
 * - Always displayed as opened/empty (O)
 * - Never contains a SpecialTool (game logic ensures this)
 */
public class FixedBox extends Box {

    public FixedBox(char[] surfaces) {
        super(surfaces, null, true); // empty from start, shown as O
    }

    public FixedBox(FixedBox other) {
        super(other);
    }

    @Override
    protected char getTypeMarker() {
        return 'X';
    }

    @Override
    protected boolean isAlwaysOpenMark() {
        return true;
    }

    @Override
    public void roll(Direction direction) {
        // FixedBox cannot be rolled. Intentionally does nothing.
    }
}
