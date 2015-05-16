package be.bioInfo.assembly.algorithm;

import java.util.ArrayList;

import be.bioInfo.assembly.model.Alignment;
import be.bioInfo.assembly.model.AlignmentType;
import be.bioInfo.assembly.model.Fragment;


/**
* @author Watillon Thibaut & Opsommer Sophie, 2015
*
*/
public class AlignmentAlgo
{
	private static final int G = -2;
	private static final int P = 1;
	private short[][] matrix;
	private int maxValueColumn, maxIndexColumn, maxValueRow,  maxIndexRow;
	private ArrayList<Alignment> alignmentList;
	private Fragment f1;
    private Fragment f2;
    private boolean insertAlignment;
	
	public AlignmentAlgo(Fragment f1, Fragment f2, boolean insertAlignment)
	{
		this.f1 = f1;
		this.f2 = f2;
		this.insertAlignment =  insertAlignment;
	}

	private void execute()
	{
		//System.out.println(f1.getCode()+" "+f2.getCode());
		//ALGO
		for(int i = 1; i < f1.getCode().length()+1; i++)
		{
			for(int j = 1; j < f2.getCode().length()+1; j++)
			{
				int p = score(f1.getCode().charAt(i-1),f2.getCode().charAt(j-1));
				matrix[i][j] = (short)Math.max(Math.max(matrix[i-1][j]+G,matrix[i][j-1]+G), matrix[i-1][j-1]+p);
			}
		}
		
		//AFFICHAGE MATRICE
		/*for(int i = 0; i < f1.getCode().length()+1; i++)
		{
			for(int j = 0; j < f2.getCode().length()+1; j++)
			{
				System.out.print(matrix[i][j]+" ");
			}
			System.out.print("\n");
		}*/
	}

	/**
	 * Calculate the similarity between 2 char
	 * @param c1 the charactere of f1 at  i-1
	 * @param c2 the charactere of f2 at j-1�me
	 * @return The similarity value
	 */
	private int score(char c1, char c2)
	{
		if(c1 == c2)
			return P;
		return -P;
	}

	/**
	 * Compute the index and the value of the highest value of the last row
	 */
	private void computeSuffixLength() {
		maxValueColumn = Integer.MIN_VALUE;
		maxIndexColumn = -1;

		//Regarde dans la derni�re ligne la colonne avec la + grande valeur
		for(int j = 0; j < f2.getCode().length()+1; j++)
		{
			if(matrix[f1.getCode().length()][j] > maxValueColumn)
			{
				maxValueColumn = matrix[f1.getCode().length()][j];
				maxIndexColumn = j;
			}
		}
	}

	/**
	 * Compute the index and the value of the highest value of the last column
	 */
	private void computePrefixLength() {
		maxValueRow = Integer.MIN_VALUE;
		maxIndexRow = -1;

		//Regarde dans la derni�re colonne la ligne avec la + grande valeur

		for(int i = 0; i < f1.getCode().length()+1; i++)
		{
			if(matrix[i][f2.getCode().length()] > maxValueRow)
			{
				maxValueRow = matrix[i][f2.getCode().length()];
				maxIndexRow = i;
			}
		}
	}
	
	/**
	 * Compute the maximum value of the last row and column
	 */
	private void computePrefixAndSuffixLength()
	{
		//CALCUL DE LA VALEUR MAXIMALE
		
		computePrefixLength();
		
		computeSuffixLength();

	}

	/**
	 * Compute the maximum value of the last row and column and construction of the alignment
	 * @return
	 */
	public ArrayList<Alignment> computeAlignmentMax()
	{
		alignmentList = new ArrayList<Alignment>();
		
		matrix = new short[f1.getCode().length()+1][f2.getCode().length()+1];
		execute();

		computePrefixAndSuffixLength();
		
		buildAlignment();

		return alignmentList;
	}
	

	/**
	 * Built the alignment
	 */
	private void buildAlignment()
	{ 
		int startRow = 0, startColumn = 0;
		
		//f1->f2
		startRow = f1.getCode().length();
		startColumn = maxIndexColumn;

		alignmentConstructor(startRow, startColumn, true);
		
		//f2->f1
		startRow = maxIndexRow;
		startColumn = f2.getCode().length();
		
		alignmentConstructor(startRow, startColumn, false);
	}

