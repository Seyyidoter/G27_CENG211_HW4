package boxes;

import tools.SpecialTool;

/**
 * RegularBox:
 * - Can be rolled
 * - Can be stamped
 * - May contain a SpecialTool
 */
public class RegularBox extends Box {

    public RegularBox(char[] surfaces, SpecialTool content) {
        super(surfaces, content, false);
    }

    public RegularBox(RegularBox other) {
        super(other);
    }

    @Override
    protected char getTypeMarker() {
        return 'R';
    }
}
