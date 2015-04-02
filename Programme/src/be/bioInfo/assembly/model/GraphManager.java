package be.bioInfo.assembly.model;

import java.util.ArrayList;

import be.bioInfo.assembly.algorith.AlignmentAlgo;

public class GraphManager 
{
	private AlignmentAlgo alignmentAlgo = new AlignmentAlgo();
	
	public Graph constructGraph(ArrayList<Node> nodeList)
	{
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		
		for(int i = 0; i < nodeList.size(); i++)
		{
			for(int j = i+1; j < nodeList.size(); j++)
			{
				if(nodeList.get(i).getComplementaryNode() != nodeList.get(j))
				{
					int weight = alignmentAlgo.computeAlignmentMax(nodeList.get(i).getData(), nodeList.get(j).getData());
					Edge edge = new Edge(nodeList.get(i), nodeList.get(j), weight);
					Edge edgeBis = new Edge(nodeList.get(j), nodeList.get(i), weight);
					edgeList.add(edge);
					edgeList.add(edgeBis);
				
				}
				
			}
		}
		

		Graph graph = new Graph(nodeList, edgeList);
		return graph;
		
	}
	

}
