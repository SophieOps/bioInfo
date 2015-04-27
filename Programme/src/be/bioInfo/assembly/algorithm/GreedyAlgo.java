package be.bioInfo.assembly.algorithm;

import java.util.ArrayList;

import be.bioInfo.assembly.exception.GreedyException;
import be.bioInfo.assembly.model.Edge;
import be.bioInfo.assembly.model.Graph;
import be.bioInfo.assembly.model.Node;

/**
* @author Watillon Thibaut & Opsommer Sophie, 2015
*
*/
public class GreedyAlgo 
{

	/**
	 * Execute the greedy algorithm
	 * @param graph the graph 
	 * @return the list of the choosen edges
	 * @throws GreedyException
	 */
	public ArrayList<Edge> execute(Graph graph) throws GreedyException
	{
		ArrayList<ArrayList<Node>> set = new ArrayList();
		ArrayList<Edge> choosenEdge = new ArrayList<Edge>();
		
		//Cr�ation des ensembles
		for(int i = 0; i < graph.getNodeList().size(); i++)
		{
			ArrayList<Node> nodeSet = new ArrayList<Node>();
			nodeSet.add(graph.getNodeList().get(i));
			set.add(nodeSet);
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

	/**
	 * Update of the choosen edges and their complementary
	 * @param edge choosen edge
	 */
	private void updateEdge(Edge edge) {
		edge.getSource().setOut(true);
		edge.getDestination().setIn(true);
		edge.getSource().getComplementaryNode().setIn(true);
		edge.getSource().getComplementaryNode().setOut(true);
		edge.getDestination().getComplementaryNode().setIn(true);
		edge.getDestination().getComplementaryNode().setOut(true);
	}
	
	/**
	 * Check the node 1 and 2 don't belong to the same set
	 * @param node1 node 1
	 * @param node2 node 2
	 * @param set list of the sets of the nodes
	 * @return true if the node 1 and 2 belong to the same set 
	 */
	private boolean checkSet(Node node1, Node node2, ArrayList<ArrayList<Node>> set)
	{
		for(ArrayList<Node> nodeSet : set)
		{
			if(nodeSet.contains(node1) && nodeSet.contains(node2))
				return true;
		}
		return false;
	}
	
	/**
	 * Look for the sets containing the node 1 and 2 and their complementary
	 * @param set list of the sets
	 * @param node1 node 1 
	 * @param node2 node 2
	 * @throws GreedyException
	 */
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
			throw new GreedyException("Probl�me dans le d�roulement du Greedy");
		
		

		applyUnion(set, indexNodeSet1, indexNodeSet2, indexComplementaryNode1,
				indexComplementaryNode2);
		
	

	}

	/**
	 * Apply the union on the sets containing the node 1 and 2 and their complementary
	 * @param set the list of the sets
	 * @param indexNodeSet1 index of the set containing the node 1 
	 * @param indexNodeSet2 index of the set containing the node 2
	 * @param indexComplementaryNode1 index of the set containing the complementary node 1 
	 * @param indexComplementaryNode2 index of the set containing the complementary node 2
	 */
	private void applyUnion(ArrayList<ArrayList<Node>> set, int indexNodeSet1,
			int indexNodeSet2, int indexComplementaryNode1,
			int indexComplementaryNode2) {
		
		ArrayList<Node> nodeSet1 = set.get(indexNodeSet1);
		ArrayList<Node> nodeSet2 = set.get(indexNodeSet2);
		ArrayList<Node> complementaryNodeSet1 = set.get(indexComplementaryNode1);
		ArrayList<Node> complementaryNodeSet2 = set.get(indexComplementaryNode2);
		
		//ajout des donn�es de l'ensemble contenant le noeud 2 dans l'ensemble du noeud 1
		for(int i = 0; i < nodeSet2.size(); i++)
		{
			nodeSet1.add(nodeSet2.get(i));
		}
		
		//si l'ensemble contenant le compl�mentaire du noeud 1 est diff�rent de l'ensemble contenant le noeud 1
		if(indexNodeSet1 != indexComplementaryNode1)
		{
			//ajout des donn�es de l'ensemble contenant le compl�mentaire du noeud 1 dans l'ensemble du noeud 1
			for(int i = 0; i < complementaryNodeSet1.size(); i++)
			{
				nodeSet1.add(complementaryNodeSet1.get(i));
			}
			//supprimer l'ensemble du compl�mentaire
			set.remove(complementaryNodeSet1);
		}
		
		//si l'ensemble contenant le compl�mentaire du noeud 2 est diff�rent de l'ensemble contenant le noeud 2
		if(indexNodeSet2 != indexComplementaryNode2)
		{
			//ajout des donn�es de l'ensemble contenant le compl�mentaire du noeud 2 dans l'ensemble du noeud 1
			for(int i = 0; i < complementaryNodeSet2.size(); i++)
			{
				nodeSet1.add(complementaryNodeSet2.get(i));
			}
			//supprimer l'ensemble du compl�mentaire
			set.remove(complementaryNodeSet2);
		}
		
		//supprimer l'ensemble du noeud 2
		set.remove(nodeSet2);
	}
}