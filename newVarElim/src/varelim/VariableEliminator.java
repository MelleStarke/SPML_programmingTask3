/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package varelim;

import java.util.ArrayList;

/**
 *
 * @author Melle
 */
public class VariableEliminator {
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

    void eliminateVariables() {
        reduceObservedVariables();
        String [] eliminationOrder = getEliminationOrder();
        
        for(String varName : eliminationOrder){
            ArrayList<Table> correspondingTables = getCorrespondingTables(varName);
            Table combinedTable = multiplyTables(correspondingTables);
            Table reducedTable = reduceTable(combinedTable, varName);
            removeTablesContaining(varName);
            probTables.add(reducedTable);
        }
        this.queryVarProb = (Table) this.probTables.clone();
        this.queryVarProb.normalize();
    }

    private void reduceObservedVariables() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String[] getEliminationOrder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ArrayList<Table> getCorrespondingTables(String varName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Table multiplyTables(ArrayList<Table> correspondingTables) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Table reduceTable(Table combinedTable, String varName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void removeTablesContaining(String varName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
