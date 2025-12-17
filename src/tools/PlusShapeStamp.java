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
        // We stamp the NEIGHBORS, not the center box itself.
        
        // Define the 4 cardinal directions
        Direction[] dirs = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        // Iterate and stamp neighbors with the targetLetter
        for (Direction d : dirs) {
            try {
                Position neighborPos = pos.move(d);
                
                if (neighborPos.inBounds()) {
                    Box target = grid.getBox(neighborPos);
                    if (target != null) {
                        // Use the passed targetLetter
                        target.stampTop(targetLetter);
                    }
                }
            } catch (IllegalArgumentException e) {
                // Hit the wall/boundary, ignore.
            }
        }
        
        System.out.println("Used PlusShapeStamp at " + pos + " with letter '" + targetLetter + "'");
    }
}
