package boxes;

import grid.Direction;

/**
 * Capability interface for objects that can be rolled on the grid.
 */
public interface Rollable {
    void roll(Direction direction);
}
