package boxes;

import tools.SpecialTool;

/**
 * UnchangingBox:
 * - Can be rolled like a normal box
 * - But: no face can be re-stamped by any SpecialTool after it is generated.
 *   Therefore, we override stamp(...) and ignore all stamping requests.
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
    }
}
