package be.bioInfo.assembly.model;

import java.util.ArrayList;

import be.bioInfo.assembly.algorithm.AlignmentAlgo;

/**
 * @author Cambier Robin & Opsommer Sophie, 2015
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
		
		for(int i = 0; i < nodeList.size(); i++)//parcour toute la liste des fragments complémentaires
		{
			for(int j = i+1; j < nodeList.size(); j++)//parcour toute la liste des fragments initiaux
			{
				if(nodeList.get(i).getComplementaryNode() != nodeList.get(j) )
						//&& (!(nodeList.get(i).getData().getCode().contains((nodeList.get(j).getData().getCode())))) 
						//&& (!(nodeList.get(j).getData().getCode().contains((nodeList.get(i).getData().getCode())))) ) 
						// ????? pour les fragments inclus
				{
					int weight = alignmentAlgo.computeAlignmentMax(nodeList.get(i).getData(), nodeList.get(j).getData());
					Edge edge = new Edge(nodeList.get(i), nodeList.get(j), weight);
					Edge edgeBis = new Edge(nodeList.get(j), nodeList.get(i), weight); //????????? 
					edgeList.add(edge);
					edgeList.add(edgeBis); //????????????
				}
			}
		}
		Graph graph = new Graph(nodeList, edgeList);//besoin de retourner les deux ?????
		return graph;
		
	}
	

}
