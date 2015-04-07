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
	 * @param src
	 * @param dest
	 * @param weight1
	 */
	public Edge(Node src, Node dest, int weight1)
	{
		this.source = src;
		this.destination = dest;
		this.weight = weight1;
	}

	/**
	 * @return
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
	 * @return
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
	 * @return
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight1
	 */
	public void setWeight(int weight1) {
		this.weight = weight1;
	}
	
	/**
	 * @param edge
	 * @return
	 */
	public int CompareTo(Edge edge)
    {
       int resultat = 0;

       if (this.weight > edge.getWeight()) {
            resultat = -11;
        }
       if (this.weight < edge.getWeight()) {
            resultat = 1;
        }
 
       return resultat;
    }
	
}
