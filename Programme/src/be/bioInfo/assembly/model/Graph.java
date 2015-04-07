package be.bioInfo.assembly.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Cambier Robin & Opsommer Sophie, 2015
 *
 */
public class Graph 
{

	private ArrayList<Node> nodeList;
	private ArrayList<Edge> edgeList;

	/**
	 * @return the list with all the node of the graph.
	 */
	public ArrayList<Node> getNodeList() {
		return nodeList;
	}

	/**
	 * @param nodeList
	 */
	public void setNodeList(ArrayList<Node> nodeList) {
		this.nodeList = nodeList;
	}

	/**
	 * @return
	 */
	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}

	/**
	 * @param edgeList
	 */
	public void setEdgeList(ArrayList<Edge> edgeList) {
		this.edgeList = edgeList;
	}
	
	/**
	 * Constructor.
	 * @param nodeList
	 * @param edgeList
	 */
	public Graph(ArrayList<Node> nodeList, ArrayList<Edge> edgeList)
	{
		this.nodeList = nodeList;
		this.edgeList = edgeList;
		sortList();
		
	    /*for(int i = 0; i < edgeList.size(); i++)
		{
			System.out.println("Arc de "+edgeList.get(i).getSource().getData().getCode()+" � "+edgeList.get(i).getDestination().getData().getCode()+" poids = "+edgeList.get(i).getWeight());
		}*/
	}
	
	private void sortList() {
        Collections.sort(this.edgeList, new Comparator<Edge>() {
             @Override
             public int compare(Edge e1, Edge e2) {
                 return e1.CompareTo(e2);
             }
          
         });
    }


	
	
	
}
