package be.bioInfo.assembly.algorithm;

import java.util.ArrayList;

import be.bioInfo.assembly.exception.GreedyException;
import be.bioInfo.assembly.model.Edge;
import be.bioInfo.assembly.model.Graph;
import be.bioInfo.assembly.model.Node;

/**
 * @author Cambier Robin & Opsommer Sophie, 2015
 *
 */
public class GreedyAlgo 
{
	/**
	 * Constructor.
	 * Do nothing, simple implementation.
	 */
	public GreedyAlgo() {}

	/**
	 * @param graph
	 * @return
	 * @throws GreedyException
	 */
	public ArrayList<Edge> execute(Graph graph) throws GreedyException
	{
		ArrayList<ArrayList<Node>> set = new ArrayList();
		ArrayList<Edge> choosenEdge = new ArrayList<Edge>();
		
		//tel qu'elle est la, la boucle ajoute les listes de noeud tel que chacune ne contient que 1 noeud ????
		for(int i = 0; i < graph.getNodeList().size(); i++)
		{
			ArrayList<Node> nodeSet = new ArrayList<Node>();
			nodeSet.add(graph.getNodeList().get(i));
			set.add(nodeSet); // ????? il ne devrait pas se trouver à l'extérieur de la boucle for ?
		}
		
		for(Edge edge: graph.getEdgeList())
		{
			boolean sameSet = checkSet(edge.getSource(), edge.getDestination(), set);
			
			if(edge.getDestination().isIn() == false && edge.getSource().isOut() == false && sameSet == false)
			{
				updateEdge(edge);
				choosenEdge.add(edge);
				unionSet(set, edge.getSource(), edge.getDestination());
			}
			if(set.size() == 1)
				break;

				
		}
		
		return choosenEdge;
	}

	//pourquoi avoir 2 variables (in et out) si elles ont toujoursles mêmes vameurs ?
	private void updateEdge(Edge edge) {
		edge.getSource().setOut(true);
		edge.getDestination().setIn(true);
		edge.getSource().getComplementaryNode().setIn(true);
		edge.getSource().getComplementaryNode().setOut(true);
		edge.getDestination().getComplementaryNode().setIn(true);
		edge.getDestination().getComplementaryNode().setOut(true);
	}
	
	//permet de vérifié qu'un arc n'as pas la même source et la même destination ?????
	private boolean checkSet(Node node1, Node node2, ArrayList<ArrayList<Node>> set)
	{
		for(ArrayList<Node> nodeSet : set)
		{
			if(nodeSet.contains(node1) && nodeSet.contains(node2))
				return true;
		}
		return false;
	}
	
	private void unionSet(ArrayList<ArrayList<Node>> set, Node node1, Node node2) throws GreedyException
	{
		int indexNodeSet1 = 0, indexNodeSet2 = 0, indexComplementaryNode1 = 0, indexComplementaryNode2 = 0;
	
		for(ArrayList<Node> nodeSet : set)
		{
			if(nodeSet.contains(node1))
				indexNodeSet1 = set.indexOf(nodeSet);
			if(nodeSet.contains(node2))
				indexNodeSet2 = set.indexOf(nodeSet);
			if(nodeSet.contains(node1.getComplementaryNode()))
				indexComplementaryNode1 = set.indexOf(nodeSet);
			if(nodeSet.contains(node2.getComplementaryNode()))
				indexComplementaryNode2 = set.indexOf(nodeSet);
		}
		
		if(indexNodeSet1 == indexNodeSet2)
			throw new GreedyException("Problème dans le déroulement du Greedy");
		
		ArrayList<Node> nodeSet1 = set.get(indexNodeSet1);
		ArrayList<Node> nodeSet2 = set.get(indexNodeSet2);
		ArrayList<Node> complementaryNodeSet1 = set.get(indexComplementaryNode1);
		ArrayList<Node> complementaryNodeSet2 = set.get(indexComplementaryNode2);
		
		
		applyUnion(set, indexNodeSet1, indexNodeSet2, indexComplementaryNode1,
				indexComplementaryNode2, nodeSet1, nodeSet2,
				complementaryNodeSet1, complementaryNodeSet2);
		
	// ????? après il devient quoi "nodeSet1" parce que il contient l'union des 2 fragment relié par l'arc...
		//parce que ça ne sort pas de la fonction unionSet.

	}

	private void applyUnion(ArrayList<ArrayList<Node>> set, int indexNodeSet1,
			int indexNodeSet2, int indexComplementaryNode1,
			int indexComplementaryNode2, ArrayList<Node> nodeSet1,
			ArrayList<Node> nodeSet2, ArrayList<Node> complementaryNodeSet1,
			ArrayList<Node> complementaryNodeSet2) {
		
		for(int i = 0; i < nodeSet2.size(); i++)
		{
			nodeSet1.add(nodeSet2.get(i));
		}
		
		if(indexNodeSet1 != indexComplementaryNode1)
		{
			for(int i = 0; i < complementaryNodeSet1.size(); i++)
			{
				nodeSet1.add(complementaryNodeSet1.get(i));
			}
			set.remove(complementaryNodeSet1);
		}
		
		if(indexNodeSet2 != indexComplementaryNode2)
		{
			for(int i = 0; i < complementaryNodeSet2.size(); i++)
			{
				nodeSet1.add(complementaryNodeSet2.get(i));
			}
			set.remove(complementaryNodeSet2);
		}
		
		set.remove(nodeSet2);
	}
}
