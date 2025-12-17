package boxes;

import tools.SpecialTool;

/**
 * UnchangingBox:
 * - Can be rolled like a normal box.
 * - Is completely immune to all SpecialTool effects after it is generated.
 *
 * Clarification (Instructor-approved):
 * Although the PDF specification contains wording that may appear ambiguous,
 * the instructor clarified that all SpecialTools in this assignment affect
 * only the top face of a box, and UnchangingBox must not be affected by any
 * SpecialTool, either directly or indirectly.
 *
 * Therefore, all stamping attempts (including top-side stamping) are ignored.
 */

public class UnchangingBox extends Box {

    public UnchangingBox(char[] surfaces, SpecialTool content) {
        super(surfaces, content, false);
    }

    public UnchangingBox(UnchangingBox other) {
        super(other);
    }

    @Override
    protected char getTypeMarker() {
        return 'U';
    }

    @Override
    public void stamp(Face face, char letter) {
        // Intentionally do nothing:
        // UnchangingBox faces cannot be changed by any SpecialTool.
        // (No validation or exceptions here, by design.)
    }
}
