package be.bioInfo.assembly.graph;

import java.util.ArrayList;

import be.bioInfo.assembly.algorithm.AlignmentAlgo;
import be.bioInfo.assembly.model.Alignment;
import be.bioInfo.assembly.model.FragmentManager;

/**
 * Construct the edges of the graph
 * @author Thibaut
 *
 */
public class EdgeManager implements Runnable
{

	//The graph
	private Graph graph;
	//The number of thread
    private int nbThread;
    //The id of the thread
    private int threadId;
    //The number of edge created
    private int nbEdgeDone;
    
    /**
     * Constructor
     * @param graph : The graph
     * @param threadId : The thread ID
     * @param nbThread : The number of thread
     */
    public EdgeManager(Graph graph, int threadId, int nbThread) 
    {
        this.graph = graph;
        this.threadId = threadId;
        this.nbThread = nbThread;
        this.nbEdgeDone = 0;
    }
    
	@Override
	public void run()
	{
		 int size = graph.getNodeList().size();
		 
		 for(int i = threadId;i<size;i+=nbThread)
		 {
	            for (int j = i+1; j < size; j++) 
	            {
	            	Node source = graph.getNodeList().get(i);
	            	Node dest = graph.getNodeList().get(j);
	            	if(source.getComplementaryNode() != dest) 
	                {
	                    AlignmentAlgo alignmentAlgo = new AlignmentAlgo(source.getData(), dest.getData(), false);
	                    ArrayList<Alignment> alignmentList = alignmentAlgo.computeAlignmentMax();

	                    synchronized (graph)
	                    {
	                        Edge edge = new Edge(source, dest, alignmentList.get(0));
	                        graph.getEdgeList().add(edge);
	                        Edge reverseEdge = new Edge(dest, source, alignmentList.get(1));
	                        graph.getEdgeList().add(reverseEdge);
	                    }
	                    nbEdgeDone +=2;
	                }
	            }
	        }
	}
	
	 public int getNbEdgeDone() 
	 {
		 return nbEdgeDone;
	 }

}
