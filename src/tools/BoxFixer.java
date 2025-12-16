package tools;

import boxes.Box;
import boxes.FixedBox;
import exceptions.BoxAlreadyFixedException; 
import grid.BoxGrid;
import grid.Position;

public class BoxFixer extends SpecialTool {

    public BoxFixer() {
        super("Box Fixer");
    }

    @Override
    public void use(BoxGrid grid, Position pos) throws BoxAlreadyFixedException {
        Box currentBox = grid.getBox(pos);

        if (currentBox == null) return;

        // If it is already a FixedBox, we cannot fix it again.
        if (currentBox instanceof FixedBox) {
            throw new BoxAlreadyFixedException("The box at " + pos + " is already fixed!");
        }

        // Create a new FixedBox using the surfaces of the current box.
        // FixedBox constructor takes char[] surfaces.
        FixedBox newFixedBox = new FixedBox(currentBox.getSurfacesCopy());

        // Replace the box in the grid
        grid.setBox(pos, newFixedBox);
        
        System.out.println("Box at " + pos + " has been transformed into a FixedBox.");
    }
}
