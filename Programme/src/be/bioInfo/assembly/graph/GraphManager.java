package be.bioInfo.assembly.graph;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.bioInfo.assembly.algorithm.AlignmentAlgo;

/**
 * Construc the graph
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class GraphManager 
{
	/**
	 * Constructor.
	 * Do nothing, simple implementation.
	 */
	public GraphManager() {}
	
	/**
	 * construct a graph, which is store in a Graph object with all the
	 *  node depend on the cost of the alignment.
	 * @param nodeList : The list of nodes
	 * @return The graph
	 */
	public static Graph constructGraph(ArrayList<Node> nodeList, boolean computeComplementary)
	{
		Graph graph = new Graph();
		graph.setNodeList(nodeList);
		multiThread(graph,  computeComplementary);
		//System.out.println(graph.getEdgeList().size());
		System.out.println(graph.getNodeList().size());
		
		/*for(int i = 0; i < graph.getEdgeList().size();i++)
		{
			System.out.println(graph.getEdgeList().get(i).getSource().getData().getCode()+" "+graph.getEdgeList().get(i).getDestination().getData().getCode());
			System.out.println(graph.getEdgeList().get(i).getAlignment().getCode1());
			System.out.println(graph.getEdgeList().get(i).getAlignment().getCode2());
			System.out.println(graph.getEdgeList().get(i).getAlignment().getType());
		}*/
		return graph;
	}
	
	/**
	 * Multi thread the construction of the graph
	 * @param graph : The graph
	 */
	private static void multiThread(Graph graph, boolean computeComplementary)
	{
		int nbThread = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(nbThread);
        int size = graph.getNodeList().size();
        int nbEdge=0;
        if(computeComplementary == true)
        	nbEdge = size * (size-2); //-1 pour soi et -1 pour son complémentaire
        else
        	nbEdge = size*(size-1);//-1 pour soi
        EdgeManager[] edgeManager = new EdgeManager[nbThread];
        
        for (int i = 0; i < nbThread; i++) {
            edgeManager[i] = new EdgeManager(graph,i,nbThread);
            service.execute(edgeManager[i]);
        }
        service.shutdown();
        
        while(!service.isTerminated())
        {
            try 
            {
                Thread.sleep(500);
            } 
            catch (InterruptedException e) 
            {
            }
            int nbDone = 0;
            for(EdgeManager ec : edgeManager)
            {
                nbDone += ec.getNbEdgeDone();
            }
            
            System.out.println(nbDone+"/"+nbEdge+" arcs construit");
        }
	}

}
