package be.bioInfo.assembly.graph;

import java.util.ArrayList;

import be.bioInfo.assembly.model.Fragment;

/**
 * Data of a node
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class Node
{
	//The fragment in the node
	private Fragment data;
	//The complementary node
	private Node complementaryNode;
	//Boolean to know if we come in the node or not
	private boolean in;  
	////Boolean to know if we go out of the node or not
	private boolean out;  
	//List of the included node
	private ArrayList<Node> includedNode;
	

	public Fragment getData() {
		return data;
	}


	public void setData(Fragment data) {
		this.data = data;
	}

	public Node getComplementaryNode() {
		return complementaryNode;
	}


	public void setComplementaryNode(Node complementaryNode) {
		this.complementaryNode = complementaryNode;
	}

	public boolean isIn() {
		return in;
	}

	public void setIn(boolean in) {
		this.in = in;
	}

	public boolean isOut() {
		return out;
	}

	public void setOut(boolean out) {
		this.out = out;
	}
	
	public ArrayList<Node> getIncludedNode() {
		return includedNode;
	}

	public void setIncludedNode(ArrayList<Node> includedNode) {
		this.includedNode = includedNode;
	}

	
	/**
	 * Constructor.
	 * Do nothing, simple implementation.
	 */
	public Node() 
	{
		includedNode = new ArrayList<Node>();
		in = false;  
		out = false;
	}
	
	
	
	
}
