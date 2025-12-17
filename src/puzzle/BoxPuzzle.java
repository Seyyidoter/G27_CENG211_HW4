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
        this.targetLetter = LETTERS[random.nextInt(LETTERS.length)];
        generateGrid();
    }

    /**
     * Main game loop.
     */
    public void play() {
        System.out.println("Welcome to Box Top Side Matching Puzzle App. An 8x8 box grid is being generated.");
        System.out.println("Your goal is to maximize the letter \"" + targetLetter + "\" on the top sides of the boxes.");
        System.out.println();
        System.out.println("The initial state of the box grid:");
        System.out.println(boxGrid.toPrettyString());

        Menu menu = new Menu();

        for (int turn = 1; turn <= MAX_TURNS; turn++) {

            // EARLY FAILURE CHECK (no possible move)
            if (!hasAnyMovableEdge()) {
                System.out.println("******** GAME OVER ********");
                System.out.println("FAILURE");
                return;
            }

            System.out.println("\n> TURN " + turn + ":");

            try {
                // Optional: View Surfaces
                menu.handleViewSurfaces();

                // Stage 1: Rolling
                System.out.println("---> TURN " + turn + " FIRST STAGE:");
                menu.handleRollingStage();

                System.out.println("The new state of the box grid:");
                System.out.println(boxGrid.toPrettyString());

                // Stage 2: Opening & Tools
                System.out.println("---> TURN " + turn + " SECOND STAGE:");
                menu.handleToolStage();

                System.out.println("The new state of the box grid:");
                System.out.println(boxGrid.toPrettyString());

            } catch (UnmovableFixedBoxException e) {
                System.out.println(e.getMessage());
                System.out.println("Turn is wasted!");
            } catch (EmptyBoxException e) {
                System.out.println("BOX IS EMPTY! " + e.getMessage());
                System.out.println("Turn is wasted!");
            } catch (BoxAlreadyFixedException e) {
                System.out.println(e.getMessage());
                System.out.println("Turn is wasted!");
            } catch (Exception e) {
                // Keep output clean and tester-friendly
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Turn is wasted!");
            } finally {
                // Reset moved status for next turn no matter what
                boxGrid.resetMovedThisTurn();
            }
        }

        endGame();
    }

    /**
     * Returns true if there exists at least one edge box that is NOT a FixedBox.
     * If all edge boxes are FixedBox, rolling cannot be performed => FAILURE possible.
     */
    private boolean hasAnyMovableEdge() {
        for (int i = 1; i <= BoxGrid.SIZE; i++) {
            // top row
            if (!(boxGrid.getBox(new Position(1, i)) instanceof FixedBox)) return true;
            // bottom row
            if (!(boxGrid.getBox(new Position(BoxGrid.SIZE, i)) instanceof FixedBox)) return true;
            // left column
            if (!(boxGrid.getBox(new Position(i, 1)) instanceof FixedBox)) return true;
            // right column
            if (!(boxGrid.getBox(new Position(i, BoxGrid.SIZE)) instanceof FixedBox)) return true;
        }
        return false;
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

        System.out.println("******** GAME OVER ********");
        System.out.println();
        System.out.println("The final state of the box grid:");
        System.out.println(boxGrid.toPrettyString());
        System.out.println("THE TOTAL NUMBER OF TARGET LETTER \"" + targetLetter + "\" IN THE BOX GRID --> " + count);
        System.out.println();
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

        // Probabilities: 85% Regular, 5% Fixed, 10% Unchanging
        if (roll < 0.85) {
            // RegularBox: 75% chance of containing a tool
            SpecialTool tool = (random.nextDouble() < 0.75) ? generateRandomTool() : null;
            return new RegularBox(surfaces, tool);
        } else if (roll < 0.90) {
            // FixedBox (5%)
            return new FixedBox(surfaces);
        } else {
            // UnchangingBox (10%) - guaranteed to contain a tool
            return new UnchangingBox(surfaces, generateRandomTool());
        }
    }

    private char[] generateValidSurfaces() {
        while (true) {
            char[] s = new char[6];
            Map<Character, Integer> counts = new HashMap<>();
            boolean valid = true;

            for (int i = 0; i < 6; i++) {
                char ch = LETTERS[random.nextInt(LETTERS.length)];
                s[i] = ch;
                counts.put(ch, counts.getOrDefault(ch, 0) + 1);

                // Max 2 of same letter (only at the start of the game)
                if (counts.get(ch) > 2) {
                    valid = false;
                    break;
                }
            }
            if (valid) return s;
        }
    }

    private SpecialTool generateRandomTool() {
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
    // GENERICS HELPER METHOD
    // =============================================================

    private <T extends SpecialTool> void useAcquiredTool(T tool, BoxGrid grid, Position pos) throws Exception {
        if (tool != null) {
            tool.use(grid, pos, this.targetLetter);
        }
    }

    // =============================================================
    // MENU INNER CLASS
    // =============================================================

    private class Menu {

        void handleViewSurfaces() {
            int choice = InputHelper.readInt("---> Do you want to view all surfaces of a box? [1] Yes or [2] No? ", 1, 2);
            if (choice == 1) {
                Position p = InputHelper.readPosition("Please enter the location of the box you want to view: ");
                Box b = boxGrid.getBox(p);
                System.out.println(b.toNetString());
            }
            System.out.println("Continuing to the first stage...");
        }

        void handleRollingStage() throws UnmovableFixedBoxException {
            while (true) {
                Position edgePos = InputHelper.readPosition("Please enter the location of the edge box you want to roll: ");

                if (!edgePos.isEdge()) {
                    System.out.println("INCORRECT INPUT: The chosen box is not on any of the edges.");
                    continue;
                }

                List<Direction> options = boxGrid.allowedInwardDirections(edgePos);
                Direction selectedDir;

                if (options.size() == 1) {
                    selectedDir = options.get(0);
                    System.out.println("The chosen box is automatically rolled " + selectedDir + ".");
                } else {
                    System.out.println("The chosen box can be rolled to either [1] " + options.get(0) + " or [2] " + options.get(1) + ": ");
                    int choice = InputHelper.readInt("", 1, 2);
                    selectedDir = options.get(choice - 1);
                }

                boxGrid.rollFromEdge(edgePos, selectedDir);
                return;
            }
        }

        void handleToolStage() throws EmptyBoxException, BoxAlreadyFixedException, UnmovableFixedBoxException, Exception {
            while (true) {
                Position openPos = InputHelper.readPosition("Please enter the location of the box you want to open: ");

                if (!boxGrid.wasMovedThisTurn(openPos)) {
                    System.out.println("INCORRECT INPUT: The chosen box was not rolled during the first stage.");
                    continue;
                }

                Box box = boxGrid.getBox(openPos);
                SpecialTool tool = box.openAndTakeContent();

                if (tool == null) {
                    throw new EmptyBoxException("Continuing to the next turn...");
                }

                System.out.println("The box on location " + openPos + " is opened. It contains a SpecialTool --> " + tool.getName());

                Position targetPos;

                if (tool instanceof MassRowStamp) {
                    int row = InputHelper.readInt("Please enter the row number to stamp (1-8): ", 1, BoxGrid.SIZE);
                    targetPos = new Position(row, 1);
                } else if (tool instanceof MassColumnStamp) {
                    int col = InputHelper.readInt("Please enter the column number to stamp (1-8): ", 1, BoxGrid.SIZE);
                    targetPos = new Position(1, col);
                } else {
                    targetPos = InputHelper.readPosition("Please enter the location of the box to use this SpecialTool: ");
                }

                useAcquiredTool(tool, boxGrid, targetPos);
                break;
            }
        }
    }
}
