package be.bioInfo.assembly.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.JOptionPane;

import be.bioInfo.assembly.exception.FragmentException;
import be.bioInfo.assembly.graph.Node;

/**
 * Read the input file and create an arrayList of Node with all 
 * fragments and the complementary of the fragment.
 * 
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class FragmentManager
{
		
	/**
	 * Constructor.
	 * Do nothing, simple implementation.
	 */
	public FragmentManager() {}

	/**
	 * Read the file and create the fragments, the nodes and compute their complementary or not
	 * @param selectedFile : The file to read
	 * @param computeComplementary : Is true if we need to compute complementary. 
	 * @return : List of nodes
	 * @throws FragmentException
	 * @throws FileNotFoundException
	 */
	public static ArrayList<Node> readFile(File selectedFile, boolean computeComplementary) throws FragmentException, FileNotFoundException
	{
        String code = "" ;
        ArrayList<Node> nodeList = new ArrayList<>();
        Node node = new Node();
		Node complementaryNode = new Node();
		
		Scanner fileScan = null;
    	fileScan = new Scanner(selectedFile);
    	String line = fileScan.nextLine(); // contient la premiere ligne du fichier avec les infos : "Groupe-num_groupe Collection num_collection Longueur longueur_sequence_cible"
 
	    while(fileScan.hasNextLine())
	    {
	    	line = fileScan.nextLine();
	        if(line.contains(">"))
	        {
	        	if(computeComplementary == true)
	        		createFragmentNode(code, nodeList, node, complementaryNode);
	        	else
	        		createFragmentNode(code, nodeList, node);
	        	
	        	node = new Node();
	        	complementaryNode = new Node();
	        	code ="";	
	        }
	        else
	        {
	        	Scanner lineScan = new Scanner(line);
		        lineScan.useLocale(Locale.FRENCH);
		        code+=lineScan.next();
		        lineScan.close();
		    }
	    }
	    
	    if(computeComplementary == true)
    		createFragmentNode(code, nodeList, node, complementaryNode);
    	else
    		createFragmentNode(code, nodeList, node);
	    //fermeture des Scanner
	    fileScan.close();
	    
	    return nodeList;
	}

	/**
	 * Create the fragments, nodes and their complementary
	 * @param code : The code read from the file
	 * @param nodeList : The list of nodes
	 * @param node : The node to create
	 * @param complementaryNode : The complementary Node to create
	 * @throws FragmentException
	 */
	private static void createFragmentNode(String code, ArrayList<Node> nodeList, Node node, Node complementaryNode) throws FragmentException 
	{
		Fragment fragment = new Fragment(code);
		Fragment complementaryFragment = new Fragment(computeComplementaryCode(code));

		complementaryNode.setData(complementaryFragment);
		node.setData(fragment);

//		node.setComplementaryNode(complementaryNode);
//		complementaryNode.setComplementaryNode(node);
		nodeList.add(node);
		nodeList.add(complementaryNode);
	}
	
	/**
	 * Create the fragments and the nodes
	 * @param code : The code read from the file
	 * @param nodeList : The list of nodes
	 * @param node : The node to create
	 * @throws FragmentException
	 */
	private static void createFragmentNode(String code, ArrayList<Node> nodeList, Node node) throws FragmentException 
	{
		Fragment fragment = new Fragment(code);
		node.setData(fragment);
		nodeList.add(node);
	}
	
	/**
	 * Compute the complementary of a fragment
	 * @param code : The code read from the file
	 * @return The complementary of the code
	 * @throws FragmentException
	 */
	private static String computeComplementaryCode(String code)
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
				default : complementaryCode+= 'C';
					break;
			}
		}
		return complementaryCode;
	}
}
