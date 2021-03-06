import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;

public class Sudoku {
	
	public static GameGrid copyGameGrid(GameGrid grid) {
		GameGrid copy = null;
		if(grid instanceof RGameGrid) {
			copy = new RGameGrid(grid);
		} else if(grid instanceof XGameGrid) {
			copy = new XGameGrid(grid);
		}
		return copy;
	}

    /**
     * Print a game menu message to the console.
     */
    public static void printMenu() {
        System.out.print("\n" +
        		"1. Set field\n" +
        		"2. Clear field\n" +
                "3. Print game\n" +
        		"4. Print rank\n" +
        		"5. Solve game\n" +
                "6. Find all Solutions\n" +
        		"7. Save game\n" +
                "8. Exit\n\n" +
                "Select an action [1-8]: ");
    }

    /**
     * Read a single integer value from the console and return it.
     * This function blocks the program's execution until a user
     * entered a value into the command line and confirmed by pressing
     * the Enter key.
     * @return The user's input as integer or -1 if the user's input was invalid.
     */
    @SuppressWarnings("resource")
	public static int parseInput() {
        Scanner in = new Scanner(System.in);
        try {
        	int temp = in.nextInt();
            return temp;
        } catch (InputMismatchException missE) {
            in.next(); // discard invalid input
            in.close();
            return -1;
        }
    }   

    /**
     * Display a dialog requesting a single integer which is returned
     * upon completion.
     *
     * The dialog is repeated in an endless loop if the given input 
     * is not an integer or not within min and max bounds.
     *
     * @param msg: a name for the requested data.
     * @param min: minium accepted integer.
     * @param max: maximum accepted integer.
     * @return The user's input as integer.
     */
    public static int requestInt(String msg, int min, int max) {
        Objects.requireNonNull(msg);

        while(true) {
            System.out.print("Please provide " + msg + ": ");
            int input = parseInput();
            if (input >= min && input <= max) return input;
            else {
                System.out.println("Invalid input. Must be between " + min + " and " + max);
            }
        }
    }

    public static void main(String[] args) {
    	String filepath = "games/xsudoku3.sd";
    	if(args.length > 0)
    		filepath = args[0];
    	GameGrid grid = null;
    	if(filepath.charAt(6) == 'x')
    		grid = new XGameGrid(filepath);
    	else
    		grid = new RGameGrid(filepath);

        // TODO print the grid here
        System.out.println(grid);

        // TODO implement the game loop here
        boolean exit = false;
        while(!exit) {
        	printMenu();
        	int s = requestInt("selection", 1, 8);
        	switch(s) {
        		case 1: //set
        			int c = requestInt("column index", 1, 9) - 1;
        			int r = requestInt("row index", 1, 9) - 1;
        			int v = requestInt("value", 1, 9);
    				grid.setField(r, c, v);
    				System.out.println(grid);
        			break;
        		
        		case 2: //clear
        			c = requestInt("column index", 1, 9) - 1;
        			r = requestInt("row index", 1, 9) - 1;
        			grid.clearField(r, c);
        			System.out.println(grid);
        			break;
        			
        		case 3: //print
        			System.out.println(grid);
        			break;
        		
        		case 4: //print rank
        			System.out.println(Ranker.rankSudoku(grid));
        			break;
        			
        		case 5: //solve
        			GameGrid copy = copyGameGrid(grid);
        			boolean possible = Solver.solve(copy);
        			if(possible) {
        				System.out.println(copy);
        			} else {
        				System.out.println("No solution found");
        			}
        			break;
        			
        		case 6: //find all solutions
        			copy = copyGameGrid(grid);
        			ArrayList<GameGrid> solutions = Solver.findAllSolutions(copy);
        			for(int i = 0; i < solutions.size(); i++) {
        				System.out.println(solutions.get(i));
        			}
        			System.out.println(solutions.size() + " solution(s) found");
        			break;
        			
        		case 7: //save
        			System.out.println("Saved");
					IOUtils.saveToFile("games/sudokuSave.sd", grid);
					System.out.println(grid);
					break;
        		
        		case 8: //exit
        			System.out.println("Bye");
        			exit = true;
        			break;
        			
        		default:
        			System.out.println("Invalid input");
        			break;
        	}
        }
        
    }
}
