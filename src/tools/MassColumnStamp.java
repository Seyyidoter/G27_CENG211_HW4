package tools;

import boxes.Box;
import grid.BoxGrid;
import grid.Position;

public class MassColumnStamp extends SpecialTool {

    public MassColumnStamp() {
        super("Mass Column Stamp");
    }

    @Override
    public void use(BoxGrid grid, Position pos, char targetLetter) {
        int col = pos.getCol();

        // Iterate through all rows in this column
        for (int r = 1; r <= BoxGrid.SIZE; r++) {
            Position targetPos = new Position(r, col);
            Box target = grid.getBox(targetPos);

            if (target != null) {
                // Stamp with the target letter
                target.stampTop(targetLetter);
            }
        }

        System.out.println("Mass Column Stamp applied to Column " + col + " with '" + targetLetter + "'");
    }
}
