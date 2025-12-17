package app;

import puzzle.BoxPuzzle;

/** 
 * Main Application Class.
 * Initializes the puzzle and starts the game.
 */
public class BoxPuzzleApp {
    public static void main(String[] args) {

        // The main method should only initalize a BoxPuzzle object.
        BoxPuzzle game = new BoxPuzzle();

        // Starts the game logic.
        game.play();

    }

}
