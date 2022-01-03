import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class represents a State of the 8-Puzzle Grid.
 * Each state has a two dimensional array representing the 
 * game board as well as an array with the stored location
 * of the zero (or blank) tile. Each state also has values for
 * g(n), h(n), and f(n). If the State is not the root node, then
 * the parent is stored and subsequent moves from the State are stored
 * as children. 
 * @author James Wooten
 * @version 1.0
 */
public class State {
    private int[][] currentGrid;
    private State parent;
    private int gScore;
    private int hScore;
    private int fScore;
    private int[] zeroGridLocation;
    private ArrayList<State> children = new ArrayList<>();

    
    //Generic Constructor
    public State() {
        this.currentGrid = new int[3][3];
        this.zeroGridLocation = new int[2];

        //Parent is null - root object
        this.parent = null;

        //Gets Grid from User
        askUserGrid();
        this.gScore = 0;
    }

    /**
     * Constructor that passes in the parent node of the state.
     * Used when a State is generated from a parent.
     * @param parent the parent of the new State
     */
    public State(State parent) {
        this.currentGrid = new int[3][3];
        this.zeroGridLocation = new int[2];

        //Copy over parent's grid
        this.setCurrentGrid(parent.getCurrentGrid());
        this.findZero();

        //Set parent
        this.parent = parent;
        this.gScore = parent.getGScore() + 1;
    }