	/**
	 * Construction of the alignment
	 * @param startRow : The index where start the alignment on the row
	 * @param startColumn : The index where start the alignment on the column
	 * @param isSuffixe : Boolean to knwo if we are treating a suffix or not.  
	 */
	private void alignmentConstructor(int startRow, int startColumn, boolean isSuffixe)
	{

		StringBuilder alignmentF1 = new StringBuilder();
        StringBuilder alignmentF2 = new StringBuilder();
        int row = startRow, column = startColumn;

		if(startColumn < f2.getCode().length())
		{
			for(int i = 1; i <= f2.getCode().length()-startColumn; i++)
			{
				alignmentF1.append(Alignment.BORDER);
				alignmentF2.append(f2.getCode().charAt(f2.getCode().length()-i));
			}
		}
		else
		{
			for(int i = 1; i <= f1.getCode().length()-startRow; i++)
			{
				alignmentF1.append(f1.getCode().charAt(f1.getCode().length()-i));
				alignmentF2.append(Alignment.BORDER);
			}
		}
				
		while(row != 0 && column != 0)
		{
			int p = score(f1.getCode().charAt(row-1),f2.getCode().charAt(column-1));
					

			if(matrix[row-1][column-1]+p == matrix[row][column])
			{
				//On vient de la diagonal
				row--;
				column--;
				alignmentF1.append(f1.getCode().charAt(row));
				alignmentF2.append(f2.getCode().charAt(column));	
			}
			else
			{
				if(matrix[row][column-1]+G == matrix[row][column])
				{
					//On vient de la gauche
					column--;
					alignmentF2.append(f2.getCode().charAt(column));
					alignmentF1.append(Alignment.GAP);
							
				}
				else
				{	//On vient d'en haut
					//matrix[row-1][column]+G == matrix[row][column]
					row--;
					alignmentF1.append(f1.getCode().charAt(row));
					alignmentF2.append(Alignment.GAP);
				}
						
			}
		}
		if(row != 0)
		{
			for(int i  = row-1;i >=0;i--)
			{
				alignmentF1.append(f1.getCode().charAt(i));
		        alignmentF2.append(Alignment.BORDER);
		    }
		}
		else if(column != 0)
		{
			for(int i  = column-1;i >=0;i--)
		    {
				alignmentF1.append(Alignment.BORDER);
				alignmentF2.append(f2.getCode().charAt(i));
			}
		}
		alignmentF1.reverse();
		alignmentF2.reverse();
		
		//on traite f1->f2 et f2->f1 avec la même matrice.
		//Quand isSuffixe est  à true, on traite f1->f2 donc on a : alignement f1 --------
		//														   alignement f2      --------
		//Quand isSuffixe est  à false, on traite f2->f1 mais on obtient : alignement f1         ---------
		//																  alignement f2     --------
		//On inverse donc pour avoir alignement f2 -------------
		//							 alignement f1        -------------
		if(isSuffixe == false)
		{
			
			StringBuilder temp = alignmentF1;
			alignmentF1= alignmentF2;
			alignmentF2 = temp;
		}
		
		Alignment alignment = new Alignment();
        alignment.setCost(matrix[startRow][startColumn]);
        
        int startIndex = 0;
        
        while(alignmentF2.charAt(startIndex) == Alignment.BORDER){
            startIndex++;
        }
        alignment.setStartIndex(startIndex);

        //Regarde le type d'alignement
        if(alignmentF1.charAt(0) == Alignment.BORDER && alignmentF1.charAt(alignmentF1.length()-1) == Alignment.BORDER)
            alignment.setType(AlignmentType.F1INCLUDEDTOF2);
        else if(alignmentF1.charAt(0)== Alignment.BORDER && alignmentF1.charAt(alignmentF1.length()-1) != Alignment.BORDER)
            alignment.setType(AlignmentType.PREFIX);//PREFIXE
        else if(alignmentF1.charAt(0)!= Alignment.BORDER && alignmentF1.charAt(alignmentF1.length()-1) == Alignment.BORDER)
            alignment.setType(AlignmentType.OTHER);//SUFFIXE
        else if(alignmentF2.charAt(0) == Alignment.BORDER || alignmentF2.charAt(alignmentF2.length() - 1) == Alignment.BORDER)
            alignment.setType(AlignmentType.F2INCLUDEDTOF1);
        else
            alignment.setType(AlignmentType.OTHER);//EGAL

       
        if(insertAlignment == true)
        {
	        alignment.setCode1(alignmentF1.toString());
	        alignment.setCode2(alignmentF2.toString());
        }

        alignmentList.add(alignment);
	}

}
