package be.bioInfo.assembly.model;

/**
 * @author Cambier Robin & Opsommer Sophie, 2015
 *
 */
public class Node
{
	private Fragment data;
	private Node complementaryNode;
	private boolean in = false;  // ?????
	private boolean out = false;  // ?????
	
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
	 * Constructor.
	 * Do nothing, simple implementation.
	 */
	public Node() {}
	
	
	
	
}
