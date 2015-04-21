package be.bioInfo.assembly.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import be.bioInfo.assembly.algorithm.AlignmentAlgo;

public class ChainManager 
{
	private AlignmentAlgo alignmentAlgo;
	private ArrayList<String> alignmentList;
	private int saveFatherAlignment;
	private File file;
	private PrintWriter pw;
	
	public ChainManager()
	{
		alignmentAlgo = new AlignmentAlgo();
		alignmentList = new ArrayList<String>();
		file = new File("chaine.txt");
		
	}

	//Fonctionne pas :(
	public void constructChain(ArrayList<Edge> edgeList)
	{
		try 
		{
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			
			//traiter chaque arc
			while(edgeList.size() != 0)
			{
				int i;
				//on cherche l'arc dont le fragment source n'apparait pas dans un autre arc comme fragment destination
				for(i = 0; i < edgeList.size(); i++)
				{
					int j;
					for(j = i+1; j < edgeList.size() && edgeList.get(i).getSource() != edgeList.get(j).getDestination(); j++)
					{
						
					}
					
					if(j == edgeList.size())
						break;
				}

				constructAlignment(edgeList.get(i).getSource().getData(), edgeList.get(i).getDestination().getData());
				edgeList.remove(i);
			}
			pw.println("##################################################################");
			pw.println(constructor());
			pw.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	//ATTENTION pas correct =S. ne prends pas en compte les gaps
	//EX 1:
	//GCCA
    //  C-GTTA
	
    //CGTTA
    //CGTTACC
	
    //GCCA
    //  C-GTTA
    //  C-GTTACC
	
	//Ex2 :
	//GCCA
    //  CAGTTA
	//C-AGTTA
	//CCAGTTAA
	//GCC-A
    //  C-AGTTA
    //  CCAGTTAA
	//Ces exemples ne sont pas pris en compte dans la construction :(
	private void constructAlignment(Fragment f1, Fragment f2) {
		String alignment="";
		
		//On ajoute des " " pour un joli affichage dans le fichier
		if(alignmentList.size() != 0)
		{
			for(int i = 0; i < alignmentList.get(saveFatherAlignment).lastIndexOf(" ")+1; i++)
			{
				alignment+=" ";
			}
		}

		//on construit l'alignement entre f1 et f2
		alignmentAlgo.buildAlignment(f1, f2);
		String saveAlignmentF2 = alignmentAlgo.getAlignmentList().get(1);
		
		//On ajoute l'alignement de f1
		if(alignmentList.contains(alignment+f1.getCode()) == false)
		{
			pw.println(alignment+alignmentAlgo.getAlignmentList().get(0));
			alignmentList.add(alignment+alignmentAlgo.getAlignmentList().get(0));
		}

		//On construit les alignement des fragments inclus à f1 avec f1
		for(int i = 0; i < f1.getFragmentInclu().size(); i++)
		{
			
			alignmentAlgo.buildAlignment(f1, f1.getFragmentInclu().get(i));
			if(!alignmentList.contains(alignmentAlgo.getAlignmentList().get(1)))
			{
				alignmentList.add(alignment+alignmentAlgo.getAlignmentList().get(1));
				pw.println(alignment+alignmentAlgo.getAlignmentList().get(1));
			}
		}
		
		//On ajoute l'alignement de f2
		if(alignmentList.contains(alignment+saveAlignmentF2) == false)
		{
			pw.println(alignment+saveAlignmentF2);
			alignmentList.add(alignment+saveAlignmentF2);
		}
	
		saveFatherAlignment = alignmentList.size()-1;
		alignment="";
		//On ajoute des " " pour faire un joli affichage dans le fichier
		for(int i = 0; i < alignmentList.get(saveFatherAlignment).lastIndexOf(" ")+1; i++)
		{
			alignment+=" ";
		}
		
		//On construit les alignements des fragments inclus à f2 avec f2
		for(int i = 0; i < f2.getFragmentInclu().size(); i++)
		{
			alignmentAlgo.buildAlignment(f2, f2.getFragmentInclu().get(i));
			if(!alignmentList.contains(alignmentAlgo.getAlignmentList().get(1)))
			{
				alignmentList.add(alignment+alignmentAlgo.getAlignmentList().get(1));
				pw.println(alignment+alignmentAlgo.getAlignmentList().get(1));
			}
		}
	}
	
	private String constructor()
	{
		int nbA, nbT, nbC, nbG, nbGap, cptEnd;
		boolean charToTreat = true;
		String chain = "";
		int i = 0;
		
		while(charToTreat == true)
		{
			nbA = 0;
			nbT = 0;
			nbC = 0;
			nbG = 0;
			nbGap = 0;
			cptEnd = 0;
			
			//On regarde le caractère apparaissant le + de fois à la position i. ce sera le caractère de la super chaine à la position i
			for(int j = 0; j < alignmentList.size(); j++)
			{
				if(alignmentList.get(j).length() > i)
				{
					switch(alignmentList.get(j).charAt(i))
					{
						case 'A' : nbA++;
							break;
						case 'T' : nbT++;
							break;
						case 'C' : nbC++;
							break;
						case 'G' : nbG++;
							break;
						case '-' : nbGap++;
							break;
						default : break;
					}		
				}
				else
				{
					cptEnd++;
				}
			}
			
			if(cptEnd == alignmentList.size())
				charToTreat = false;
			else
			{
				String charactereMax="T";
				int max=nbT;
				if(nbA >= nbT)
				{
					max = nbA;
					charactereMax = "A";
				}
				if(nbC >= max)
				{
					max = nbC;
					charactereMax = "C";
				}
				if(nbG >= max)
				{
					max = nbG;
					charactereMax = "G";
				}
				if(nbGap>max)
				{
					max = nbGap;
					charactereMax = "-";
				}
				chain+=charactereMax;
			}
			i++;
		}
		
		return chain;
	}
	
	
	
}
