import java.util.ArrayList;

/**
 * This class represents the stack of States necessary to reach the 
 * solution state
 * @author James Wooten
 * @version 1/0
 */
public class SolutionPathStack {
    private ArrayList<State> solutionStack = new ArrayList<>();
    private State top = null;

   
   /** 
    * Push the passed State onto the Solution Path Stack and set as Top value
    * @param s the State to be pushed onto the Solution Path Stack
    */
   public void push(State s) {
       this.solutionStack.add(s);
       this.top = s;
   }

   
   /** 
    * Remove and return the top State on the Solution Path Stack
    * @return State the top State on the Solution path
    */
   public State pop() {
       //If Empty, print message and return null
       if(this.isEmpty()) {
           System.out.println("Stack is empty");
           return null;
       }
       State temp = this.top;
       this.solutionStack.remove(this.solutionStack.size() - 1);

       //If the only item removed, set top null
       if(this.solutionStack.size() == 0) {
           this.top = null;
           return temp;
       }

       this.top = this.solutionStack.get(this.solutionStack.size() - 1);
       return temp;
   }

   
   /** 
    * Boolean to check if Stack is empty
    * @return boolean returns true if stack is empty
    */
   public boolean isEmpty() {
       return this.solutionStack.isEmpty();
   }
}
