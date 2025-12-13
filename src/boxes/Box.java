package boxes;

import grid.Direction;
import tools.SpecialTool;

import java.util.Arrays;
import java.util.Objects;

/**
 * Base abstraction for all Boxes on the grid.
 * Design goals:
 * - Prevent privacy leaks: do NOT expose internal arrays directly.
 * - Support polymorphism: subclasses override behavior (FixedBox cannot roll, UnchangingBox cannot be stamped).
 * - Keep state minimal and consistent.
 */
public abstract class Box implements Rollable {

    // Internal fixed mapping to indices (kept private/protected to avoid duplication bugs).
    protected static final int IDX_TOP = 0;
    protected static final int IDX_BOTTOM = 1;
    protected static final int IDX_LEFT = 2;
    protected static final int IDX_RIGHT = 3;
    protected static final int IDX_FRONT = 4; // towards DOWN direction
    protected static final int IDX_BACK = 5;  // towards UP direction

    private final char[] surfaces;  // 6 letters, never leaked
    private boolean opened;         // opened boxes become empty (O marker)
    private SpecialTool content;    // can be null if empty

    /**
     * Main constructor: defensive-copies surfaces.
     */
    protected Box(char[] surfaces, SpecialTool content, boolean opened) {
        validateSurfaces(surfaces);
        this.surfaces = Arrays.copyOf(surfaces, surfaces.length); // defensive copy => no privacy leak
        this.content = content;
        this.opened = opened;
    }

    /**
     * Copy constructor:
     * - Deep copy surfaces array
     * - Copy simple fields
     * - content reference is copied as-is (tool objects are managed by game logic)
     */
    protected Box(Box other) {
        Objects.requireNonNull(other, "other box is null");
        this.surfaces = Arrays.copyOf(other.surfaces, other.surfaces.length);
        this.opened = other.opened;
        this.content = other.content;
    }

    // -------------------------
    // Basic getters (safe)
    // -------------------------

    public final boolean isOpened() {
        return opened;
    }

    public final boolean isEmpty() {
        return content == null;
    }

    public final char getTopLetter() {
        return surfaces[IDX_TOP];
    }

    public final char getLetter(Face face) {
        Objects.requireNonNull(face, "face is null");
        return surfaces[toIndex(face)];
    }

    /**
     * Returns a defensive copy to avoid privacy leak.
     */
    public final char[] getSurfacesCopy() {
        return Arrays.copyOf(surfaces, surfaces.length);
    }

    /**
     * Opens the box and returns the tool (if any).
     * After opening, the box becomes empty and should be marked as 'O'.
     * Note: EmptyBoxException is thrown by upper-level menu/game logic
     * when this method returns null, because this class should stay
     * reusable and simple.
     */
    public final SpecialTool openAndTakeContent() {
        opened = true;
        SpecialTool t = content;
        content = null;
        return t;
    }

    /**
     * Sets content (typically during grid generation).
     * Keep it simple; game logic is responsible for probabilities/rules.
     */
    public final void setContent(SpecialTool tool) {
        this.content = tool;
    }

    // -------------------------
    // Stamping API
    // -------------------------

    /**
     * Generic stamping API used by SpecialTools.
     * Default behavior: stamps the given face.
     * UnchangingBox must override and ignore stamping on ALL faces.
     */
    public void stamp(Face face, char letter) {
        Objects.requireNonNull(face, "face is null");

        // Letter validation: allowed stamped letters are A..H (case-insensitive)
        char L = Character.toUpperCase(letter);
        if (L < 'A' || L > 'H') {
            throw new IllegalArgumentException("Invalid stamped letter: " + letter + ". Allowed letters are A..H.");
        }

        surfaces[toIndex(face)] = L;
    }

    /**
     * Convenience method: stamp only TOP.
     */
    public void stampTop(char letter) {
        stamp(Face.TOP, letter);
    }

