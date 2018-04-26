/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package varelim;

import java.util.ArrayList;

/**
 *
 * @author Melle
 */
public class VariableEliminator {
    private Variable            targetVar;
    private ArrayList<Variable> observedVars;
    private Networkreader       network;
    private double              probability;

    public VariableEliminator(Variable Q, ArrayList<Variable> O, Networkreader network){
        this.targetVar = Q;
        this.observedVars = O;
        this.network = network;
    }
    
    public void eliminateVariables(){
        
    }
    
    
    public double getProbability(){
        return this.probability;
    }
    
    public void setTarget(Variable target){
        this.targetVar = target;
    }
    
    public void setObserved(ArrayList<Variable> observed){
        this.observedVars = observed;
    }
    
    public void setNetwork(Networkreader network){
        this.network = network;
    }
    
    public Variable getTarget(){
        return this.targetVar;
    }
    
    public ArrayList getObserved(){
        return this.observedVars;
    }
    
    public Networkreader getNetwork(){
        return this.network;
    }
    
    @Override
    public String toString(){
        return "Given: " + this.observedVars + "\n"
                + "Target: " + this.targetVar.getName() + "\n"
                + "Probability: " + this.probability; 
    }
}
