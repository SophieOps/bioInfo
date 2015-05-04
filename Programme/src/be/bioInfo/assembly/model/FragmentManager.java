package be.bioInfo.assembly.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;




import javax.swing.JOptionPane;

import be.bioInfo.assembly.exception.FragmentException;

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
	 * Read the file and create the fragments, the nodes and compute their complementary
	 * @param selectedFile the file to read
	 * @param withcompl true if the file contains all the complementary fragment
	 * @return the list of the nodes for the creation of the graph
	 * @throws FragmentException
	 * @throws FileNotFoundException
	 */
	public ArrayList<Node> readFile(File selectedFile, boolean withCompl) throws FragmentException, FileNotFoundException
	{
		ArrayList<Node> nodeList = new ArrayList<>();
			String code = "" ;
			Node node = new Node();
			Scanner fileScan = null;
			fileScan = new Scanner(selectedFile);
			String line = fileScan.nextLine(); // contient la première ligne du fichier avec les infos : "Groupe-num_groupe Collection num_collection Longueur longueur_sequence_cible"
		if (!withCompl){
			Node complementaryNode = new Node();
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
					Scanner lineScan = new Scanner(line);
					lineScan.useLocale(Locale.FRENCH);
					code+=lineScan.next();
					lineScan.close();
				}
			}
			createFragmentNode(code, nodeList, node, complementaryNode);
			//fermeture des Scanner
			fileScan.close();
		}else{
			//TODO : Vérifié la gestion du withcompl pour le traitement des fichiers qui contiennent les fragments complémentaires. 
			while(fileScan.hasNextLine())
			{
				line = fileScan.nextLine();
				if(line.contains(">"))
				{
					createFragmentNode(code, nodeList, node);
					node = new Node();
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
			createFragmentNode(code, nodeList, node);
			//fermeture des Scanner
			fileScan.close();
			
			associateComplementaryNode(nodeList);
		}

		return nodeList;
	}

	/**
	 * Create the fragments and the nodes
	 * @param code the code of the fragment
	 * @param nodeList the list of the nodes
	 * @param node a node
	 * @param complementaryNode
	 * @throws FragmentException
	 */
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
	
	/**
	 * Create the fragment and the node
	 * @param code the code of the fragment
	 * @param nodeList the list of the nodes
	 * @param node a node
	 * @throws FragmentException
	 */
	private void createFragmentNode(String code, ArrayList<Node> nodeList, Node node) throws FragmentException 
	{
		Fragment fragment = new Fragment();
		fragment.setCode(code);
		Fragment complementaryFragment = new Fragment();
		complementaryFragment.setCode(computeComplementaryCode(code));
		node.setData(fragment);
		nodeList.add(node);
	}
	
	/**
	 * Associate the node with is complementary node
	 * @param nodeList the list of the nodes
	 * @throws FragmentException
	 */
	private void associateComplementaryNode(ArrayList<Node> nodeList) throws FragmentException{
		for (Node node : nodeList){
			for(Node nodeCompl : nodeList){
				if((!node.equals(nodeCompl)) 
						&& (computeComplementaryCode(node.getData().getCode()).equals(nodeCompl.getData().getCode()))){
					node.setComplementaryNode(nodeCompl);
					nodeCompl.setComplementaryNode(node); 
				}
			}
		}
		for(Node node : nodeList){
			if(!node.getComplementaryNode().getComplementaryNode().equals(node)){
				 JOptionPane.showMessageDialog(null, "Il y a un fragment non associé" + node.toString(), "InfoBox: Erreur lors de l'association des noeuds complémentaires inversé", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	/**
	 * Compute the complementary of a fragment
	 * @param code
	 * @return
	 * @throws FragmentException
	 */
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
