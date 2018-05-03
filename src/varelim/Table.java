package varelim;

import java.util.ArrayList;

/**
 * Class to represent the Table Object consisting of probability rows
 * 
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 */

public class Table {

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
        
        public Table(){
            this.table = new ArrayList<ProbRow>();
            this.node = null;
            this.parents = new ArrayList<Variable>();
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
        
        public boolean contains(String name){
            for(Variable parent : this.parents)
                if(name.equals(parent.getName()))
                    return true;
            return name.equals(this.node.getName());
        }
        
        public void multiply(Table with){
            
        }
        
        public void deleteCol(String name){
            for(int i = 0; i < parents.size(); i++){
                String parentName = parents.get(i).getName();
                if(name.equals(parentName))
                    parents.remove(i);
            }
            String nodeName = node.getName();
            if(name.equals(nodeName)){
                node = parents.get(0);
                parents.remove(0);
            }
            
            for(ProbRow row : table){
                row.deleteCol(name);
            }
        }
        
        public void reduce(ArrayList<ProbRow> table){
            for(ProbRow row : table){
                if(!this.containsVals(row))
                    this.table.add(row.deepcopy());
                else
                    this.combineRow(row);
                
            }
        }
        
        private void combineRow(ProbRow Qrow){
            for(ProbRow row : this.table){
                if(row.sameParentsValues(Qrow))
                    row.add(Qrow);
            }
        }
        
        private boolean containsVals(ProbRow Qrow){
            ArrayList<String> Qvals = Qrow.getValues();
            for(ProbRow row : this.table){
                ArrayList<String> vals = row.getValues();
                if(vals.equals(Qvals))
                    return true;
                /*
                boolean equals = true;
                for(int i = 0; i < row.getValues().size(); i++)
                    if(!Qvals.get(i).equals(vals.get(i)))
                        equals = false;
                if(equals)
                    return true;
                */
            }
            return false;
        }
}
