package puzzle;

import boxes.*;
import exceptions.*;
import grid.*;
import tools.*;
import util.InputHelper;

import java.util.*;

public class BoxPuzzle {

    private final BoxGrid boxGrid;
    private final char targetLetter;
    private static final int MAX_TURNS = 5;
    private static final char[] LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    private final Random random;

    public BoxPuzzle() {
        this.random = new Random();
        this.boxGrid = new BoxGrid();
        // Randomly select target letter
        this.targetLetter = LETTERS[random.nextInt(LETTERS.length)];
        
        // Populate the grid (since BoxGrid initializes with nulls)
        generateGrid();
    }

    /**
     * Main game loop.
     */
    public void play() {
        System.out.println("Welcome to Box Top Side Matching Puzzle App. An 8x8 box grid is being generated.");
        System.out.println("Your goal is to maximize the letter \"" + targetLetter + "\" on the top sides of the boxes.");
        
        System.out.println("The initial state of the box grid:");
        System.out.println(boxGrid.toPrettyString());

        for (int turn = 1; turn <= MAX_TURNS; turn++) {
            System.out.println("\n> TURN " + turn + ":");
            
            // Inner class for menu operations
            Menu menu = new Menu();

            try {
                // Optional: View Surfaces
                menu.handleViewSurfaces();

                // Stage 1: Rolling
                System.out.println("---> TURN " + turn + " FIRST STAGE:");
                boolean rollSuccess = menu.handleRollingStage();
                
                // If rolling failed (e.g. FixedBox on edge), the exception is caught below 
                // and the turn is wasted immediately.
                
                System.out.println("The new state of the box grid:");
                System.out.println(boxGrid.toPrettyString());

                // Stage 2: Opening & Tools
                System.out.println("---> TURN " + turn + " SECOND STAGE:");
                menu.handleToolStage(); // May throw EmptyBoxException or BoxAlreadyFixedException
                
                System.out.println("The new state of the box grid:");
                System.out.println(boxGrid.toPrettyString());

            } catch (UnmovableFixedBoxException e) {
                // Turn is wasted
                System.out.println(e.getMessage());
                System.out.println("Turn is wasted!");
            } catch (EmptyBoxException e) {
                // Turn is wasted
                System.out.println("BOX IS EMPTY! " + e.getMessage());
                System.out.println("Turn is wasted!");
            } catch (BoxAlreadyFixedException e) {
                // Turn is wasted
                System.out.println(e.getMessage());
                System.out.println("Turn is wasted!");
            } catch (Exception e) {
                // Generic handler to prevent crash
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Reset moved status for next turn
            boxGrid.resetMovedThisTurn();
        }

        endGame();
    }

    /**
     * Calculates score and prints result.
     */
    private void endGame() {
        int count = 0;
        for (int r = 1; r <= BoxGrid.SIZE; r++) {
            for (int c = 1; c <= BoxGrid.SIZE; c++) {
                if (boxGrid.getBox(new Position(r, c)).getTopLetter() == targetLetter) {
                    count++;
                }
            }
        }
        
        System.out.println("GAME OVER");
        System.out.println("The final state of the box grid:");
        System.out.println(boxGrid.toPrettyString());
        System.out.println("THE TOTAL NUMBER OF TARGET LETTER \"" + targetLetter + "\"");
        System.out.println("IN THE BOX GRID --> " + count);
        // Print SUCCESS message
        System.out.println("The game has been SUCCESSFULLY completed!");
    }

    // =============================================================
    // GRID GENERATION LOGIC
    // =============================================================
    private void generateGrid() {
        for (int r = 1; r <= BoxGrid.SIZE; r++) {
            for (int c = 1; c <= BoxGrid.SIZE; c++) {
                Position pos = new Position(r, c);
                Box box = createRandomBox();
                boxGrid.setBox(pos, box);
            }
        }
    }

    private Box createRandomBox() {
        char[] surfaces = generateValidSurfaces();
        double roll = random.nextDouble();

        // Probabilities: 85% Regular, 5% Fixed, 10% Unchanging (Implied remainder)
        if (roll < 0.85) {
            // RegularBox
            // 75% chance of tool
            SpecialTool tool = (random.nextDouble() < 0.75) ? generateRandomTool() : null;
            return new RegularBox(surfaces, tool);
        } else if (roll < 0.90) { // 85% to 90% range covers the 5% for FixedBox
            // FixedBox
            // Never contains a tool
            return new FixedBox(surfaces);
        } else {
            // UnchangingBox
            // Guaranteed to contain a tool
            return new UnchangingBox(surfaces, generateRandomTool());
        }
    }

    private char[] generateValidSurfaces() {
        while (true) {
            char[] s = new char[6];
            Map<Character, Integer> counts = new HashMap<>();
            boolean valid = true;

            for (int i = 0; i < 6; i++) {
                char c = LETTERS[random.nextInt(LETTERS.length)];
                s[i] = c;
                counts.put(c, counts.getOrDefault(c, 0) + 1);
                // Max 2 of same letter
                if (counts.get(c) > 2) {
                    valid = false;
                    break;
                }
            }
            if (valid) return s;
        }
    }

    private SpecialTool generateRandomTool() {
        // 5 Types of tools
        int pick = random.nextInt(5);
        return switch (pick) {
            case 0 -> new PlusShapeStamp();
            case 1 -> new MassRowStamp();
            case 2 -> new MassColumnStamp();
            case 3 -> new BoxFlipper();
            case 4 -> new BoxFixer();
            default -> null; 
        };
    }

    // =============================================================
    // MENU INNER CLASS
    // =============================================================
    private class Menu {

        void handleViewSurfaces() {
            // Ask to view surfaces
            int choice = InputHelper.readInt("---> Do you want to view all surfaces of a box? [1] Yes or [2] No? ", 1, 2);
            if (choice == 1) {
                Position p = InputHelper.readPosition("Please enter the location of the box you want to view: ");
                Box b = boxGrid.getBox(p);
                // Show cube diagram
                System.out.println(b.toNetString());
            }
            System.out.println("Continuing to the first stage...");
        }

        boolean handleRollingStage() throws UnmovableFixedBoxException {
            while (true) {
                Position edgePos = InputHelper.readPosition("Please enter the location of the edge box you want to roll: ");
                
                // Validate Edge
                if (!edgePos.isEdge()) {
                    System.out.println("INCORRECT INPUT: The chosen box is not on any of the edges.");
                    continue;
                }

                // Check direction options
                List<Direction> options = boxGrid.allowedInwardDirections(edgePos);
                Direction selectedDir;

                if (options.size() == 1) {
                    selectedDir = options.get(0);
                    // Rolls automatically if not corner
                    System.out.println("The chosen box is automatically rolled " + selectedDir + ".");
                } else {
                    // Corner case: Player chooses direction
                    System.out.println("The chosen box can be rolled to either [1] " + options.get(0) + " or [2] " + options.get(1) + ": ");
                    int choice = InputHelper.readInt("", 1, 2);
                    selectedDir = options.get(choice - 1);
                }

                // Execute roll (might throw UnmovableFixedBoxException)
                boxGrid.rollFromEdge(edgePos, selectedDir);
                return true;
            }
        }

        void handleToolStage() throws EmptyBoxException, BoxAlreadyFixedException, Exception {
            while (true) {
                Position pos = InputHelper.readPosition("Please enter the location of the box you want to open: ");
                
                // Must select a box rolled in previous stage
                if (!boxGrid.wasMovedThisTurn(pos)) {
                    System.out.println("INCORRECT INPUT: The chosen box was not rolled during the first stage.");
                    continue;
                }

                Box box = boxGrid.getBox(pos);
                // Open box, lid closed, contents marked empty
                SpecialTool tool = box.openAndTakeContent();

                if (tool == null) {
                    throw new EmptyBoxException("Continuing to the next turn...");
                }

                System.out.println("The box on location " + pos + " is opened. It contains a SpecialTool --> " + tool.getName());
                
                // Must be used immediately
                Position targetPos = InputHelper.readPosition("Please enter the location of the box to use this SpecialTool: ");
                
                // Call useTool directly on object
                tool.use(boxGrid, targetPos);
                break;
            }
        }
    }
}
