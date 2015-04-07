package be.bioInfo.assembly.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;



import be.bioInfo.assembly.exception.FragmentException;

public class FragmentManager
{
	public ArrayList<Node> readFile(File selectedFile) throws FragmentException, FileNotFoundException
	{
        String code = "" ;
        ArrayList<Node> nodeList = new ArrayList<>();
        Node node = new Node();
		Node complementaryNode = new Node();
		
		Scanner fileScan = null;
    	fileScan = new Scanner(selectedFile);
    	
    	String line = fileScan.nextLine();
        Scanner lineScan;
        
        lineScan = new Scanner(line);
        lineScan.useLocale(Locale.FRENCH);  
   
	    fileScan = new Scanner(selectedFile);
	    line = fileScan.nextLine();
	    
	    while(fileScan.hasNextLine())
	    {
	    	line = fileScan.nextLine();
	        if(line.contains(">"))
	        {
	        	createFragmentNode(code, nodeList, node, complementaryNode);

	        	node = new Node();
	        	complementaryNode = new Node();	
	        	code ="";		 
	        }
	        else
	        {
	        	lineScan = new Scanner(line);
		        lineScan.useLocale(Locale.FRENCH);
		        code+=line;
		    }
	    }
	    createFragmentNode(code, nodeList, node, complementaryNode);
	    
	    return nodeList;
	}

	private void createFragmentNode(String code, ArrayList<Node> nodeList, Node node, Node complementaryNode) throws FragmentException 
	{
		Fragment fragment = new Fragment();
		fragment.setCode(code);
		
		Fragment complementaryFragment = new Fragment();
		complementaryFragment.setCode(computeComplementaryCode(code));
		
		node.setData(fragment);
		complementaryNode.setData(complementaryFragment);
		
		node.setComplementaryNode(complementaryNode);
		complementaryNode.setComplementaryNode(node);
		
		nodeList.add(node);
		nodeList.add(complementaryNode);

	}
	
	private String computeComplementaryCode(String code) throws FragmentException
	{
		String complementaryCode="";
		code = code.toUpperCase();
		for(int i = code.length(); i > 0 ; i--)
		{
			switch(code.charAt(i-1))
			{
				case 'A' : complementaryCode+= 'T';
					break;
				case 'T' : complementaryCode+= 'A';
					break;
				case 'C' : complementaryCode+= 'G';
					break;	
				case 'G' : complementaryCode+= 'C';
					break;
				default : throw new FragmentException("Illégal caractère dans le fragment");
			}
		}
		
		return complementaryCode;
	}

}
