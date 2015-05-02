package be.bioInfo.assembly.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The data of the graph
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class Graph 
{
	//The list of the nodes
	private ArrayList<Node> nodeList;
	//The list of the edges
	private ArrayList<Edge> edgeList;

	public ArrayList<Node> getNodeList() {
		return nodeList;
	}

	public void setNodeList(ArrayList<Node> nodeList) {
		this.nodeList = nodeList;
	}

	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}

	public void setEdgeList(ArrayList<Edge> edgeList) {
		this.edgeList = edgeList;
	}
	
	/**
	 * Constructor
	 */
	public Graph()
	{
		edgeList = new ArrayList<Edge>();
	}
	
	/**
	 * Sort the list in descending order of the cost of the alignment
	 */
	public void sortList() {
        Collections.sort(this.edgeList, new Comparator<Edge>() {
             @Override
             public int compare(Edge e1, Edge e2) {
                 return e2.getAlignment().getCost()-e1.getAlignment().getCost();
             }
         });
    }
}
