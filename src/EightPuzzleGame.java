
/*
* This class represents an 8-Puzzle Game. The goal of the game is to move the available tiles
* Up, Down, Left, or Right and swapping locations with the empty (represented by a zero) grid to
* reach the "Goal State". This class keeps track of the nodes generated, stored in the frontier, and expanded.
* The expansion of a node represents moving the tiles in a manner that sets the current layout to that of the
* expanded node. The path to the solution is stored in a stack to allow for visualizing the path. 
*/

public class EightPuzzleGame {
    private State current;
    private State start;
    private State goal;
    private int numNodesGenerated = 0;
    private int numNodesExpanded = 0;
    private String heuristicSelected = "";
    private Heuristics heuristics = new Heuristics();
    private PriorityQueue priorityQueue = new PriorityQueue();
    private SolutionPathStack solutionPath = new SolutionPathStack();
    private StateVisitedHashSet stateVisited = new StateVisitedHashSet();

    /**
     * Runs the 8-Puzzle Game
     * Gets the user input and then solves based on the Manhattan Heuristic, then resets
     * and solves based on the Hamming Heuristic
     */
    public void run() {


        //Get and set up Start and Goal Grid 
        System.out.println("Please enter the current state");
        this.start = new State();

        System.out.println("Please enter the goal state");
        this.goal = new State();

        this.current = new State(this.start);
        this.current.setGScore(0);

        this.current.setParent(null);

        System.out.println();
        System.out.println("-----Manhattan-----");

        //Run Game using Manhattan Heuristic
        runGame("Manhattan");

        //Reset Values to allow re-run
        reset();
        
        //Reset as root State
        this.current = new State(this.start);
        this.current.setGScore(0);
        this.current.setParent(null);

        System.out.println();
        System.out.println("-----Hamming-----");

        //Run Game using Hamming Heuristic 
        runGame("Hamming");




    }


    /**
     * Resets the game to the initial values
     */
    private void reset() {
        this.numNodesExpanded = 0;
        this.numNodesGenerated = 0;
        this.priorityQueue = new PriorityQueue();
        this.solutionPath = new SolutionPathStack();
        this.stateVisited = new StateVisitedHashSet();
    }

    
    /** 
     * Runs the 8-Puzzle Game. The user selected Heuristic is taken in as 
     * a String and used for calculation of the g(n) and f(n) formulas.
     * The user input for the Current and Goal grids are also taken and then 
     * solved for the optimal path from Current -> Goal State.
     * @param hFunction The user selected Heuristic used in finding the solution
     */
    private void runGame(String hFunction) {

        //User selected Heuristic
        this.heuristicSelected = hFunction;

        //Choose g(n) based on user selection
        if(this.heuristicSelected.equals("Manhattan")) {
            heuristics.calculateManhattan(current, goal);
        } else if(this.heuristicSelected.equals("Hamming")){
            heuristics.calculateHamming(current, goal);
        } else {
            System.out.println("Heuristic Not Supported");
            System.exit(-3);
        }

        //Add to Visited States list to prevent loops
        this.stateVisited.add(this.current);

        /*
        * Iterate through rounds of Node expansion and generation of the 
        * expanded node's children. If not found by the RoundCounter Loop, it
        * will display that there is not solution found and exit the program
        */ 
        int roundCounter = 0;

        while(current.getHScore() != 0) {
            if(roundCounter == 10000000) {
                System.out.println("Unable to find a solution");
                System.out.println("Number of Nodes Expanded: " + this.numNodesExpanded);
                System.out.println("Number of Nodes in Frontier: " + this.numNodesGenerated);
                System.exit(-1);
            }
            findNextMoves(current);
            expandNode();
            roundCounter++;
        }

        /*
        * Moves back through the parent nodes (starting with current)
        * to find the best solution path
        */
        getSolutionPath(current);

        //Prints the Number of Nodes expanded, Frontier Count, and Generated from Start to solution 
        System.out.println("Solution Found!");
        System.out.println("Minimum Number of Moves: " + this.current.getGScore());
        System.out.println("Number of Nodes Expanded: " + this.numNodesExpanded);
        System.out.println("Number of Nodes in Frontier: " + this.priorityQueue.getPriorityQueue().size());

        int stateCounter = 0;

        while(!this.solutionPath.isEmpty()) {
            System.out.println("State: " + stateCounter);
            this.solutionPath.pop().printGrid();
            stateCounter++;
        }
    }

    
    /** 
     * Pushes the Current state onto the SolutionPath Stack
     * and recursively adds each parent up to the root node
     * @param current the Solution State
     */
    private void getSolutionPath(State current) {
        // Add current to queue
        this.solutionPath.push(current);

        if(current.getParent() == null) {
            return;
        }

        getSolutionPath(current.getParent());
    }

    
    /** 
     * Finds the available moves on the graph depending on where
     * the Zero value is located on the current grid and generates
     * children in each direction
     * @param current the Current State to check available moves
     */
    private void findNextMoves(State current) {

        //Get X and Y coords for Zero value
        int y = this.current.getZeroGridLocation()[0];
        int x = this.current.getZeroGridLocation()[1];
        
        //if(y!= 0) Move Up
        if(y != 0) {
            generateChild(current, "Up");
        }
        
        //if(y!= 2) Move Down
        if(y != 2) {
            generateChild(current, "Down");
        }

        //if(x!= 0) Move Left
        if(x != 0) {
            generateChild(current, "Left");
        }

        //if(x!= 2) Move Right
        if(x != 2) {
            generateChild(current, "Right");
        }
    
    }


    
    /** 
     * Creates a new State node and adjusts the Child's grid from the intial
     * layout of the parents to the new grid with the zero moved in the input direction.
     * Checks if the State has already been visited and discards if so.
     * @param parent the Child's parent State
     * @param direction The direction to move the zero value
     */
    private void generateChild(State parent, String direction) {

        //Create the child node
        State child = new State(parent);

        //Move the child node's grid in the correct direction
        child.moveGrid(direction);

        //Add the child to the priority queue if not visited
        if(this.stateVisited.contains(child)) {
            return;
        }

        //Add the child to the parent node's Arraylist
        parent.getChildren().add(child);

        //Calculate the h(n)
        if(this.heuristicSelected.equals("Manhattan")) {   
            heuristics.calculateManhattan(child, goal);
        } else if(this.heuristicSelected.equals("Hamming")){
            heuristics.calculateHamming(child, goal);
        } else {
            System.out.println("Heuristic Not Supports");
            System.exit(-3);
        }
        child.findZero();
       
        //Add to the visited states list
        this.stateVisited.add(child);

        //Increase Node generate count
        this.numNodesGenerated++;

        //Add to priority queue
        this.priorityQueue.enqueue(child);

    }

    
    /** 
     * Takes the front state from the priority queue and sets to the
     * current node 
     */
    private void expandNode() {

        this.current = this.priorityQueue.poll();
        this.numNodesExpanded++;
        
    }
}
