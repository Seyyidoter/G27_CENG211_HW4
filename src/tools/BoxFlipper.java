package tools;

import boxes.Box;
import boxes.FixedBox;
import exceptions.UnmovableFixedBoxException; 
import grid.BoxGrid;
import grid.Position;

public class BoxFlipper extends SpecialTool {

    public BoxFlipper() {
        super("Box Flipper");
    }

    @Override
    public void use(BoxGrid grid, Position pos, char targetLetter) throws UnmovableFixedBoxException {
        Box target = grid.getBox(pos);
        if (target == null) return;

        // Cannot flip a FixedBox.
        if (target instanceof FixedBox) {
            throw new UnmovableFixedBoxException("Cannot flip a FixedBox at " + pos);
        }

        // Flip the box
        target.flipUpsideDown();
        System.out.println("Box at " + pos + " has been flipped upside down.");
    }
}