    /**
     * Tool support: flip the box upside down (swap TOP and BOTTOM).
     * FixedBox override: should not be flippable in game logic (exception thrown above),
     * but keeping this method is convenient for tests / tool implementation.
     */
    public void flipUpsideDown() {
        swap(IDX_TOP, IDX_BOTTOM);
    }

    // -------------------------
    // Rolling (dice behavior)
    // -------------------------

    /**
     * Default rolling behavior for dice-like rotation.
     * FixedBox overrides with "do nothing".
     */
    @Override
    public void roll(Direction direction) {
        Objects.requireNonNull(direction, "direction is null");

        // Dice-like cycles (4-cycle) depending on direction.
        switch (direction) {
            case RIGHT -> cycle(IDX_TOP, IDX_LEFT, IDX_BOTTOM, IDX_RIGHT);
            case LEFT  -> cycle(IDX_TOP, IDX_RIGHT, IDX_BOTTOM, IDX_LEFT);
            case UP    -> cycle(IDX_TOP, IDX_FRONT, IDX_BOTTOM, IDX_BACK);
            case DOWN  -> cycle(IDX_TOP, IDX_BACK, IDX_BOTTOM, IDX_FRONT);
        }
    }

    // -------------------------
    // Printing helpers
    // -------------------------

    /**
     * Returns the marker char for legend:
     * RegularBox => 'R', UnchangingBox => 'U', FixedBox => 'X'
     */
    protected abstract char getTypeMarker();

    /**
     * FixedBox is always displayed as opened/empty (O).
     */
    protected boolean isAlwaysOpenMark() {
        return false;
    }

    /**
     * Grid token format: | R-E-M | etc.
     * O = opened/empty, M = mystery (not opened yet)
     */
    public final String gridToken() {
        char type = getTypeMarker();
        char status = (isAlwaysOpenMark() || opened) ? 'O' : 'M';
        return "| " + type + "-" + getTopLetter() + "-" + status + " |";
    }

    /**
     * Printable cube net view for "view all surfaces" option.
     * The middle letter is the TOP face.
     */
    public final String toNetString() {
        StringBuilder sb = new StringBuilder();

        sb.append("  -----\n");
        sb.append("  | ").append(getLetter(Face.BACK)).append(" |\n");
        sb.append("-------------\n");

        sb.append("| ").append(getLetter(Face.LEFT)).append(" | ")
                .append(getLetter(Face.TOP)).append(" | ")
                .append(getLetter(Face.RIGHT)).append(" |\n");

        sb.append("-------------\n");
        sb.append("  | ").append(getLetter(Face.FRONT)).append(" |\n");
        sb.append("  -----\n");
        sb.append("  | ").append(getLetter(Face.BOTTOM)).append(" |\n");
        sb.append("  -----");

        return sb.toString();
    }

    // -------------------------
    // Internal utilities
    // -------------------------

    private static void validateSurfaces(char[] s) {
        if (s == null || s.length != 6) {
            throw new IllegalArgumentException("surfaces must be a char[6]");
        }
    }

    private static int toIndex(Face f) {
        return switch (f) {
            case TOP -> IDX_TOP;
            case BOTTOM -> IDX_BOTTOM;
            case LEFT -> IDX_LEFT;
            case RIGHT -> IDX_RIGHT;
            case FRONT -> IDX_FRONT;
            case BACK -> IDX_BACK;
        };
    }

    private void swap(int a, int b) {
        char tmp = surfaces[a];
        surfaces[a] = surfaces[b];
        surfaces[b] = tmp;
    }

    /**
     * 4-cycle: a <- b <- c <- d <- a (rotates values)
     */
    private void cycle(int a, int b, int c, int d) {
        char tmp = surfaces[a];
        surfaces[a] = surfaces[b];
        surfaces[b] = surfaces[c];
        surfaces[c] = surfaces[d];
        surfaces[d] = tmp;
    }
}
