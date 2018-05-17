package varelim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class to represent the Table Object consisting of probability rows
 * 
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 */

public class Table implements Cloneable {

    private ArrayList<ProbRow> table;
    private Variable node;
    private ArrayList<Variable> parents;

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
    }

    public Table(ArrayList<ProbRow> table) throws CloneNotSupportedException{
        this.table = (ArrayList) table.clone();
        if(table.get(0).getNode() != null)
            this.node = table.get(0).getNode().clone();
        else
            this.node = null;
        this.parents = (ArrayList) table.get(0).getParents().clone();
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

    /**
     * returns true if the table contains a variable corresponding with varName
     * 
     * @param varName
     * @return 
     */
    boolean contains(String varName) {
        if(node != null && node.getName().equals(varName))
            return true;
        for(Variable parent : parents)
            if(parent.getName().equals(varName))
                return true;
        return false;
    }
    
    /**
     * multiply this table with the given table
     * 
     * @param tab
     * @throws CloneNotSupportedException 
     */
    void multiply(Table tab) throws CloneNotSupportedException {
        ArrayList<ProbRow> newTable = new ArrayList<>();
        
        for(ProbRow thisRow : table){
            for(ProbRow otherRow : tab.getTable()){
                newTable.add(thisRow.multiply(otherRow));
            }
        }
        ArrayList<ProbRow> trimmedTable = this.trimDuplicateVariables(newTable);
        this.node = newTable.get(0).getNode();
        this.parents = newTable.get(0).getParents();
        
        this.table = newTable;
    }
    
    /**
     * reduce this table corresponding with the reduce target, then return it
     * 
     * @param reduceTarget
     * @return 
     */
    public Table getReducedTable(String reduceTarget) throws CloneNotSupportedException {
        ArrayList<ProbRow> newTable = new ArrayList<>();
        removeVariable(reduceTarget);
        
        for(int i = 0; i < table.size() - 1; i++){
            ProbRow firstRow = table.get(i);
            for(int j = i + 1; j < table.size(); j ++){
                ProbRow secondRow = table.get(j);
                if(firstRow.sameParentsValues(secondRow)){
                    ProbRow newRow = firstRow.clone();
                    newRow.setProb(firstRow.getProb() + secondRow.getProb());
                    newTable.add(newRow);
                    break;
                }
            }
        }
        return new Table(newTable);
    }
    
    @Override
    public Table clone() throws CloneNotSupportedException {
        Table copy = (Table)super.clone();
        if(this.node != null)
            copy.node = this.node.clone();
        else
            copy.node = null;
        copy.parents = (ArrayList) this.parents.clone();
        copy.table = (ArrayList) this.table.clone();
        return copy;
    }
    
    @Override
    public String toString(){
        ArrayList<String> parentNames = new ArrayList<>();
        parents.forEach(p -> parentNames.add(p.getName()));
        String nodeName = "none";
        if(node != null)
            nodeName = node.getName();
        return "node: " + nodeName + "\n"
                + "parents: " + parentNames + "\n"
                + "probabilites: \n"
                + table.toString();
    }

    private void setNode(Variable node) {
        this.node = node;
    }

    private void setParents(ArrayList<Variable> parents) {
        this.parents = parents;
    }

    private void setTable(ArrayList<ProbRow> table) {
        this.table = table;
    }

    /**
     * removes rows with incorrect values for any duplicate variables
     * also removes duplicate variables
     * 
     * @param table
     * @return 
     */
    private ArrayList<ProbRow> trimDuplicateVariables(ArrayList<ProbRow> table) {
        Iterator iter = table.iterator();
        while(iter.hasNext()){
            ProbRow row = (ProbRow)iter.next();
            if(row.incorrectDuplicateVariableValue())
                iter.remove();
        }
        return table;
    }

    /**
     * normalize the probabilities contained in this table
     * currently only correctly works for tables of a single variable
     * 
     */
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

    /**
     * removes any rows that do not comply with the list of observed variables
     * also removes that variable from the table afterwards
     * 
     * @param observedVars 
     *//*
    void removeObservedVariables(ArrayList<Variable> observedVars) {
        for(Variable observed : observedVars){
            String varName = observed.getName();
            if(this.contains(varName)){
            String varVal = observed.getValue();
                Iterator rowIter = this.table.iterator();
                while(rowIter.hasNext()){
                    ProbRow row = (ProbRow) rowIter.next();
                    if(row.isIrrelevantRow(varName, varVal))
                        rowIter.remove();
                }
            }
            /*
            if(this.node.getName().equals(varName)){
                this.node = this.parents.get(0);
                this.parents.remove(0);
            } else {
                for(int i = 0; i < this.parents.size(); i++)
                    if(this.parents.get(i).getName().equals(varName)){
                        this.parents.remove(i);
                        break;
                    } 
            
            }*//*
        }
        
        for(Variable observed : observedVars){
            String varName = observed.getName();
            if(this.contains(varName)){
                int removeIdx = this.table.get(0).getVariableIndex(varName);
                this.removeVariable(varName);
                for(ProbRow row : this.table)
                    row.removeVar(this, removeIdx);
            }
        }
        /*
        Map<String, String> observedMap = new HashMap<>();
        for(Variable observed : observedVars)
            observedMap.put(observed.getName(), observed.getValue());
        
        Iterator parentIter = this.parents.iterator();
        while(parentIter.hasNext()){
            Variable parent = (Variable) parentIter.next();
            if(observedMap.containsKey(parent.getName())){
                Iterator rowIter = this.table.iterator();
                while(rowIter.hasNext()){
                    ProbRow row = (ProbRow) rowIter.next();
                    if(row.isIrrelevantRow(parent.getName(), observedMap.get(parent.getName())))
                        rowIter.remove();
                }
            }
        }*//*
    }
    */
    public void removeVariable(String name){
        int removeIndex = getVariableIndex(name);
        if(this.node != null && this.node.getName().equals(name)){
            this.node = null;
        } else {
            for(int i = 0; i < this.parents.size(); i++)
                if(this.parents.get(i).getName().equals(name)){
                    this.parents.remove(i);
                    break;
                }
        }
        for(ProbRow row : table)
            row.removeVariable(this, removeIndex);
    }

    void removeObservedVariable(Variable observedVar) {
        String observedName = observedVar.getName();
        String observedValue = observedVar.getValue();
        Iterator rowIter = this.table.iterator();
        while(rowIter.hasNext()){
            ProbRow row = (ProbRow) rowIter.next();
            if(row.isIrrelevantRow(observedName, observedValue))
                rowIter.remove();
        }
        removeVariable(observedName);
    }

    private int getVariableIndex(String reduceTarget) {
        return this.table.get(0).getVariableIndex(reduceTarget);
    }

}
