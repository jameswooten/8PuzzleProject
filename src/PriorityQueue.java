import java.util.ArrayList;

/**
 * This class represents the Frontier of the A* search. The generated States are stored
 * in ascending order to their f(n) value.
 * @author James Wooten
 * @version 1.0
 */
public class PriorityQueue {
    private ArrayList<State> priorityQueue = new ArrayList<>();


    
    /** 
     * Adds the state to the Priority Queue based on the value of f(n) in ascending order
     * @param s the State to be added to the Priority Queue
     */
    public void enqueue(State s) {
        if(this.priorityQueue.isEmpty()) {
            priorityQueue.add(s);
        } else {
            //Iterate through each of the items in the Queue and compare f(n)
            for(int i = 0; i < priorityQueue.size(); i++) {
                if(s.getFScore() < priorityQueue.get(i).getFScore()) {
                    priorityQueue.add(i, s);
                    return;
                }
            }
            this.priorityQueue.add(s);
            

           
        }
    }

    
    /** 
     * Returns the current Priority Queue
     * @return ArrayList<State> the current Priority Queue
     */
    public ArrayList<State> getPriorityQueue() {
        return this.priorityQueue;
    }

    
    /** 
     * Removes the State at the passed index from the Priority Queue
     * @param i the index location to be removed
     */
    public void dequeue(int i) {
        this.getPriorityQueue().remove(i);
    }

    
    /** 
     * Retrieves then removes the front State of the Priority Queue
     * @return State The front State in the Priority Queue
     */
    public State poll() {
        //If empty, return null
        if(this.priorityQueue.isEmpty()) {
            return null;
        } else {
            State temp = this.priorityQueue.get(0);
            this.priorityQueue.remove(0);
            return temp;
        }
    }

    
    /** 
     * Return the front State from the Priority Queue
     * @return State The front State in the Priority Queue 
     */
    public State peek() {
        return this.priorityQueue.get(0);
    }
}
