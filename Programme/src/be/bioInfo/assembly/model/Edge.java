package be.bioInfo.assembly.model;

/**
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class Edge 
{
	private Node source;
	private Node destination;
	private int weight;
	
	/**
	 * @param src the node source of the edge
	 * @param dest the node destination of the edge
	 * @param weight1 the weight of the edge
	 */
	public Edge(Node src, Node dest, int weight)
	{
		this.source = src;
		this.destination = dest;
		this.weight = weight;
	}

	/**
	 * @return the node source of the edge
	 */
	public Node getSource() {
		return source;
	}

	/**
	 * @param src 
	 */
	public void setSource(Node src) {
		this.source = src;
	}

	/**
	 * @return the node destination of the edge
	 */
	public Node getDestination() {
		return destination;
	}

	/**
	 * @param dest
	 */
	public void setDestination(Node dest) {
		this.destination = dest;
	}

	/**
	 * @return the weight of the edge
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight1
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	/**
	 * @param edge
	 * @return
	 */
	public int CompareTo(Edge edge)
    {
       int resultat = 0;

       if (this.weight > edge.getWeight()) {
            resultat = -1;
        }
       if (this.weight < edge.getWeight()) {
            resultat = 1;
        }
 
       return resultat;
    }
	
}
