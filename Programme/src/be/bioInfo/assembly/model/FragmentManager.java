package be.bioInfo.assembly.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;



import be.bioInfo.assembly.exception.FragmentException;

/**
 * Read the input file and create an arrayList of Node with all 
 * fragments and the complementary of the fragment.
 * 
 * @author Cambier Robin & Opsommer Sophie, 2015
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
	 * @param selectedFile
	 * @return
	 * @throws FragmentException
	 * @throws FileNotFoundException
	 */
	public ArrayList<Node> readFile(File selectedFile) throws FragmentException, FileNotFoundException
	{
        String code = "" ;
        ArrayList<Node> nodeList = new ArrayList<>();
        Node node = new Node();
		Node complementaryNode = new Node();
		
		Scanner fileScan = null;
    	fileScan = new Scanner(selectedFile);
    	
    	String line = fileScan.nextLine(); // contient la première ligne du fichier avec les infos : "Groupe-num_groupe Collection num_collection Longueur longueur_sequence_cible"
        //Scanner lineScan = new Scanner(line);
        //lineScan.useLocale(Locale.FRENCH);  
        
	    while(fileScan.hasNextLine())
	    {
	    	line = fileScan.nextLine();
	        if(line.contains(">"))
	        {
	        	//if(code.length() == 0){
	        	createFragmentNode(code, nodeList, node, complementaryNode);
	        	
	        	node = new Node();
	        	complementaryNode = new Node();
	        		
	        	code ="";
	        	//} 
	        }
	        else
	        {
	        	Scanner lineScan = new Scanner(line);
		        lineScan.useLocale(Locale.FRENCH);
		        code+=lineScan.next();
		        lineScan.close();
		    }
	    }
	    createFragmentNode(code, nodeList, node, complementaryNode);
	    //fermeture des Scanner
	    fileScan.close();
	    
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
		//node.getComplementaryNode().setComplementaryNode(node); // pas sur que dans ce cas-là, le noeud complémentaire relié à node soit changé aussi.
		
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