    /**
     * Asks for the grid from the user and checks for validity before
     * converting into a two dimensional array
     */
    public void askUserGrid() {
        Scanner scanner1 = new Scanner(System.in);
        boolean check = true;

        int[] userInput = new int[9];

        //Loop to ask for input until correct layout given
        do {
            check = true;

            for(int i = 0; i < 9; i++) {
            
                if(checkTypeInput(scanner1)) {
                    userInput[i] = scanner1.nextInt();
                } else {
                    check = false;
                    break;
                }
                 
                //Check that no duplicate or out of range has been entered
                if(!checkDuplicates(i, userInput) || !checkUserInputRange(i, userInput)) {
                    check = false;
                    break;
                }
            }

            // //Comsume Newline
            // scanner1.nextLine();
            
        } while (!check);
        
        //Convert grid to two dimensional and get zero location
        convertGrid(userInput);
        findZero();
    }

    
    /** 
     * Converts the one dimensional array into a two dimensional array
     * to represent the game board
     * @param arr the user input array
     */
    private void convertGrid(int[] arr) {
        int count = 0;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                this.currentGrid[i][j] = arr[count];
                count++;
            }
        }
    }

    /**
     * Find the location of the zero on the game board and saves
     */
    public void findZero() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(this.currentGrid[i][j] == 0) {
                    this.zeroGridLocation[0] = i;
                    this.zeroGridLocation[1] = j;
                }
            }
        }
    }

    
    /** 
     * Checks that the user has entered an int
     * @param s the Scanner for user input
     * @return boolean returns true if correct type
     */
    private boolean checkTypeInput(Scanner input) {
        //Check if the Scanner's next item is an int
        if(input.hasNextInt()) {
            return true;
        } else {
            System.out.println("Invalid Input - Must be an Integer");
            input.nextLine();
            System.out.println("Please Re-Enter.");
            return false;
        }
    }
    
    /** 
     * Checks that the user has entered an int between 0-8
     * @param i the array location on original one dimensional array
     * @param arr the user input array
     * @return boolean returns true if within 0-8 range
     */
    private boolean checkUserInputRange(int i, int[] arr) {
        for(int j = 0; j < 3; j++) {
            if(arr[i] < 0 || arr[i] > 8) {
                System.out.println("Invalid Input - Must be an Integer between 0-8");
                System.out.println("Please Re-Enter.");
               return false;
            }
        }
        return true;
    }

    
    /** 
     * Checks that the user has not entered a duplicate int
     * @param i the array location on original one dimensional array
     * @param arr the user input array
     * @return boolean returns true if not a duplicate value
     */
    private boolean checkDuplicates(int i, int[] arr) {
        //true if first value
        if(i == 0) {
            return true;
        }

        for(int j = 0; j < i; j++) {
            if(arr[j] == arr[i]) {
                System.out.println("Invalid Input - You have entered a duplicate");
                System.out.println("Please Re-Enter.");
                return false;
            }
        }
        return true;
    }

    
    /** 
     * Deep copy current grid and return copy to prevent
     * reference errors
     * @return int[][] the deep copied array
     */
    public int[][] getCurrentGrid() {
        int[][] grid = new int[3][3];
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                grid[i][j] = this.currentGrid[i][j];
            }
        }
        return grid;
    }

    
    /** 
     * Deep copies input array into State's current grid
     * @param grid the array to copy into current grid
     */
    public void setCurrentGrid(int[][] grid) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                this.currentGrid[i][j] = grid[i][j];
            }
        }
    }

    
    /** 
     * Shifts the zero location of the current grid in the direction
     * of the input String and swaps values, then sets current grid to 
     * new arrangement
     * @param direction the direction to swap the zero value
     */
    public void moveGrid(String direction) {
        this.findZero();
        int y = this.getZeroGridLocation()[0];
        int x = this.getZeroGridLocation()[1];
        int[][] newGrid = this.getCurrentGrid();
        int temp;

        switch (direction) {
            case "Up":
                temp = newGrid[(y - 1)][x];
                newGrid[y][x] = temp;
                newGrid[(y - 1)][x] = 0;
                break;
            case "Down":
                temp = newGrid[(y + 1)][x];
                newGrid[y][x] = temp;
                newGrid[(y + 1)][x] = 0;
            break;
            case "Left":
                temp = newGrid[y][(x-1)];
                newGrid[y][x] = temp;
                newGrid[y][(x - 1)] = 0;
            break;
            case "Right":
                temp = newGrid[y][(x + 1)];
                newGrid[y][x] = temp;
                newGrid[y][(x + 1)] = 0;
            break;
            default:
                break;
        }

        this.setCurrentGrid(newGrid);

    }

    /**
     * Prints the current grid into formatted orientation
     */
    public void printGrid() {
        System.out.println(" -------");
        System.out.print("| ");

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
               System.out.print(this.currentGrid[i][j]);
               if(j < 2) {
                   System.out.print(" ");
               }
            }
            System.out.println(" |");
            if(i < 2) {
                System.out.print("| ");
            }
        }
        System.out.println(" -------");
    }

    
    /** 
     * Getter for g(n) vlaue
     * @param gScore
     */
    public void setGScore(int gScore) {
        this.gScore = gScore;
    }

    
    /** 
     * Getter for h(n) value
     * @return int
     */
    public int getHScore() {
        return hScore;
    }

    
    /** 
     * Setter for h(n) value
     * @param hScore
     */
    public void setHScore(int hScore) {
        this.hScore = hScore;
    }

    
    /** 
     * getter for g(n) value
     * @return int
     */
    public int getGScore() {
        return gScore;
    }

    
    /** 
     * Getter for Parent State
     * @return State
     */
    public State getParent() {
        return this.parent;
    }

    
    /** 
     * Setter for Parent State
     * @param parent the Parent state to set
     */
    public void setParent(State parent) {
        this.parent = parent;
    }

    
    /** 
     * Getter for f(n) value
     * @return int
     */
    public int getFScore() {
        return this.fScore;
    }

    
    /** 
     * Setter for f(n) value
     * @param i
     */
    public void setFScore(int i) {
        this.fScore = i;
    }
    
    /** 
     * Setter for children list
     * @param children the list of children to set
     */
    public void setChildren(ArrayList<State> children) {
        this.children = children;
    }

    
    /** 
     * Getter for children list
     * @return ArrayList<State> the list of children to get
     */
    public ArrayList<State> getChildren() {
        return this.children;
    }

    
    /** 
     * Setter for Zero location on grid
     * @param zeroGridLocation the location of zero to set
     */
    public void setZeroGridLocation(int[] zeroGridLocation) {
        this.zeroGridLocation = zeroGridLocation;
    }

    
    /** 
     * The Getter for Zero Location on grid
     * @return int[]
     */
    public int[] getZeroGridLocation() {
        return zeroGridLocation;
    }




}
