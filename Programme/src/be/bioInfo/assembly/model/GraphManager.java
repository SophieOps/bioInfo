package be.bioInfo.assembly.model;

import java.util.ArrayList;

import be.bioInfo.assembly.algorithm.AlignmentAlgo;

/**
  * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class GraphManager 
{
	private AlignmentAlgo alignmentAlgo = new AlignmentAlgo();

	/**
	 * Constructor.
	 * Do nothing, simple implementation.
	 */
	public GraphManager() {}
	
	/**
	 * construct a graph, which is store in a Graph object with all the
	 *  node depend on the weight of the alignment of each fragment.
	 * @param nodeList
	 * @return
	 */
	public Graph constructGraph(ArrayList<Node> nodeList)
	{
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		
		//Pour chaque noeud, on construit ses arcs
		for(int i = 0; i < nodeList.size(); i++)
		{
			//On ne construit pas les arcs entre le noeud i et les noeuds qui se trouvent avant car ils ont déjà été traités avant
			for(int j = i+1; j < nodeList.size(); j++)
			{
				if(nodeList.get(i).getComplementaryNode() != nodeList.get(j) )	//Si le noeud à traiter n'est pas le complémentaire du noeud courant
				{
					alignmentAlgo.computeAlignmentMax(nodeList.get(i).getData(), nodeList.get(j).getData());
					Edge edge = new Edge(nodeList.get(i), nodeList.get(j), alignmentAlgo.getMaxValueRow());//poids = valeur max de la dernière ligne
					Edge edgeBis = new Edge(nodeList.get(j), nodeList.get(i), alignmentAlgo.getMaxValueColumn());//poids = valeur max de la dernière colonne
					edgeList.add(edge);
					edgeList.add(edgeBis);
				}
			}
			
		}
		Graph graph = new Graph(nodeList, edgeList);
		return graph;
		
	}
	

}
