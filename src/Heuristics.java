/**
* This class represents the heuristics used by the program to determine the best course of action to take.
* The first Heuristic is the Manhattan heuristic, which bases the h(n) value on the cumulative number of moves
* necessary by each available tile to reach the goal orientation. The second heuristic, which is referred to as
* Hamming heuristic within this program simply counts how many of the tiles are in the incorrect location.
* The final value used for comparison is obtained through f(n) = g(n) + h(n) where g(n) is the steps taken and
* h(n) is the value of the chosen heuristic.
* @author James Wooten
* @version 1.0
*/
public class Heuristics {
    
    
    /** 
     * Calculates the cumulative Manhattan score of a grid based on the current orientation
     * @param current the Current game state
     * @param goal the Goal game state
     */
    public void calculateManhattan(State current, State goal) {
        int hScore = 0;
        
        //Loops through the board and checks the value for each tile
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                hScore += getTileManhattan(i, j, current, goal);
            }
        }
        //Sets the h(n) and f(n) values
        current.setHScore(hScore);
        current.setFScore(current.getGScore() + current.getHScore());
        
    }


    
    /** 
     * Used by the cumulative Manhattan method to calculate the value for each tile.
     * z = |y2 - y1| + |x2 - x1|, where z is the individual tile score and the goal state is given
     * the y2 and x2 coordinates. 
     * @param i the Current State's row
     * @param j the Current State's column
     * @param current the Current game state
     * @param goal the Goal game state
     * @return int the score for the individual tile
     */
    private int getTileManhattan(int i, int j, State current, State goal) {
        int value = current.getCurrentGrid()[i][j];
        int hScore = 0;

        if(value == 0) {
            return 0;
        }

        //Calculate the score based on the difference in x and y values between goal and current
        for(int k = 0; k < 3; k++) {
            for(int l = 0; l < 3; l++) {
                if(goal.getCurrentGrid()[k][l] == value) {
                    hScore = Math.abs(l - j) + Math.abs(k - i);
                    return hScore;
                }
            }
        }
        return hScore;
    }

    
    /** 
     * Calculates how many tiles are out of order for the Hamming heuristic
     * @param current the current game state
     * @param goal the goal game state
     */
    public void calculateHamming(State current, State goal) {
        int hScore = 0;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(current.getCurrentGrid()[i][j] != goal.getCurrentGrid()[i][j]) {
                    hScore++;
                }
            }
        }
        current.setHScore(hScore);
        current.setFScore(current.getGScore() + current.getHScore());
    }
    
    
}
