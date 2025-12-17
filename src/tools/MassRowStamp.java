package tools;

import boxes.Box;
import grid.BoxGrid;
import grid.Position;

public class MassRowStamp extends SpecialTool {

    public MassRowStamp() {
        super("Mass Row Stamp");
    }

    @Override
    public void use(BoxGrid grid, Position pos, char targetLetter) {
        int row = pos.getRow();

        // Iterate through all columns in this row
        for (int c = 1; c <= BoxGrid.SIZE; c++) {
            Position targetPos = new Position(row, c);
            Box target = grid.getBox(targetPos);
            
            if (target != null) {
                // Stamp with the target letter
                target.stampTop(targetLetter);
            }
        }
        
        System.out.println("Mass Row Stamp applied to Row " + row + " with '" + targetLetter + "'");
    }
}
