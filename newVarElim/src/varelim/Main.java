package varelim;

import java.util.ArrayList;

/**
 * Main class to read in a network, add queries and observed variables, and
 * eliminate variables.
 * 
 * @author Marcel de Korte, Moira Berens, Djamari Oetringer, Abdullahi Ali, Leonieke van den Bulk
 */

public class Main {
	private final static String networkName = "earthquake.bif"; // The network to be read in (format from http://www.bnlearn.com/bnrepository/)

	public static void main(String[] args) throws CloneNotSupportedException {
		
		// Read in the network
		Networkreader reader = new Networkreader(networkName); 
		
		// Get the variables and probabilities of the network
		ArrayList<Variable> Vs = reader.getVs(); 
		ArrayList<Table> Ps = reader.getPs(); 
		
		// Print variables and probabilities
		reader.printNetwork(Vs, Ps);

		// Ask user for query and heuristic
		reader.askForQuery(); 
		
		// Turn this on if you want to experiment with different heuristics for bonus points (you need to implement the heuristics yourself)
		//reader.askForHeuristic();
		//String heuristic = reader.getHeuristic();
		
		Variable Q = reader.getQueriedVariable(); 
		
		// Ask user for observed variables 
		reader.askForObservedVariables(); 
		ArrayList<Variable> O = reader.getObservedVariables(); 
		
		// Print the query and observed variables
		reader.printQueryAndObserved(Q, O); 
		
		
		//PUT YOUR CODE FOR THE VARIABLE ELIMINATION ALGORITHM HERE
                VariableEliminator varElim = new VariableEliminator(Q, O, reader);
                varElim.eliminateVariables();
        }
}