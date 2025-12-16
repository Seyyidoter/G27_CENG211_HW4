package tools;

import boxes.Box;
import grid.BoxGrid;
import grid.Position;

public class MassRowStamp extends SpecialTool {

    public MassRowStamp() {
        super("Mass Row Stamp");
    }

    @Override
    public void use(BoxGrid grid, Position pos) {
        Box sourceBox = grid.getBox(pos);
        if (sourceBox == null) return;

        char letterToStamp = sourceBox.getTopLetter();
        int row = pos.getRow();

        // Iterate through all columns in this row
        for (int c = 1; c <= BoxGrid.SIZE; c++) {
            // Skip the source box itself if desired, or stamp it too
            Position targetPos = new Position(row, c);
            Box target = grid.getBox(targetPos);
            
            if (target != null) {
                target.stampTop(letterToStamp);
            }
        }
        
        System.out.println("Mass Row Stamp applied to Row " + row);
    }
}
