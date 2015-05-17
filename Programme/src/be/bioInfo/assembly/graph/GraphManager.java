package be.bioInfo.assembly.graph;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.bioInfo.assembly.algorithm.AlignmentAlgo;
import be.bioInfo.assembly.view.MainFrame;

/**
 * Construct the graph
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
	 * @praram computeComplementary : is true if the nodes are associate with their complementary
	 * @return The graph
	 */
	public static Graph constructGraph(ArrayList<Node> nodeList, boolean computeComplementary)
	{
		Graph graph = new Graph();
		graph.setNodeList(nodeList);
		multiThread(graph,  computeComplementary);

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
        int nbEdge = 0;
        if(computeComplementary == true)
        	nbEdge = size * (size-2); //-1 pour soi et -1 pour son complementaire
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
            
            System.out.println(nbDone+"/"+nbEdge+" arcs construits");
            //MainFrame.setValueProgressBar((int)(nbDone/nbEdge)*1000);
        }
	}

}
