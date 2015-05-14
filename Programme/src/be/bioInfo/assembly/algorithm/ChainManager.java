package be.bioInfo.assembly.algorithm;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import be.bioInfo.assembly.graph.Edge;
import be.bioInfo.assembly.model.Alignment;

/**
 * Super chain manager
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class ChainManager 
{
	private static File file;
	private static PrintWriter pw;

	public ChainManager(){
		
	}

	public static void constructChain(ArrayList<Edge> edgeList)
	{
		String superChain;
		ArrayList<short[]> values = new ArrayList<>();
		int sequenceIndex = 0;
		
		int i;

		//Recherche du premier arc
		for(i = 0; i < edgeList.size(); i++)
		{
			if(edgeList.get(i).getSource().isIn() == false && edgeList.get(i).getSource().isOut() == true && edgeList.get(i).getDestination().isIn() == true && edgeList.get(i).getDestination().isOut() == true)
			{
				break;
			}
		}
		
		/*if(i == edgeList.size())
			i = 0;*/
		Edge currentEdge = edgeList.get(i);
		edgeList.remove(i);
		
		boolean edgeStillNotTreated = true;
		do
		{
			//Si on rentre dans le if on est dans le dernier arc à traiter
			if(edgeList.isEmpty())
				edgeStillNotTreated = false;
			int currentSeqIndex = sequenceIndex;
			//System.out.println(currentEdge.getSource().getData().getCode()+" "+currentEdge.getDestination().getData().getCode());
			/*System.out.println(currentEdge.getAlignment().getCode1());
			System.out.println(currentEdge.getAlignment().getCode2());*/
			for (int j = 0; j < currentEdge.getAlignment().getCode1().length(); j++)
			{
				//Création d'un tableau contenant le nombre de A,T,C,G,- pour la currentSeqIndex position dans la super chaine
				//On rajoute une cellule pour savoir si on est en présence d'un gap
				if(currentSeqIndex>=values.size())
				{
					values.add(new short[6]);
	            }
				//Si on est pas en présence d'un gap à la currentSeqIndex position dans la super chaine
				if(values.get(currentSeqIndex)[5] != 1) 
				{
					short[] value = values.get(currentSeqIndex);
	                addValue(value, currentEdge.getAlignment().getCode1().charAt(j));//Ajoute 1 à la cellule représentant la j ème lettre de code1
	                addValue(value, currentEdge.getAlignment().getCode2().charAt(j));//Ajoute 1 à la cellule représentant la j ème lettre de code2
	                
	                //Si on a un gap à la j ème position de code 2
	                if (currentEdge.getAlignment().getCode2().charAt(j) == Alignment.GAP) 
	                {
	                	values.get(currentSeqIndex)[5] = 1;//Spécifie qu'on est en présence d'un gap
	                }
	            }
				else
				{
					//Propagation des gaps 
					j--;
					currentEdge.getAlignment().setStartIndex(currentEdge.getAlignment().getStartIndex()+1);
				}
				currentSeqIndex++;
			}
			//AAAATTTT*****  1
			//****TTTTGGGGG  2
			
			//TTTTGGGGG****  3
			//*******GGCCCC  4
			//Permet de se positionner sur le début de l'alignement donc sur le premier T de 2
			//Ensuite on se positionnera sur le premier G de 4
			sequenceIndex+=currentEdge.getAlignment().getStartIndex();
			
			
			//Recherche de l'arc suivant à traiter
			int j;
			for(j = 0; j < edgeList.size(); j++)
			{
				if(currentEdge.getDestination() == edgeList.get(j).getSource())
				{
					currentEdge = edgeList.get(j);
					edgeList.remove(j);
					break;
				}
			}
			/*if(j == edgeList.size() && j!=0)
			{
				currentEdge = edgeList.get(0);
				edgeList.remove(0);
			}*/
				
			
		}while(edgeStillNotTreated == true);
		
		superChain = (constructSuperChain(values));
		try 
		{
			file = new File("superChaine.fasta");
			pw = new PrintWriter(new FileWriter(file));
			pw.println(superChain);
			pw.close();
			
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Construct the chain
	 * @param bestValues : The list of the possible best value for each position 
	 * @return
	 */
	 private static String constructSuperChain(ArrayList<short[]> bestValues) {
	        StringBuilder superChain = new StringBuilder();
	        for(short[] values : bestValues){
	            superChain.append(mostPresentValue(values));
	           
	        }
	        return superChain.toString();
	    }

	 /**
	  * Research the most represented value for the given position
	  * @param values : The board containing the number of presence of each value
	  * @return
	  */
	    private static char mostPresentValue(short[] values) {
	        char max = 'A';
	        short scoreMax = values[0];
	        if(values[1] > scoreMax){
	            max = 'T';
	            scoreMax = values[1];
	        }
	        if(values[2] > scoreMax){
	            max = 'C';
	            scoreMax = values[2];
	        }
	        if(values[3] > scoreMax){
	            max = 'G';
	            scoreMax = values[3];
	        }
	        if(values[4] > scoreMax){
	            max = Alignment.GAP;
	            scoreMax = values[4];
	        }
	        return max;
	    }

	    /**
	     * Increase the number of presence of the current charactere for the given position
	     * @param value : The board containing the number of presence of each charactere for the given position
	     * @param currentChar : The current charactere
	     */
	    private static void addValue(short[] value, char currentChar) {
	        switch (currentChar) {
	            case 'A':
	                value[0]++;
	                break;
	            case 'T':
	                value[1]++;
	                break;
	            case 'C':
	                value[2]++;
	                break;
	            case 'G':
	                value[3]++;
	                break;
	            case Alignment.GAP:
	                value[4]++;
	                break;
	        }
	    }
	
	
	
}
