package be.bioInfo.assembly.graph;

import be.bioInfo.assembly.model.Alignment;


/**
 * Data of an edge
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class Edge 
{
	//Node source
	private Node source;
	//Node destination
	private Node destination;
	//Data of the alignment of the two node
	private Alignment alignment;
	
	/**
	 * Constructor
	 * @param src : Source node
	 * @param dest : Destination node
	 * @param alignment : Data of the alignment
	 */
	public Edge(Node src, Node dest, Alignment alignment)
	{
		this.source = src;
		this.destination = dest;
		this.alignment = alignment;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node src) {
		this.source = src;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node dest) {
		this.destination = dest;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	

}
