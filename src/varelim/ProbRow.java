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
                this.variables = parents;
                this.variables.set(0, node);
	}

	/**
	 * Check whether two have ProbRows have the same parents
	 * @param pr a probability row
	 * @return True if this probRow and pr have the same parents
	 */
	public boolean sameParentsValues(ProbRow pr) {
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

    void removeVariable(Table tab, int removeIndex) {
        node = tab.getNode();
        parents = tab.getParents();
        
        values.remove(removeIndex);
    }

    void setProb(double prob) {
        this.prob = prob;
    }
    
    @Override
    public ProbRow clone() throws CloneNotSupportedException{
        ProbRow copy = (ProbRow)super.clone();
        if(this.node != null)
            copy.node = this.node.clone();
        else 
            copy.node = null;
        copy.parents = (ArrayList) this.parents.clone();
        copy.values = (ArrayList) this.values.clone();
        return copy;
    }

    public void setNode(Variable node) {
        this.node = node;
    }

    public void setParents(ArrayList<Variable> parents){
        this.parents = parents;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    /**
     * multiply the probability of this row with the given other row
     * and appends the corresponding parents and values
     * 
     * @param other
     * @return
     * @throws CloneNotSupportedException 
     */
    public ProbRow multiply(ProbRow other) throws CloneNotSupportedException {
        ProbRow newRow = this.clone();
        
        ArrayList<Variable> newParents = newRow.getParents();
        newParents.addAll((ArrayList) other.getVariableList().clone());
        ArrayList<String> newValues = newRow.getValues();
        newValues.addAll((ArrayList) other.getValues().clone());
        double newProb = newRow.getProb() * other.getProb();
        
        newRow.setParents(newParents);
        newRow.setProb(newProb);
        newRow.setValues(newValues);
        
        return newRow;
    }
/*
    public boolean sameMatchingVariableValues(ProbRow other) {
        ArrayList<String> theseVarNames = (ArrayList)this.getVariableNameList().clone();
        ArrayList<String> otherVarNames = (ArrayList)other.getVariableNameList().clone();
        ArrayList<Integer> theseMatchingVariableIndices = new ArrayList<>();
        ArrayList<Integer> otherMatchingVariableIndices = new ArrayList<>();
        
        for(int i = 0; i < theseVarNames.size(); i++){
            String thisVarName = theseVarNames.get(i);
            for(int j = 0; j < otherVarNames.size(); j++){
                String otherVarName = otherVarNames.get(i);
                if(thisVarName.equals(otherVarName)){
                    theseMatchingVariableIndices.add(i);
                    otherMatchingVariableIndices.add(j);
                }
            }
        }
        ArrayList<String> theseValues = (ArrayList)this.getValues().clone();
        ArrayList<String> otherValues = (ArrayList)other.getValues().clone();
        
        for(int i = 0; i < theseMatchingVariableIndices.size(); i++){
            int thisMatchingParentIdx = theseMatchingVariableIndices.get(i);
            int otherMathingParentIdx = otherMatchingVariableIndices.get(i);
            String thisMatchingParentValue = theseValues.get(thisMatchingParentIdx);
            String otherMatchingParentValue = otherValues.get(otherMathingParentIdx);
            if(!thisMatchingParentValue.equals(otherMatchingParentValue))
                return false;
        }
        return true;
    }

   
    public boolean intersectsVariable(ProbRow other) {
        ArrayList<String> theseVars = this.getVariableNameList();
        ArrayList<String> otherVars = other.getVariableNameList();
        
        for(String thisVar : theseVars)
            if(otherVars.contains(thisVar))
                return true;
        return false;
    }
    */
    /**
     * returns a full list of variables included in this ProbRow
     * including the node and the parents
     * 
     * @return 
     */
    private ArrayList<Variable> getVariableList(){
        ArrayList<Variable> variableList = new ArrayList<>();
        if(this.node != null)
            variableList.add(this.node);
        for(Variable parent : this.parents)
            variableList.add(parent);
        return variableList;
    }
    
    /**
     * returns a full list of variables included in this ProbRow
     * including the node and the parents
     * represented as strings
     * 
     * @return 
     */
    private ArrayList<String> getVariableNameList(){
        ArrayList<String> variableList = new ArrayList<>();
        if(this.node != null)
            variableList.add(this.node.getName());
        for(Variable parent : this.parents)
            variableList.add(parent.getName());
        return variableList;
    }

    /**
     * returns true if any duplicate variables have different values
     * false otherwise
     * also removes any duplicate variables from the values and parents list
     * 
     * @return 
     */
  
    /**
     * returns a list of index pairs corresponding to the column numbers
     * of any duplicate variables.
     * the list is considered to start with the node, then the parents
     * 
     * @return 
     */
    private ArrayList<int []> getDuplicateVariableIndices() {
        ArrayList<String> variableNames = this.getVariableNameList();
        ArrayList<int []> duplicateVariableIndices = new ArrayList<>();
        for(int i = 0;  i < variableNames.size() - 1; i++){
            for(int j = i + 1; j < variableNames.size(); j++){
                String firstVarName = variableNames.get(i);
                String secondVarName = variableNames.get(j);
                if(firstVarName.equals(secondVarName))
                    duplicateVariableIndices.add(new int [] {i, j});
            }
        }
        return duplicateVariableIndices;
    }

    /**
     * removes the 2nd element of each pair that indicates duplicate variables
     * 
     * @param duplicateVariableIndices 
     */


    /**
     * returns true if this ProbRow does not comply with the information
     * given about an observed variable
     * 
     * @param name
     * @param value
     * @return 
     */
    boolean isIrrelevantRow(String name, String value) {
        int varIdx = this.getVariableIndex(name);
        
        if(this.values.get(varIdx).equals(value))
            return false;
        return true;
    }

    /**
     * returns an index representation of the variables contained in this ProbRow
     * considered to start with the node, then the parents
     * 
     * @param name
     * @return 
     */
    int getVariableIndex(String name) {
        if(node != null && node.getName().equals(name))
            return 0;
        for(int i = 0; i < this.parents.size(); i++)
            if(this.parents.get(i).getName().equals(name))
                return i + 1;
        throw new IllegalArgumentException(name + " is not contained in this ProbRow: " + this.getVariableNameList().toString());
    }

    boolean sameMatchingVariableValues(ProbRow other) {
        ArrayList<Variable> theseVars = this.getVariableList();
        ArrayList<Variable> otherVars = other.getVariableList();
        ArrayList<String> theseValues = this.getValues();
        ArrayList<String> otherValues = other.getValues();
        
        ArrayList<Integer> removeIdxs = new ArrayList<>();
        
        for(int i = 0; i < theseVars.size(); i++){
            Variable thisVar = theseVars.get(i);
            String thisValue = theseValues.get(i);
            for(int j = 0; j < otherVars.size(); j++){
                Variable otherVar = otherVars.get(j);
                String otherValue = otherValues.get(j);
                if(thisVar.equals(otherVar) && !thisValue.equals(otherValue))
                    return false;
            }
        }
        this.removeDuplicateVariables(removeIdxs);
        
        return true;
    }

    private void removeDuplicateVariables(ArrayList<Integer> removeIdxs) {
        for(int i = removeIdxs.size(); i >= 0; i--){
            this.values.remove(i);
            this.variables.remove(i);
        }
    }
    
    
}