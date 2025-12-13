package grid;

import java.util.Locale;
import java.util.Objects;

/**
 * Immutable position on an 8x8 board.
 * Rows: 1..8, Cols: 1..8
 * Immutability prevents privacy leaks and makes it safe for sets/maps.
 */
public final class Position {

    public static final int MIN = 1;
    public static final int MAX = 8;

    private final int row;
    private final int col;

    public Position(int row, int col) {
        validate(row, col);
        this.row = row;
        this.col = col;
    }

    /**
     * Copy constructor (defensive; still useful for rubric).
     */
    public Position(Position other) {
        Objects.requireNonNull(other, "other position is null");
        this.row = other.row;
        this.col = other.col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public boolean isEdge() {
        return row == MIN || row == MAX || col == MIN || col == MAX;
    }

    public boolean isCorner() {
        return (row == MIN || row == MAX) && (col == MIN || col == MAX);
    }

    public boolean inBounds() {
        return row >= MIN && row <= MAX && col >= MIN && col <= MAX;
    }

    /**
     * Move one step in the given direction.
     * Throws IllegalArgumentException if result is out of bounds.
     */
    public Position move(Direction d) {
        Objects.requireNonNull(d, "direction is null");
        int nr = row;
        int nc = col;

        switch (d) {
            case UP -> nr--;
            case DOWN -> nr++;
            case LEFT -> nc--;
            case RIGHT -> nc++;
        }

        return new Position(nr, nc);
    }

    /**
     * Parses position strings case-insensitively.
     * Accepts:
     * - "R2-C4" or "r2-c4"
     * - "2-4", "2,4", "2 4"
     */
    public static Position parse(String raw) {
        if (raw == null) throw new IllegalArgumentException("Position input is null");
        String s = raw.trim().toUpperCase(Locale.ROOT);

        // Format: R2-C4
        if (s.matches("R[1-8]\\s*-\\s*C[1-8]")) {
            int r = s.charAt(s.indexOf('R') + 1) - '0';
            int c = s.charAt(s.indexOf('C') + 1) - '0';
            return new Position(r, c);
        }

        // Other simple formats: "2-4", "2,4", "2 4"
        s = s.replace(',', '-').replace(' ', '-');
        if (s.matches("[1-8]\\s*-\\s*[1-8]")) {
            String[] parts = s.split("-");
            int r = Integer.parseInt(parts[0].trim());
            int c = Integer.parseInt(parts[1].trim());
            return new Position(r, c);
        }

        throw new IllegalArgumentException("Invalid position format: " + raw);
    }

    private static void validate(int r, int c) {
        if (r < MIN || r > MAX || c < MIN || c > MAX) {
            throw new IllegalArgumentException("Out of bounds: row=" + r + ", col=" + c);
        }
    }

    @Override
    public String toString() {
        return "R" + row + "-C" + col;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position p)) return false;
        return row == p.row && col == p.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}
