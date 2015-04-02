package be.bioInfo.assembly.model;


public class Node
{
	private Fragment data;
	private Node complementaryNode;
	private boolean in=false, out=false;
	
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
	
	
	
	
}
