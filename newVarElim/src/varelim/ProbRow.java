package varelim;

import java.util.ArrayList;
/**
 * Represents a row of probabilities
 * 
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 */
public class ProbRow implements Cloneable {
    private double prob;
    private ArrayList<String> values;
    private Variable node;
    private ArrayList<Variable> parents;
    private ArrayList<Variable> variables;

    /**
     * Constructor
     * @param node a variable 
     * @param prob probability belonging to this row of values of the node
     * @param values values of all the variables (node+parents) in the row, of which the value of the node itself is always last
     * @param parents the parent variables
     */
    public ProbRow(Variable node, double prob, ArrayList<String> values, ArrayList<Variable> parents) {
            this.prob = prob;
            this.values = values;
            this.node = node;
            this.parents = parents;
            this.variables = new ArrayList<>();
            this.variables.addAll(parents);    
            this.variables.add(node);
    }

    /**
     * Check whether two have ProbRows have the same parents
     * @param pr a probability row
     * @return True if this probRow and pr have the same parents
     */
    public boolean sameVariableValues(ProbRow pr) {
            return this.values.equals(pr.values);
    }

    /**
     * Getter of the probability
     * @return the probability of the node.
     */
    public double getProb() {
            return prob;
    }

    /**
     * Getter of the node.
     * @return node given the probabilities.
     */
    public Variable getNode() {
            return node;
    }

    /**
     * Getter of the values.
     * @return ArrayList<String> of values of the probability row
     */
    public ArrayList<String> getValues() {
            return values;
    }

    /**
     * Transform probabilities to string.
     */
    public String toString() {
            String valuesString = "";
            for(int i = 0; i < values.size()-1; i++){
                    valuesString = valuesString + values.get(i) + ", ";
            }
            valuesString = valuesString + values.get(values.size()-1);
            return valuesString + " | " + Double.toString(prob);
    }

    /**
     * Getter of the parents.
     * @return the parents of the node given the probabilities.
     */
    public ArrayList<Variable> getParents(){
            return parents;
    }

    @Override
    public ProbRow clone() throws CloneNotSupportedException{
        ProbRow copy = (ProbRow) super.clone();
        if(this.hasNode())
            copy.node = this.node.clone();
        copy.parents = (ArrayList) this.parents.clone();
        copy.values = (ArrayList) this.values.clone();
        copy.variables = (ArrayList) this.variables.clone();
        return copy;
    }

    void setProb(double newProb) {
        this.prob = newProb;
    }

    public boolean hasNode(){
        return this.node != null;
    }

    void setVariables(ArrayList<Variable> variables) {
        this.variables = variables;
    }

    void setNode(Variable node) {
        this.node = node;
    }

    void setParents(ArrayList<Variable> parents) {
        this.parents = parents;
    }

    void removeValue(int i) {
        this.values.remove(i);
    }

    boolean sameMatchingVariableValues(ProbRow other) {
        for(int i = 0; i < this.variables.size(); i++)
            for(int j = 0; j < other.variables.size(); j++){
                Variable thisVar = this.variables.get(i);
                Variable otherVar = other.variables.get(j);
                if(thisVar.equalName(otherVar)){
                    String thisVal = this.values.get(i);
                    String otherVal = other.values.get(j);
                    if(!thisVal.equals(otherVal))
                        return false;
                }
                
            }
        return true;
    }

    ArrayList<Variable> getVariables() {
        return this.variables;
    }
    
    void multiply(ProbRow other) {
        ArrayList<Variable> extraVars = (ArrayList) other.getVariables().clone();
        ArrayList<String> extraValues = (ArrayList) other.getValues().clone();
        this.variables.addAll(extraVars);
        this.parents.addAll(extraVars);
        this.values.addAll(extraValues);
        this.prob = this.prob * other.prob;
    }

    String getCorrespondingValue(Variable var) {
        for(int i = 0; i < this.values.size(); i++)
            if(this.variables.get(i).equalName(var))
                return this.values.get(i);
        throw new IllegalArgumentException(var + " isn't contained in " + this.variables);
    }

    boolean containsVariable(Variable var) {
        for(Variable thisVar : this.variables)
            if(var.equalName(thisVar))
                return true;
        return false;
    }
}