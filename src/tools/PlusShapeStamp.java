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
    public void use(BoxGrid grid, Position pos) {
        // 1. Get the source box to find which letter to stamp
        Box sourceBox = grid.getBox(pos);
        if (sourceBox == null) return;

        char letterToStamp = sourceBox.getTopLetter();

        // 2. Define the 4 cardinal directions
        Direction[] dirs = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

        // 3. Iterate and stamp neighbors
        for (Direction d : dirs) {
            try {
                // Calculate neighbor position
                Position neighborPos = pos.move(d);
                
                // If position is valid (in bounds), get the box
                if (neighborPos.inBounds()) {
                    Box target = grid.getBox(neighborPos);
                    if (target != null) {
                        // Stamp the top face of the neighbor
                        target.stampTop(letterToStamp);
                    }
                }
            } catch (IllegalArgumentException e) {
                // Hit the wall/boundary, ignore.
            }
        }
        
        System.out.println("Used PlusShapeStamp at " + pos + " with letter '" + letterToStamp + "'");
    }
}
