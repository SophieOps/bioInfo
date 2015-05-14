package be.bioInfo.assembly.algorithm;


import java.util.ArrayList;
import be.bioInfo.assembly.exception.GreedyException;
import be.bioInfo.assembly.graph.Edge;
import be.bioInfo.assembly.graph.Graph;
import be.bioInfo.assembly.graph.Node;
import be.bioInfo.assembly.model.AlignmentType;

/**
 * Greedy algorithm
* @author Watillon Thibaut & Opsommer Sophie, 2015
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
	 * Execute the greedy algorithm
	 * @param graph the graph 
	 * @return the list of the choosen edges
	 * @throws GreedyException
	 */
	public static ArrayList<Edge> execute(Graph graph) throws GreedyException
	{
		ArrayList<ArrayList<Node>> set = new ArrayList<ArrayList<Node>>();
		ArrayList<Edge> choosenEdge = new ArrayList<Edge>();

		graph.sortList();

		//Création des ensembles
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
				if(edge.getAlignment().getType() == AlignmentType.F1INCLUDEDTOF2 || edge.getAlignment().getType() == AlignmentType.F2INCLUDEDTOF1 )//traitement diff�rent pour les arcs o� un fragment est inclu � l'autre
				{
					//EX :
					//seq:  AAAAATTCGCGCGCGCGCGCTTCAAAAA
					//f1: AAAAATTCGCGCGCGC
					//f2: ATTC
					//f3: TTCAAAAA
					//f4: GCGCTTCAA
					//f2 inclus a f1
					//f1 > f4 > f3 pour reformer la sequence
					//si l'algo choisi l' arc suivant
					//f1 > f2
					//il peut plus choisir f1 > f4 car f1 est deja pris. Donc il va choisir
					//f2 > f3
					//Et f3>f4 car c'est le seul qu'il reste
					//Donc on a
					//f1 >f2 > f3 > f4 et c'est pas bon car on a pris un arc o� un frag est inclu � un autre
					inclusionManagement(set, edge, choosenEdge);
					
				}
				else
				{
					if(edge.getAlignment().getType() == AlignmentType.OTHER)
					{
						updateEdge(edge);
						choosenEdge.add(edge);
						unionSet(set, edge.getSource(), edge.getDestination());	
					}
				}
			}
			
			int i = 0;
			while(i < set.size())
			{
				if(set.get(i).isEmpty())
					set.remove(i);
				else
					i++;
			}
		
			if(set.size() == 1)
				break;

		}
		if(choosenEdge.isEmpty())
			choosenEdge.add(graph.getEdgeList().get(0));
		
		for(ArrayList<Node> n : set)
		{
			System.out.println(n.size());
			for(int i = 0; i < n.size(); i++)
			{
				System.out.println(n.get(i).getId());
			}
		}
		System.out.println("SIZE "+choosenEdge.size());
		for(int i = 0; i < choosenEdge.size(); i++)
		{
			System.out.println(choosenEdge.get(i).getSource().getId()+" "+choosenEdge.get(i).getDestination().getId()+" "+choosenEdge.get(i).getAlignment().getCost());
		}

		return choosenEdge;
	}


	private static void inclusionManagement(ArrayList<ArrayList<Node>> set, Edge edge, ArrayList<Edge> choosenEdge)throws GreedyException
	{
		if(edge.getAlignment().getType() == AlignmentType.F1INCLUDEDTOF2)//f1 inclus � f2
			f1IncluTof2(set, edge, choosenEdge);
		else
			f2IncluTof1(set, edge, choosenEdge);
		int i = 0;
		while(i < choosenEdge.size())
		{
			if(choosenEdge.get(i) == null)
				choosenEdge.remove(i);
			else
				i++;
		}
	}
	
	private static void f2IncluTof1(ArrayList<ArrayList<Node>> set, Edge edge, ArrayList<Edge> choosenEdge) throws GreedyException{
		
		//f2 ne peut plus �tre choisi donc in et out � true
		edge.getDestination().setIn(true);
		edge.getDestination().setOut(true);
		//IDEM pour le compl�mentaire
		if(edge.getDestination().getComplementaryNode() != null)
		{
			edge.getDestination().getComplementaryNode().setIn(true);
			edge.getDestination().getComplementaryNode().setOut(true);
		}
		
		//On ajoute f2 � la liste de fragments inclus � f1
		if(!edge.getSource().getIncludedNode().contains(edge.getDestination()))
		{
			edge.getSource().getIncludedNode().add(edge.getDestination());
			//on mets f2 dans l'ensemble de f1
			unionInclu(set,edge.getSource(), edge.getDestination());
		}
		else
			throw new GreedyException("Noeud destination d�j� dans l'ensemble du noeud source");

	}

	private static void f1IncluTof2(ArrayList<ArrayList<Node>> set, Edge edge, ArrayList<Edge> choosenEdge)throws GreedyException {
		//f1 ne peut plus �tre choisi donc in et out mis � true 
		edge.getSource().setIn(true);
		edge.getSource().setOut(true);
		//IDEM pour le compl�mentaire
		if(edge.getSource().getComplementaryNode() != null)
		{
			edge.getSource().getComplementaryNode().setIn(true);
			edge.getSource().getComplementaryNode().setOut(true);
		}

		//On ajoute f1 � la liste de fragments inclus � f2
		if(!edge.getDestination().getIncludedNode().contains(edge.getSource()))
		{
			edge.getDestination().getIncludedNode().add(edge.getSource());
			//on mets f1 dans l'ensemble de f2
			unionInclu(set, edge.getDestination(), edge.getSource());
		}
		else
			throw new GreedyException("Noeud source d�j� dans l'ensemble du noeud destination");
		
		
	}

	/**
	 * Update of the choosen edges and their complementary
	 * @param edge choosen edge
	 */
	private static void updateEdge(Edge edge) {
		edge.getSource().setOut(true);
		edge.getDestination().setIn(true);
		if(edge.getSource().getComplementaryNode() != null)
		{
			edge.getSource().getComplementaryNode().setIn(true);
			edge.getSource().getComplementaryNode().setOut(true);
		}
		if(edge.getDestination().getComplementaryNode() != null)
		{
			edge.getDestination().getComplementaryNode().setIn(true);
			edge.getDestination().getComplementaryNode().setOut(true);
		}
	}
	
	/**
	 * Check the node 1 and 2 don't belong to the same set
	 * @param node1 node 1
	 * @param node2 node 2
	 * @param set list of the sets of the nodes
	 * @return true if the node 1 and 2 belong to the same set 
	 */
	private static boolean checkSet(Node node1, Node node2, ArrayList<ArrayList<Node>> set)
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
	private static void unionSet(ArrayList<ArrayList<Node>> set, Node node1, Node node2) throws GreedyException
	{
		int indexNodeSet1 = -1, indexNodeSet2 = -1, indexComplementaryNode1 = -1, indexComplementaryNode2 = -1;
	
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
			throw new GreedyException("Noeud source et destination sont d�j� dans le m�me ensemble");
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
	private static void applyUnion(ArrayList<ArrayList<Node>> set, int indexNodeSet1,
			int indexNodeSet2, int indexComplementaryNode1,
			int indexComplementaryNode2) {
		
		ArrayList<Node> nodeSet1 = set.get(indexNodeSet1);
		ArrayList<Node> nodeSet2 = set.get(indexNodeSet2);

	
		
		//ajout des donn�es de l'ensemble contenant le noeud 2 dans l'ensemble du noeud 1
		for(int i = 0; i < nodeSet2.size(); i++)
		{
			if(!nodeSet1.contains(nodeSet2.get(i)))
				nodeSet1.add(nodeSet2.get(i));
		}

		if(indexComplementaryNode1 != -1)
		{
			ArrayList<Node> complementaryNodeSet1 = set.get(indexComplementaryNode1);
			ArrayList<Node> complementaryNodeSet2 = set.get(indexComplementaryNode2);
			//si l'ensemble contenant le compl�mentaire du noeud 1 est diff�rent de l'ensemble contenant le noeud 1
			if(indexNodeSet1 != indexComplementaryNode1)
			{
				//ajout des donn�es de l'ensemble contenant le compl�mentaire du noeud 1 dans l'ensemble du noeud 1
				for(int i = 0; i < complementaryNodeSet1.size(); i++)
				{
					if(!nodeSet1.contains(complementaryNodeSet1.get(i)))
						nodeSet1.add(complementaryNodeSet1.get(i));
				}
				//supprimer l'ensemble du compl�mentaire
				set.get(indexComplementaryNode1).clear();
			}
			//si l'ensemble contenant le compl�mentaire du noeud 2 est diff�rent de l'ensemble contenant le noeud 2
			if(indexNodeSet2 != indexComplementaryNode2)
			{
				//ajout des donn�es de l'ensemble contenant le compl�mentaire du noeud 2 dans l'ensemble du noeud 1
				for(int i = 0; i < complementaryNodeSet2.size(); i++)
				{
					if(!nodeSet1.contains(complementaryNodeSet2.get(i)))
						nodeSet1.add(complementaryNodeSet2.get(i));
				}
				//supprimer l'ensemble du compl�mentaire
				set.get(indexComplementaryNode2).clear();
			}
		}		
		//supprimer l'ensemble du noeud 2
		set.get(indexNodeSet2).clear();
	}
	
	private static void unionInclu(ArrayList<ArrayList<Node>> set, Node node1, Node node2) throws GreedyException
	{
		int indexNodeSet1 = -1, indexNodeSet2 = -1, indexComplementaryNode2 = -1;
		
		for(ArrayList<Node> nodeSet : set)
		{
			if(nodeSet.contains(node1))
				indexNodeSet1 = set.indexOf(nodeSet);
			if(nodeSet.contains(node2))
				indexNodeSet2 = set.indexOf(nodeSet);
			if(nodeSet.contains(node2.getComplementaryNode()))
				indexComplementaryNode2 = set.indexOf(nodeSet);
		}
		if(indexNodeSet1 == indexNodeSet2)
			throw new GreedyException("Noeud source et destination sont d�j� dans le m�me ensemble");
		
		ArrayList<Node> nodeSet1 = set.get(indexNodeSet1);
		ArrayList<Node> nodeSet2 = set.get(indexNodeSet2);

		

		//ajout des donn�es de l'ensemble contenant le noeud 2 dans l'ensemble du noeud 1
		for(int i = 0; i < nodeSet2.size(); i++)
		{
			if(!nodeSet1.contains(nodeSet2.get(i)))
				nodeSet1.add(nodeSet2.get(i));
		}
		
		if(indexComplementaryNode2 != -1)
		{
			ArrayList<Node> complementaryNodeSet2 = set.get(indexComplementaryNode2);

			//si l'ensemble contenant le compl�mentaire du noeud 2 est diff�rent de l'ensemble contenant le noeud 2
			if(indexNodeSet2 != indexComplementaryNode2)
			{
				//ajout des donn�es de l'ensemble contenant le compl�mentaire du noeud 2 dans l'ensemble du noeud 1
				for(int i = 0; i < complementaryNodeSet2.size(); i++)
				{
					if(!nodeSet1.contains(complementaryNodeSet2.get(i)))
						nodeSet1.add(complementaryNodeSet2.get(i));
				}
				//supprimer l'ensemble du compl�mentaire
				set.get(indexComplementaryNode2).clear();;
			}
		}
		//supprimer l'ensemble du noeud 2
		set.get(indexNodeSet2).clear();
	}
			
}
