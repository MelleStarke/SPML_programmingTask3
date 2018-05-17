/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varelim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author Melle
 */
public class VariableEliminator {
    private final boolean debug = true;
    private Variable queryVar;
    private ArrayList<Variable> observedVars;
    private Networkreader network;
    private Table queryVarProb;
    private ArrayList<Table> probTables;
    
    public VariableEliminator(Variable Q, ArrayList<Variable> O, Networkreader network){
        this.queryVar = Q;
        this.observedVars = O;
        this.network = network;
        this.probTables = network.getPs();
        this.queryVarProb = new Table(null, null, null);
    }
    
    /**
     * eliminates variables relative to the queried variable
     * and observed variables
     * 
     * @throws CloneNotSupportedException 
     */
    public void eliminateVariables() throws CloneNotSupportedException{
        Scanner scan = new Scanner(System.in);
        //removeObservedVariables();
        ArrayList<String> eliminationOrder =  getEliminationOrderAsStrings();
        if(debug){
            System.out.println("elimination order:\n" + eliminationOrder);
            scan.nextLine();
        }
        
        // System.out.println("order: " + eliminationOrder.toString());
        int i = 0;
        for(String varName : eliminationOrder){
            //System.out.println(i);
            ArrayList<Table> correspondingTables = getCorrespondingTables(varName);
            if(debug){
                System.out.println("corresponding tables:\n" + correspondingTables);
                scan.nextLine();
            }
            removeTablesContaining(varName);
            
            Table combinedTable = multiplyTables(correspondingTables);
            if(debug){
                System.out.println("combined table:\n" + combinedTable);
                scan.nextLine();
            }
            
            Table reducedTable = reduceTable(combinedTable, varName);
            if(debug){
                System.out.println("reduced table:\n" + reducedTable);
                scan.nextLine();
            }
            
            probTables.add(reducedTable);
            i++;
        }
        this.queryVarProb = (Table) this.probTables.get(0).clone();
        this.queryVarProb.normalize();
    }
    
    /**
     * removes tables containing varName from this.probTables
     * 
     * @param var 
     */
    public void removeTablesContaining(String varName){
        Iterator<Table> iter = probTables.iterator();
        while(iter.hasNext()){
            Table tab = iter.next();
            if(tab.contains(varName))
                iter.remove();
        }
    }
    
    /**
     * returns a list of all tables containing varName
     * 
     * @param varName
     * @return 
     */
    private ArrayList<Table> getCorrespondingTables(String varName){
        
        ArrayList<Table> correspondingTables = new ArrayList<>();
        for(Table tab : this.probTables){
            if(tab.contains(varName))
                correspondingTables.add(tab);
        }
        if(correspondingTables.isEmpty())
            throw new IllegalArgumentException(varName + "has no corresponding tables");
        return correspondingTables;
    }
    
    /**
     * reduce the targetTable, relative to the reduceTarget
     * 
     * @param targetTable
     * @param reduceTarget
     * @return
     * @throws CloneNotSupportedException 
     */
    private Table reduceTable(Table targetTable, String reduceTarget) throws CloneNotSupportedException{
        Table tableCopy = targetTable.clone();
        return tableCopy.getReducedTable(reduceTarget);
    }
    
    /**
     * returns the order in which variables will be eliminated
     * as a list of strings
     * 
     * @return 
     */
    private ArrayList<String> getEliminationOrderAsStrings() throws CloneNotSupportedException{
        ArrayList<Variable> Vs = getRelevantEliminationVariables();
        ArrayList<String> order = new ArrayList<String>();
        
        for(Variable var : Vs){
            String varName = var.getName();
            if(network.isLeafNode(varName) && !(queryVar.getName().equals(varName)))
                order.add(varName);
        }       
        ArrayList<String> parentsOfQuery = new ArrayList<String>();
        for(Variable parent : queryVar.getParents()){
            parentsOfQuery.add(parent.getName());
        }
        for(Variable var : Vs){
            String varName = var.getName();
            if(!parentsOfQuery.contains(varName)
                    && !network.isLeafNode(varName)
                    && !queryVar.getName().equals(varName))
                order.add(varName);
        }
        for(Variable var : Vs){
            String varName = var.getName();
            if(parentsOfQuery.contains(varName))
                order.add(varName);
        }
        return order;
    }
    
    /**
     * multiply all tables in a given list until a single one results
     * also removes irrelevant rows according to the observed variables
     * 
     * @param tables
     * @return
     * @throws CloneNotSupportedException 
     */
    private Table multiplyTables(ArrayList<Table> tables) throws CloneNotSupportedException {
        Table combinedTable = tables.get(0);
        tables.remove(0);
        System.out.println("table size: " + tables.size());
        for(Table tab : tables)
            combinedTable.multiply(tab);
        return combinedTable;
    }
    
    private void removeObservedVariables(){
        for(int i = 0; i < this.observedVars.size(); i++){
            Variable observedVar = this.observedVars.get(i);
            String observedName = observedVar.getName();
            Iterator tabIter = this.probTables.iterator();
            while(tabIter.hasNext()){
                Table tab = (Table) tabIter.next();
                if(tab.getNode().equals(observedVar)){
                    tabIter.remove();
                } else {
                    tab.removeObservedVariable(observedVar);
                }
            }
        }
    }
    
    @Override
    public String toString(){
        return "\nGiven: " + this.observedVars + "\n"
                + "Target: " + this.queryVar.getName() + "\n"
                + "Probability: " + this.queryVarProb; 
    }

    private ArrayList<Variable> getRelevantEliminationVariables() throws CloneNotSupportedException {
        ArrayList<Variable> result = new ArrayList<>();
        for(Variable v : this.network.getVs()){
            if(!v.equals(this.queryVar) && !v.equalsAny(this.observedVars))
                result.add(v.clone());
        }
        return result;
    }

    
    
}
