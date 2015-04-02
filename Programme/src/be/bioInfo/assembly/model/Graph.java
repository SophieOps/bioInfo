package be.bioInfo.assembly.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Graph 
{

	private ArrayList<Node> nodeList;
	private ArrayList<Edge> edgeList;
	
	public Graph(ArrayList<Node> nodeList, ArrayList<Edge> edgeList)
	{
		this.nodeList = nodeList;
		this.edgeList = edgeList;
		sortList();
		
	/*	for(int i = 0; i < edgeList.size(); i++)
		{
			System.out.println("Arc de "+edgeList.get(i).getSource().getData().getCode()+" à "+edgeList.get(i).getDestination().getData().getCode()+" poids = "+edgeList.get(i).getWeight());
		}
		*/
	}
	
	private void sortList() {
        Collections.sort(this.edgeList, new Comparator<Edge>() {
             @Override
             public int compare(Edge e1, Edge e2) {
                 return e1.CompareTo(e2);
             }
          
         });
    }

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
	
	
	
}
