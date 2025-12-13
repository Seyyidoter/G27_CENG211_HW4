package grid;

import boxes.Box;
import boxes.FixedBox;
import exceptions.UnmovableFixedBoxException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/*
ANSWER TO COLLECTIONS QUESTION:
I used a List<List<Box>> (ArrayList) to represent the 8x8 grid.
This provides O(1) indexed access while still using the Java Collections framework.
It also simplifies row/column iterations and avoids privacy leaks by never exposing
the underlying lists to the outside.
*/
public class BoxGrid {

    public static final int SIZE = 8;

    private final List<List<Box>> grid;     // 8x8, never exposed
    private final Set<Position> movedThisTurn; // boxes rolled in the first stage (used for "open" validation)

    public BoxGrid() {
        this.grid = new ArrayList<>(SIZE);
        for (int r = 0; r < SIZE; r++) {
            List<Box> row = new ArrayList<>(SIZE);
            for (int c = 0; c < SIZE; c++) {
                row.add(null); // filled by BoxPuzzle generation
            }
            grid.add(row);
        }
        this.movedThisTurn = new HashSet<>();
    }

    /**
     * Copy constructor (mostly for rubric/tests).
     * Note: Boxes are mutable; this is a shallow copy of Box references.
     */
    public BoxGrid(BoxGrid other) {
        Objects.requireNonNull(other, "other grid is null");
        this.grid = new ArrayList<>(SIZE);
        for (int r = 0; r < SIZE; r++) {
            this.grid.add(new ArrayList<>(other.grid.get(r)));
        }
        this.movedThisTurn = new HashSet<>(other.movedThisTurn);
    }

    // -------------------------
    // Accessors (safe)
    // -------------------------

    public Box getBox(Position p) {
        Objects.requireNonNull(p, "position is null");
        return grid.get(p.getRow() - 1).get(p.getCol() - 1);
    }

    public void setBox(Position p, Box b) {
        Objects.requireNonNull(p, "position is null");
        grid.get(p.getRow() - 1).set(p.getCol() - 1, b);
    }

    // -------------------------
    // First stage: rolling (domino-effect)
    // -------------------------

    /**
     * Rolls boxes from an edge position inward using domino-effect.
     * Rules handled here:
     * - If selected edge position contains a FixedBox => throw UnmovableFixedBoxException (wasted turn).
     * - Rolling stops when a FixedBox is reached (FixedBox blocks transmission).
     * - All rolled boxes are recorded in movedThisTurn for stage-2 validation.
     */
    public void rollFromEdge(Position edgePos, Direction inwardDir) throws UnmovableFixedBoxException {
        Objects.requireNonNull(edgePos, "edgePos is null");
        Objects.requireNonNull(inwardDir, "inwardDir is null");

        if (!edgePos.isEdge()) {
            throw new IllegalArgumentException("Selected position is not on the edge: " + edgePos);
        }

        Box start = getBox(edgePos);
        if (start instanceof FixedBox) {
            throw new UnmovableFixedBoxException("Selected edge box is FixedBox and cannot be moved: " + edgePos);
        }

        movedThisTurn.clear();

        Position cur = edgePos;
        while (cur.inBounds()) {
            Box b = getBox(cur);
            if (b == null) {
                // Defensive: generation bug should not crash everything.
                break;
            }

            if (b instanceof FixedBox) {
                // FixedBox blocks domino-effect; stop before it.
                break;
            }

            b.roll(inwardDir);
            movedThisTurn.add(new Position(cur)); // store copy (Position is immutable anyway)

            // Move inward; if out of bounds, finish.
            cur = tryMove(cur, inwardDir);
            if (cur == null) break;
        }
    }

    private Position tryMove(Position p, Direction d) {
        try {
            return p.move(d);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    // -------------------------
    // Second stage helper: "open" validation support
    // -------------------------

    public boolean wasMovedThisTurn(Position p) {
        return movedThisTurn.contains(p);
    }

    public void resetMovedThisTurn() {
        movedThisTurn.clear();
    }

    // -------------------------
    // Corner/edge direction helper
    // -------------------------

    /**
     * Returns possible inward directions for an edge position.
     * - Non-corner edge => exactly one direction
     * - Corner => two directions
     */
    public List<Direction> allowedInwardDirections(Position edgePos) {
        Objects.requireNonNull(edgePos, "edgePos is null");
        if (!edgePos.isEdge()) {
            throw new IllegalArgumentException("Not an edge position: " + edgePos);
        }

        List<Direction> dirs = new ArrayList<>(2);
        if (edgePos.getRow() == 1) dirs.add(Direction.DOWN);
        if (edgePos.getRow() == SIZE) dirs.add(Direction.UP);
        if (edgePos.getCol() == 1) dirs.add(Direction.RIGHT);
        if (edgePos.getCol() == SIZE) dirs.add(Direction.LEFT);
        return dirs;
    }

    // -------------------------
    // Printing
    // -------------------------

    /**
     * Prints the grid in the expected legend format.
     */
    public String toPrettyString() {
        StringBuilder sb = new StringBuilder();

        sb.append("     C1      C2      C3      C4      C5      C6      C7      C8\n");
        sb.append(" -----------------------------------------------------------------\n");

        for (int r = 1; r <= SIZE; r++) {
            sb.append("R").append(r).append(" ");
            for (int c = 1; c <= SIZE; c++) {
                Box b = grid.get(r - 1).get(c - 1);
                sb.append(b == null ? "| ?-?-? |" : b.gridToken()).append(" ");
            }
            sb.append("\n -----------------------------------------------------------------\n");
        }

        return sb.toString();
    }
}
