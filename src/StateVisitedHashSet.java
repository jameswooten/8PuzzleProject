import java.util.HashSet;
import java.util.Iterator;

/**
 * This class represents the stored set of previously visited states
 * @author James Wooten
 * @version 1.0
 */
public class StateVisitedHashSet extends HashSet<State> {
   
    
    
    /** 
     * Checks if the hash set contains the input state's current grid 
     * and compares to see if there are any differences
     * @param current
     * @return boolean
     */
    public boolean contains(State current) {
        Iterator<State> it = this.iterator();

        while(it.hasNext()) {
            int[][] temp = it.next().getCurrentGrid();
            int numSameSquare = 0;
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    if(temp[i][j] == current.getCurrentGrid()[i][j]) {
                        numSameSquare++;
                    }
                }
            }
            if(numSameSquare == 9) {
                return true;
            }
        }
        return false;
    }
}
