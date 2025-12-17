package tools;

import boxes.Box;
import grid.BoxGrid;
import grid.Direction;
import grid.Position;

public class PlusShapeStamp extends SpecialTool {

    public PlusShapeStamp() {
        super("Plus Shape Stamp");
    }

    @Override
    public void use(BoxGrid grid, Position pos, char targetLetter) {
        // PlusShapeStamp re-stamps 5 boxes:
        // center (pos) + its 4 orthogonal neighbors (up/down/left/right).

        // 1) Stamp center box
        Box center = grid.getBox(pos);
        if (center != null) {
            center.stampTop(targetLetter);
        }

        // 2) Stamp 4 neighbors
        Direction[] dirs = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        for (Direction d : dirs) {
            try {
                Position neighborPos = pos.move(d); // throws if out of bounds
                Box neighbor = grid.getBox(neighborPos);
                if (neighbor != null) {
                    neighbor.stampTop(targetLetter);
                }
            } catch (IllegalArgumentException e) {
                // Out of bounds => ignore this direction
            }
        }

        System.out.println("Used PlusShapeStamp at " + pos + " with letter '" + targetLetter + "'");
    }
}
