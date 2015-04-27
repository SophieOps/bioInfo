package be.bioInfo.assembly.model;

/**
  * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class Node
{
	private Fragment data;
	private Node complementaryNode;
	private boolean in = false;  
	private boolean out = false;  
	private static int counter = 0;
	private int id;
	
	/**
	 * @return
	 */
	public Fragment getData() {
		return data;
	}

	/**
	 * @param data
	 */
	public void setData(Fragment data) {
		this.data = data;
	}

	/**
	 * @return
	 */
	public Node getComplementaryNode() {
		return complementaryNode;
	}

	/**
	 * @param complementaryNode
	 */
	public void setComplementaryNode(Node complementaryNode) {
		this.complementaryNode = complementaryNode;
	}

	/**
	 * @return
	 */
	public boolean isIn() {
		return in;
	}

	/**
	 * @param in
	 */
	public void setIn(boolean in) {
		this.in = in;
	}

	/**
	 * @return
	 */
	public boolean isOut() {
		return out;
	}

	/**
	 * @param out
	 */
	public void setOut(boolean out) {
		this.out = out;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Constructor.
	 * Do nothing, simple implementation.
	 */
	public Node() {
		this.id = this.counter;
		this.counter++;
	}
	
	
	
	
}
