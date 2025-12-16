package tools;

import boxes.Box;
import grid.BoxGrid;
import grid.Position;

public class BoxFlipper extends SpecialTool {

    public BoxFlipper() {
        super("Box Flipper");
    }

    @Override
    public void use(BoxGrid grid, Position pos) {
        Box target = grid.getBox(pos);
        if (target != null) {
            target.flipUpsideDown();
            System.out.println("Box at " + pos + " has been flipped upside down.");
        }
    }
}
