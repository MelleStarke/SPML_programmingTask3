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
    private Variable            queryVar;
    private ArrayList<Variable> observedVars;
    private Networkreader       network;
    private double              probability;
    private ArrayList<Table>    probTables;

    public VariableEliminator(Variable Q, ArrayList<Variable> O, Networkreader network){
        this.queryVar = Q;
        this.observedVars = O;
        this.network = network;
        this.probTables = network.getPs();
    }
    
    public void eliminateVariables(){
        ArrayList<Integer> orderIdxs = getOrderIndices();
        
        for(int idx : orderIdxs){
            ArrayList<Variable> Vs = network.getVs();
            String varName = Vs.get(idx).getName();
            Table targetTable = getTargetTable(varName);
            Table resultTable = reduceTable(targetTable, varName);
            removeTablesContaining(varName);
            this.probTables.add(resultTable);
        }
    }
    
    private void removeTablesContaining(String name){
        
    }
    
    public ArrayList<Table> getProbTables(){
        return this.probTables;
    }
    
    private Table getTargetTable(String name){
        ArrayList<Table> targetTables = new ArrayList<Table>();
        for(Table tab : this.probTables){
            if(tab.contains(name))
                targetTables.add(tab);
        }
        
        Table resultTable = targetTables.get(0);
        
        for(int i = 1; i < targetTables.size(); i++){
            Table next = targetTables.get(i);
            resultTable.multiply(next);
        }
        return resultTable;
    }
    
    private Table reduceTable(Table targetTable, String name) {
        Table result = new Table();
        targetTable.deleteCol(name);
        
        ArrayList<ProbRow> newTable = targetTable.getTable();
        result.reduce(newTable);
        
        return result;
    }
    
    private ArrayList<Integer> getOrderIndices(){
        ArrayList<Variable> Vs = network.getVs();
        ArrayList<Integer> orderIdxs = new ArrayList<Integer>();
        
        for(int i = 0; i < Vs.size(); i++){
            String name = Vs.get(i).getName();
            if(network.isLeafNode(name) && !(this.queryVar.getName().equals(name)))
                orderIdxs.add(i);
        }
       /*
        ArrayList<String> parentsOfQuery = new ArrayList<String>();
        for(Variable parent : queryVar.getParents()){
            parentsOfQuery.add(parent.getName());
        }
        for(int i = 0; i < Vs.size(); i++){
            String name = Vs.get(i).getName();
            if(!parentsOfQuery.contains(name)
                    && !network.isLeafNode(name)
                    && !this.queryVar.getName().equals(name))
                orderIdxs.add(i);
        }
        
        for(int i = 0; i < Vs.size(); i++){
            String name = Vs.get(i).getName();
            if(parentsOfQuery.contains(name))
                orderIdxs.add(i);
        }
        */
        return orderIdxs;
    }
    
    
    public double getProbability(){
        return this.probability;
    }
    
    public void setQuery(Variable target){
        this.queryVar = target;
    }
    
    public void setObserved(ArrayList<Variable> observed){
        this.observedVars = observed;
    }
    
    public void setNetwork(Networkreader network){
        this.network = network;
    }
    
    public Variable getQuery(){
        return this.queryVar;
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
                + "Target: " + this.queryVar.getName() + "\n"
                + "Probability: " + this.probability; 
    }
}
