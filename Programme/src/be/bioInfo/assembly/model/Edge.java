package be.bioInfo.assembly.model;

public class Edge 
{
	private Node source;
	private Node destination;
	private int weight;
	
	public Edge(Node source, Node destination, int weight)
	{
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
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
