package problemC;


import java.util.ArrayList;
import java.util.Scanner;


public class Rainbow{

	//Variables
	int number_of_nodes;
	ArrayList<Edge>[] adjecent; // Contains neighbour edges

	int[] incrementValues;
	int globalSum;
	int[] colorCount;

	public Rainbow() {
		Scanner sc = new Scanner(System.in);

		number_of_nodes = sc.nextInt();
		adjecent = new ArrayList[number_of_nodes]; // make adjacent list

		// Create graph
		for (int i = 0; i < number_of_nodes; i++) {			
			adjecent[i] = new ArrayList<>(); // create a list for each node
		}
		for (int j = 1; j < number_of_nodes; j++) {
			int a = sc.nextInt()-1; //read node a . -1 because of index start at 0.
			int b = sc.nextInt()-1; //read node b .
			int c = sc.nextInt(); //edge color c
			adjecent[a].add(new Edge(c, b)); // create new Edge in adjecent list to node a, going to b
			adjecent[b].add(new Edge(c, a)); // create new Edge in adjecent list to node b, going to a
		}
 
		globalSum = 0; 
		incrementValues = new int[number_of_nodes];
		//colorCount = new int[number_of_nodes];
		
		dfs(0, 0); // Start with arbitrary root. Choose node 0.
		
		
		// Print results
		int good_nodes = 0;
		for (int i = 0; i < number_of_nodes; i++) { // Count good nodes
			// "bad" if globalSum + incrementValue > 0.
			if (globalSum + incrementValues[i] == 0) {
				good_nodes++;
			}
		}
			System.out.println(good_nodes); // Print good nodes
			
		for (int i = 0; i < number_of_nodes; i++) {
			// "bad" if globalSum + incrementValue > 0.
			if (globalSum + incrementValues[i] == 0) {
				System.out.println(i+1); // print the ones that are good
			}
		}
	}
	
	// Depth first search
	public void dfs(int node, int parent) { // O(V)
		colorCount = new int[number_of_nodes]; // THIS WILL TAKE TIME. OBS
		/* Doing the inclusion/exclusion step. Updates mark values. */
		
		for (Edge edge : adjecent[node]) { // increment
			colorCount[edge.getColor()]++; // Increase colorCount for edge
		}
		
		
		
		for (Edge edge : adjecent[node]) {
			if(edge.getAssociated_node() == parent) { // Either associated node is a parent
				
				if(colorCount[edge.getColor()] > 1) { // "BAD" This is "bad". Decrement the sub-tree. Two edges with same color
					globalSum ++;
					incrementValues[node]--;
				}
				
			}else { // Or not parent
				if(colorCount[edge.getColor()] > 1) {  //"BAD"
					// Incrememt the child node because we are exploring the sub-tree down
					incrementValues[edge.getAssociated_node()]++; 
				}
			}
		}
		
		
		
		for (Edge edge : adjecent[node]) { // decrement
			colorCount[edge.getColor()]--; // Decrease colorCount for edge
		}
		
		/* Done inclusion/exclusion step. Updates mark values. */
		
		//Actual DFS
		for (Edge edge : adjecent[node]) { // Push increment values down the sub-tree
			if (edge.getAssociated_node() != parent) {
				incrementValues[edge.getAssociated_node()] += incrementValues[node]; // push to child
				
				// Recursive dfs
				dfs(edge.getAssociated_node(), node); //This node becomes parent.
			}
		}
		
		
	}
	
	private void print_node_list() {
		for (int i = 0; i < adjecent.length; i++) {
			System.out.println("Edges in Nodes "+(i+1)+"'s list:");
			for (int j = 0; j < adjecent[i].size(); j++) {
				System.out.println(adjecent[i].get(j).toString());
			}
		}
	}

		
	public static void main(String[] args) {
			Rainbow rainbow = new Rainbow();
		}
	}

	class Edge {

		private int color; // Color of node
		private int associated_node; // 

		public Edge(int color, int associated_node) {
			this.color = color;
			this.associated_node = associated_node;
		}

		@Override
		public String toString() {
			String string = "";

			string += "Color: "+color+". Associated node: " + associated_node;

			return string;
		}

		public int getColor() {
			return color;
		}

		public int getAssociated_node() {
			return associated_node;
		}
		
	}
