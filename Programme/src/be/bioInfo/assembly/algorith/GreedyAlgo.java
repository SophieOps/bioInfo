package be.bioInfo.assembly.algorith;

import java.util.ArrayList;

import be.bioInfo.assembly.exception.GreedyException;
import be.bioInfo.assembly.model.Edge;
import be.bioInfo.assembly.model.Graph;
import be.bioInfo.assembly.model.Node;

public class GreedyAlgo 
{

	/**
	 * Exécution de l'algo
	 * @param graph le graph 
	 * @return liste des arcs choisit
	 * @throws GreedyException
	 */
	public ArrayList<Edge> execute(Graph graph) throws GreedyException
	{
		ArrayList<ArrayList<Node>> set = new ArrayList();
		ArrayList<Edge> choosenEdge = new ArrayList<Edge>();
		
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
	 * Mise à jour des arcs choisit et des complémentaires
	 * @param edge arc choisit
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
	 * Vérifie que le noeud 1 et 2 n'appartienent pas au même ensemble
	 * @param node1 noeud 1
	 * @param node2 noeud 2
	 * @param set ensemble des noeuds 
	 * @return true si le noeud 1 et 2 appartiennent au même ensemble
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
	 * Recherche les ensembles contenant les noeud 1 et 2 et leur complémentaire
	 * @param set liste contenant les ensembles
	 * @param node1 noeud 1 
	 * @param node2 noeud 2
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
			throw new GreedyException("Problème dans le déroulement du Greedy");
		
		

		applyUnion(set, indexNodeSet1, indexNodeSet2, indexComplementaryNode1,
				indexComplementaryNode2);
		
	

	}

	/**
	 * Applique l'union sur les ensembles contenant les noeud 1 et 2 et leur complémentaire
	 * @param set la liste des ensembles
	 * @param indexNodeSet1 indice de l'ensemble contenant le noeud 1
	 * @param indexNodeSet2 indice de l'ensemble contenant le noeud 2
	 * @param indexComplementaryNode1 indice de l'ensemble contenant le complémentaire du noeud 1
	 * @param indexComplementaryNode2 indice de l'ensemble contenant le complémentaire du noeud 2
	 */
	private void applyUnion(ArrayList<ArrayList<Node>> set, int indexNodeSet1,
			int indexNodeSet2, int indexComplementaryNode1,
			int indexComplementaryNode2) {
		
		ArrayList<Node> nodeSet1 = set.get(indexNodeSet1);
		ArrayList<Node> nodeSet2 = set.get(indexNodeSet2);
		ArrayList<Node> complementaryNodeSet1 = set.get(indexComplementaryNode1);
		ArrayList<Node> complementaryNodeSet2 = set.get(indexComplementaryNode2);
		
		//ajout des données de l'ensemble contenant le noeud 2 dans l'ensemble du noeud 1
		for(int i = 0; i < nodeSet2.size(); i++)
		{
			nodeSet1.add(nodeSet2.get(i));
		}
		
		//si l'ensemble contenant le complémentaire du noeud 1 est différent de l'ensemble contenant le noeud 1
		if(indexNodeSet1 != indexComplementaryNode1)
		{
			//ajout des données de l'ensemble contenant le complémentaire du noeud 1 dans l'ensemble du noeud 1
			for(int i = 0; i < complementaryNodeSet1.size(); i++)
			{
				nodeSet1.add(complementaryNodeSet1.get(i));
			}
			//supprimer l'ensemble du complémentaire
			set.remove(complementaryNodeSet1);
		}
		
		//si l'ensemble contenant le complémentaire du noeud 2 est différent de l'ensemble contenant le noeud 2
		if(indexNodeSet2 != indexComplementaryNode2)
		{
			//ajout des données de l'ensemble contenant le complémentaire du noeud 2 dans l'ensemble du noeud 1
			for(int i = 0; i < complementaryNodeSet2.size(); i++)
			{
				nodeSet1.add(complementaryNodeSet2.get(i));
			}
			//supprimer l'ensemble du complémentaire
			set.remove(complementaryNodeSet2);
		}
		
		//supprimer l'ensemble du noeud 2
		set.remove(nodeSet2);
	}
}
