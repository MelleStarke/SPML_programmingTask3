package varelim;

import java.util.ArrayList;

/**
 * Responsible for creating variables and giving
 * information about the variables to other classes.
 * 
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 */
public class Variable implements Cloneable {

	private String name;
	private ArrayList<String> values;
	private String value;
	private boolean observed = false;
	private ArrayList<Variable> parents;

	/**
	 * Constructor of the class.
	 * @param name, name of the variable.
	 * @param values, the value of the variable.
	 */
	public Variable(String name, ArrayList<String> values) {
		this.name = name;
		this.values = values;
                this.parents = new ArrayList<>();
                this.value  = "";
	}

	/**
	 * Getter of the values.
	 * @return the values of the variable.
	 */
	public ArrayList<String> getValues(){
		return values;
	}
	
	/**
	 * Getter of the amount of values.
	 * @return the amount of values
	 */
	public int getNumberOfValues() {
		return values.size();
	}

	/**
	 * Getter of the name.
	 * @return the name of the variable.
	 */
	public String getName() {
		return name;
	}

	/** 
	 * Setter of the value.
	 * @param value to which the value of the variable should be set.
	 */
	public void setValue(String s) {
		this.value = s;
	}

	/**
	 * Check if string v is contained by the variable.
	 * @return a boolean denoting if values contains string v.
	 */
	public boolean isValueOf(String v) {
		return values.contains(v);
	}
	
	/**
	 * Getter of the value.
	 * @return the value of the variable.
	 */
	public String getValue(){
		return value;
	}

	/**
	 * Getter of the parents.
	 * @return the list of parents.
	 */
	public ArrayList<Variable> getParents() {
		return parents;
	}
	
	/**
	 * Setter of the parents.
	 * @param the list of parents of the variable.
	 */
	public void setParents(ArrayList<Variable> parents) {
		this.parents = parents;
	}

	/**
	 * Check if a variable has parents.
	 * @return a boolean denoting if the variable has parents.
	 */
	public boolean hasParents(){
		return parents != null;
	}
	
	/**
	 * Getter for the number of parents a variable has.
	 * @return the amount of parents
	 */
	public int getNrOfParents() {
		if(parents != null)
			return parents.size();
		return 0;
	}

	/**
	 * Setter for the observation of a variable.
	 * @param a boolean denoting if the variable is observed or not.
	 */
	public void setObserved(boolean b) {
		this.observed = b;
	}

	/**
	 * Getter for if a variable is observed.
	 * @return a boolean denoting if the variable is observed or not.
	 */
	public boolean getObserved() {
		return observed;
	}
        
        @Override
        public String toString(){
            return "not finished yet";
        }
        
        @Override
        public Variable clone() throws CloneNotSupportedException{
            Variable copy = (Variable)super.clone();
            copy.setName(this.getName());
            copy.setObserved(this.getObserved());
            copy.setParents((ArrayList)this.getParents().clone());
            copy.setValue(this.getValue());
            copy.setValues((ArrayList)this.getValues().clone());
            return copy;
        }

    public void setName(String name) {
        this.name = name;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
    
    public boolean equals(Variable other){
        return this.getName().equals(other.getName());
    }

    boolean equalsAny(ArrayList<Variable> vars) {
        for(Variable var : vars)
            if(this.equals(var))
                return true;
        return false;
    }
}