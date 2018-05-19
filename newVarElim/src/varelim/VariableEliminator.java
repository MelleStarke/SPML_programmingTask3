/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varelim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Melle
 */
public class VariableEliminator {
    private Variable queryVar;
    private ArrayList<Variable> observedVars;
    private Networkreader network;
    private Table queryVarTable;
    private ArrayList<Table> probTables;
    
    public VariableEliminator(Variable Q, ArrayList<Variable> O, Networkreader network){
        this.queryVar = Q;
        this.observedVars = O;
        this.network = network;
        this.probTables = network.getPs();
        this.queryVarTable = null;
    }

    void eliminateVariables() throws CloneNotSupportedException {
            if(!this.observedVars.isEmpty())
                System.out.println("tables before observed removal:\n" + this.probTables + "\n\n");
        reduceObservedValues();
            if(!this.observedVars.isEmpty())
                System.out.println("tables after observed removal:\n" + this.probTables + "\n\n");
        ArrayList<String> eliminationOrder = getEliminationOrder();
        System.out.println("elim order: " + eliminationOrder);
        int i = 0;
        for(String varName : eliminationOrder){
            System.out.println(i);
            i++;
            //    System.out.println("\n\ncurrent variable: " + varName);
            ArrayList<Table> correspondingTables = getCorrespondingTables(varName);
            //    System.out.println("\ncorresponding tables:\n" + correspondingTables);
            removeTablesContaining(varName);
            Table multipliedTable = multiplyTables(correspondingTables);
            //    System.out.println("\nmultiplied table: \n" + multipliedTable);
            Table reducedTable = reduceTable(multipliedTable, varName);
            //    System.out.println("\nreduced table:\n" + reducedTable);
            probTables.add(reducedTable);
        }
        for(Table tab : this.probTables)
            tab.normalize();
    }

    private void reduceObservedValues() {
        for(Table tab : this.probTables)
            tab.removeObservedVariables(this.observedVars);
        Iterator tabIter = this.probTables.iterator();
        while(tabIter.hasNext()){
            Table tab = (Table) tabIter.next();
            if(tab.getVariables().isEmpty())
                tabIter.remove();
        }
    }

    private ArrayList<String> getEliminationOrder() throws CloneNotSupportedException {
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
    
    private ArrayList<Variable> getRelevantEliminationVariables() throws CloneNotSupportedException {
        ArrayList<Variable> result = new ArrayList<>();
        for(Variable v : this.network.getVs()){
            if(!v.equals(this.queryVar) && !v.equalsAny(this.observedVars))
                result.add(v);
        }
        return result;
    }

    private ArrayList<Table> getCorrespondingTables(String varName){
        ArrayList<Table> correspondingTables = new ArrayList<>();
        for(Table tab : this.probTables){
            if(tab.containsVariable(varName))
                correspondingTables.add(tab);
        }
        if(correspondingTables.isEmpty())
            throw new IllegalArgumentException(varName + "has no corresponding tables");
        return correspondingTables;
    }
    
    private void removeTablesContaining(String varName) {
        Iterator<Table> iter = probTables.iterator();
        while(iter.hasNext()){
            Table tab = iter.next();
            if(tab.containsVariable(varName))
                iter.remove();
        }
    }

    private Table multiplyTables(ArrayList<Table> tables) throws CloneNotSupportedException {
        Table combinedTable = tables.get(0);
        tables.remove(0);
        for(Table tab : tables)
            combinedTable.multiply(tab);
        return combinedTable;
    }

    private Table reduceTable(Table table, String reduceTarget) throws CloneNotSupportedException {
        table.reduceTable(reduceTarget);
        return table;
    }
    
    @Override
    public String toString(){
        return "remaining probability tables after variable elimination:\n" + this.probTables;
    }

    ArrayList<Table> getProbTables() {
        return this.probTables;
    }
}
