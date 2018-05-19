package varelim;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class to represent the Table Object consisting of probability rows
 * 
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 */

public class Table implements Cloneable {

    private ArrayList<ProbRow> table;
    private Variable node;
    private ArrayList<Variable> parents;
    private ArrayList<Variable> variables;

    /**
     * Constructor of the class.
     * @param table made out of probability rows (ProbRows)
     * @param node belonging to the current probability table
     * @param parents belonging to the current probability table
     */
    public Table(ArrayList<ProbRow> table, Variable node, ArrayList<Variable> parents) {
        this.table = table;
        this.node = node;
        this.parents = parents;
        this.variables = parents;
        this.variables = new ArrayList<>();
        this.variables.addAll(parents);  
        this.variables.add(node);
    }

    /**
     * Getter of the table made out of ProbRows
     * @return table
     */
    public ArrayList<ProbRow> getTable() {
        return table;
    }

     /**
     * Getter of the node that belongs to the probability table
     * @return the node
     */
    public Variable getNode() {
        return node;
    }

    /**
     * Getter of the parents that belong to the probability table
     * @return the parents
     */
    public ArrayList<Variable> getParents() {
        return parents;
    }

    /**
      * Gets the i'th element from the ArrayList of ProbRows
      * @param i index
      * @return i'th ProbRow in Table
      */
    public ProbRow get(int i) {
        return table.get(i);
    }

    /**
     * Returns the size of the Table (amount of probability rows)
     * @return size of Table
     */
    public int size() {
        return table.size();
    }

    void normalize() {
        double totalProb = 0;
        for(ProbRow row : this.table){
            totalProb += row.getProb();
        }
        for(ProbRow row : this.table){
            double newProb = row.getProb() / totalProb;
            row.setProb(newProb);
        }
    }

    @Override
    public Table clone() throws CloneNotSupportedException{
        Table copy = (Table) super.clone();
        if(this.hasNode())
            copy.node = this.node.clone();
        copy.parents = (ArrayList) this.parents.clone();
        copy.table = (ArrayList) this.table.clone();
        copy.variables = (ArrayList) this.variables;
        return copy;
    }

    public boolean hasNode(){
        return this.node != null;
    }

    /**
     * removes a single instance of the given variable from the table and its
     * ProbRows
     * @param removeName 
     */
    public void removeVariable(String removeName){
        for(int i = 0; i < this.variables.size(); i++){
            String varName = this.variables.get(i).getName();
            if(varName.equals(removeName)){
                this.variables.remove(i);
                if(this.hasNode() && this.node.getName().equals(removeName))
                    this.node = null;
                else
                    for(int j = 0; j < this.parents.size(); j++){
                        String parentName = this.parents.get(j).getName();
                        if(parentName.equals(removeName)){
                            this.parents.remove(j);
                            break;
                        }
                    }
                for(ProbRow row : this.table){
                    row.setVariables(this.variables);
                    row.setNode(this.node);
                    row.setParents(this.parents);
                    row.removeValue(i);
                }  
                break;
            }
        }
    }

    boolean containsVariable(String varName) {
        for(Variable var : this.variables)
            if(var.getName().equals(varName))
                return true;
        return false;
    }

    void multiply(Table other) throws CloneNotSupportedException {
        ArrayList<ProbRow> newTable = new ArrayList<>();
        ArrayList<String> duplicateVariables = this.getIntersectingVariables(other);
        
        for(ProbRow thisRow : this.table)
            for(ProbRow otherRow : other.getTable())
                if(thisRow.sameMatchingVariableValues(otherRow)){
                    ProbRow newRow = thisRow.clone();
                    newRow.multiply(otherRow);
                    newTable.add(newRow);
                }
        this.variables = newTable.get(0).getVariables();
        this.parents = newTable.get(0).getParents();
        this.table = newTable;
        for(String removeName : duplicateVariables)
            this.removeVariable(removeName);
    }

    void reduceTable(String reduceTarget) throws CloneNotSupportedException {
        ArrayList<ProbRow> newTable = new ArrayList<>();
        removeVariable(reduceTarget);
        
        for(int i = 0; i < this.table.size()-1; i++){
            ProbRow firstRow = this.table.get(i);
            for(int j = i+1; j < this.table.size(); j++){
                ProbRow secondRow = this.table.get(j);
                if(firstRow.sameVariableValues(secondRow)){
                    ProbRow newRow = firstRow.clone();
                    double newProb = firstRow.getProb() + secondRow.getProb();
                    newRow.setProb(newProb);
                    newTable.add(newRow);
                }
            }
        }
        this.table = newTable;
    }
    
    @Override
    public String toString(){
        String nodeName = "-";
        String table = "";
        for(ProbRow row : this.table)
            table += "   " + row + "\n";
        if(this.hasNode())
            nodeName = this.node.getName();
        return "\nnode: " + nodeName + "\n"
                + "parents: " + this.parents + "\n"
                + "nr of variables: " + this.variables.size() + "\n"
                + table;
    }

    private ArrayList<String> getIntersectingVariables(Table other) {
        ArrayList<String> intersectingVariables = new ArrayList<>();
        
        for(Variable thisVar : this.variables)
            for(Variable otherVar : other.variables)
                if(thisVar.equalName(otherVar))
                    intersectingVariables.add(thisVar.getName());
        return intersectingVariables;
    }

    void removeObservedVariables(ArrayList<Variable> observedVars) {
        for(Variable observedVar : observedVars){
            if(this.containsVariable(observedVar.getName())){
                String observedValue = observedVar.getValue();
                Iterator rowIter = this.table.iterator();
                while(rowIter.hasNext()){
                    ProbRow row = (ProbRow) rowIter.next();
                    String currValue = row.getCorrespondingValue(observedVar);
                    if(!currValue.equals(observedValue))
                        rowIter.remove();
                }
            this.removeVariable(observedVar.getName());
            }
        }
    }

    ArrayList<Variable> getVariables() {
        return this.variables;
    }
}
