package be.bioInfo.assembly.algorithm;

import java.util.ArrayList;

import be.bioInfo.assembly.model.Fragment;


/**
* @author Watillon Thibaut & Opsommer Sophie, 2015
*
*/
public class AlignmentAlgo
{
	private static final int G = -2;
	private static final int P = 1;
	private int maxValueColumn = Integer.MIN_VALUE, maxIndexColumn = -1, maxValueRow = Integer.MIN_VALUE,  maxIndexRow = -1;
	private ArrayList<String> alignmentList;
	private int inclusion;
	
	/**
	 * Execute the semi-global alignment algorithm.
	 * Calculate metrics of the matrice "matrix" while compare char by char the similarity
	 * @param f1 The first fragment
	 * @param f2 The second fragment
	 * @param matrix The matrix of the semi-global alignment
	 */
	private void execute(Fragment f1, Fragment f2, int matrix[][])
	{
		//System.out.println(f1.getCode()+" "+f2.getCode());
		//ALGO
		for(int i = 1; i < f1.getCode().length()+1; i++)
		{
			for(int j = 1; j < f2.getCode().length()+1; j++)
			{
				int p = score(f1.getCode().charAt(i-1),f2.getCode().charAt(j-1));
				matrix[i][j] = max(matrix[i-1][j]+G, matrix[i-1][j-1]+p,matrix[i][j-1]+G);
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
	 * @param c2 the charactere of f2 at j-1ème
	 * @return The similarity value
	 */
	private int score(char c1, char c2)
	{
		if(c1 == c2)
			return P;
		else
			return -P;
	}

	/**
	 * Compute the maximum between the above neighbor, the diagonal neighbor and the left neighbor
	 * @param a the value of the above neighbor+G
	 * @param b the value of the diagonal neighbor+P
	 * @param c the value of the left neighbor+G
	 * @return the maximum
	 */
	private int max(int a, int b, int c)
	{
		int maximum = Integer.MIN_VALUE;
		
		if(a > maximum)
			maximum = a;
		if(b > maximum)
			maximum = b;
		if(c > maximum)
			maximum = c;
		return maximum;	
	}
	
	/**
	 * Compute the index and the value of the highest value of the last row
	 * @param f1 a fragment
	 * @param f2 a second fragment
	 * @param matrix the matrice
	 */
	private void maxValueRow(Fragment f1, Fragment f2, int[][] matrix) {
		maxValueRow = Integer.MIN_VALUE;
		maxIndexRow = -1;
		
		//Regarde dans la dernière ligne
		for(int j = 0; j < f2.getCode().length()+1; j++)
		{
			if(matrix[f1.getCode().length()][j] >= maxValueRow)
			{
				maxValueRow = matrix[f1.getCode().length()][j];
				maxIndexRow = j;
			}
		}
	}

	/**
	 * Compute the index and the value of the highest value of the last column
	 * @param f1 un fragment
	 * @param f2 un second fragment
	 * @param matrix the semi-global alignment matrice
	 */
	private void maxValueColumn(Fragment f1, Fragment f2, int[][] matrix) {
		maxValueColumn = Integer.MIN_VALUE;
		maxIndexColumn = -1;
		
		//Regarde dans la dernière colonne
		for(int i = 0; i < f1.getCode().length()+1; i++)
		{
			if(matrix[i][f2.getCode().length()] >= maxValueColumn)
			{
				maxValueColumn = matrix[i][f2.getCode().length()];
				maxIndexColumn = i;
			}
		}
	}
	
	/**
	 * Compute the maximum value of the last row and column
	 * @param f1 a fragment
	 * @param f2 a second fragment
	 * @param matrix the semi-global alignment matrix
	 */
	private void computeMaxValue(Fragment f1, Fragment f2, int[][] matrix)
	{
		//CALCUL DE LA VALEUR MAXIMALE
		
		maxValueColumn(f1, f2, matrix);
		
		maxValueRow(f1, f2, matrix);

	}

	/**
	 * Compute the maximum value of the last row and column and reconstruction of the alignment
	 * @param f1 a fragment
	 * @param f2 a second fragment
	 */
	public void computeAlignmentMax(Fragment f1, Fragment f2)
	{
		inclusion = 0;
		
		int matrix [][] = new int[f1.getCode().length()+1][f2.getCode().length()+1];
		
		execute(f1,f2, matrix);
		
		computeMaxValue(f1, f2, matrix);
		
		//buildAlignment(f1, f2);

		manageInclusion(f1, f2);
		
	}

	/**
	 * Look if the fragments are included to each other
	 * @param f1 a fragment
	 * @param f2 a secon  fragment
	 */
	private void manageInclusion(Fragment f1, Fragment f2) {
		
		
		if(f1.getCode().length() <= f2.getCode().length())//f1 peut être inclu à f2
		{
			if(maxValueRow >= maxValueColumn) 
			{
				if(maxIndexRow >= f1.getCode().length())
				{
					inclusion = 1;
					/*System.out.println("f1 inclus à f2");
					System.out.println(alignmentList.get(0));
					System.out.println(alignmentList.get(1));*/
				}
			}
		}
		else //f2 peut être inclu à f1
		{
			if(maxValueColumn >= maxValueRow)
			{
				if(maxIndexColumn>=f2.getCode().length())
				{
					inclusion = -1;
					/*System.out.println("f2 inclus à f1");
					System.out.println(alignmentList.get(0));
					System.out.println(alignmentList.get(1));*/
				}
			}
		}
	}
	
	public void buildAlignment(Fragment f1, Fragment f2)
	{
		alignmentList = new ArrayList<String>();
		
		int matrix [][] = new int[f1.getCode().length()+1][f2.getCode().length()+1];
		
		execute(f1,f2, matrix);
		
		computeMaxValue(f1, f2, matrix);
		
		alignment(f1, f2, matrix);
	}
	
	/**
	 * Compute the fragments alignment
	 * @param f1 a fragment
	 * @param f2 a second fragment
	 * @param matrix the semi-global alignment matrix
	 */
	private void alignment(Fragment f1, Fragment f2, int matrix[][])
	{
		
		int i = 0;
		int j = 0;
		
		//Calcul de l'alignement de f1->f2
		if(maxValueRow>=maxValueColumn)
		{
			i = f1.getCode().length();
			j = maxIndexRow;
		}
		else
		{
			j = f2.getCode().length();
			i = maxIndexColumn;
		}
		alignmentConstructor(i, j, f1, f2, matrix);
	}
	
	/**
	 * Construction of the alignment
	 * @param i index of the row where the alignment start or index of the last row
	 * @param j index of the column where the alignment start or index of the last column
	 * @param f1 a fragment
	 * @param f2 a second fragment
	 * @param matrix the semi-global alignment matrix
	 */
	private void alignmentConstructor(int i, int j, Fragment f1, Fragment f2, int matrix[][])
	{
		String alignmentF1 = "", alignmentF2 = "";
		int savei = i, savej = j;
		
		while(i != 0 && j != 0)
		{
			int p = score(f1.getCode().charAt(i-1),f2.getCode().charAt(j-1));
			
			//System.out.println(f1.getCode().charAt(i-1)+" "+f2.getCode().charAt(j-1));
			
			if(matrix[i-1][j-1]+p == matrix[i][j])
			{
				//On vient de la diagonal
				i = i-1;
				j = j-1;
				alignmentF1+=f1.getCode().charAt(i);
				alignmentF2+=f2.getCode().charAt(j);	
			}
			else
			{
				if(matrix[i][j-1]+G == matrix[i][j])
				{
					//On vient de la gauche
					j = j-1;
					alignmentF2+=f2.getCode().charAt(j);
					alignmentF1+="-";
					
				}
				else
				{	//On vient d'en haut
					//matrix[i-1][j]+G == matrix[i][j]
					i = i-1;
					alignmentF1+=f1.getCode().charAt(i);
					alignmentF2+="-";
				}
				
			}
		}
		
		alignmentReconstructor(i, j, savei, savej, f1, f2, alignmentF1, alignmentF2);
	}

	/**
	 * Well-done construction of the alignment
	 * @param i index of the row where the alignment start or index of the last row
	 * @param j index of the column where the alignment start or index of the last column
	 * @param savei the index of the row saved
	 * @param savej the index of the column saved
	 * @param f1 a fragment
	 * @param f2 a second fragment
	 * @param alignmentF1 F1 alignment
	 * @param alignmentF2 F2 alignment
	 */
	private void alignmentReconstructor(int i, int j, int savei, int savej, Fragment f1, Fragment f2,
			String alignmentF1, String alignmentF2) {
		
		String reconstructorF1="", reconstructorF2="";
		
		//System.out.println("fragment : "+f1.getCode()+" "+f2.getCode());
		
		//On recopie tout les caractères avant i de f1 et on mets des " " pour f2
		for(int a = 0; a < i; a++)
		{
			reconstructorF1+=String.valueOf(f1.getCode().charAt(a));
			reconstructorF2+=" ";
		}

		//On recopie tout les caractères avant j de f2 et on mets des " " pour f1
		for(int b = 0; b < j; b++)
		{
			reconstructorF2+=String.valueOf(f2.getCode().charAt(b));
			reconstructorF1+=" ";
		}
	
		int alignmentLength = alignmentF1.length();
		
		//On ajoute l'alignement calculé précédemment
		for(int c = alignmentLength; c > 0; c--)
		{
			reconstructorF1+=alignmentF1.charAt(c-1);
			reconstructorF2+=alignmentF2.charAt(c-1);
		}

		//On ajoute ce qui se trouve après l'alignement pour f1
		for(int d = savei; d<f1.getCode().length(); d++)
		{
			reconstructorF1+=f1.getCode().charAt(d);
		}
		
		//On ajoute ce qui se trouve après l'alignement pour f2
		for(int d = savej; d<f2.getCode().length(); d++)
		{
			reconstructorF2+=f2.getCode().charAt(d);
		}
		
		
		alignmentList.add(reconstructorF1);
		alignmentList.add(reconstructorF2);
		/*System.out.println(reconstructorF1);
		System.out.println(reconstructorF2);*/
	}

	public int getMaxValueColumn() {
		return maxValueColumn;
	}

	public int getMaxValueRow() {
		return maxValueRow;
	}

	//le premier élément de la liste contient l'alignement de f1 et le deuxième élément l'alignement de f2
	public ArrayList<String> getAlignmentList() {
		return alignmentList;
	}

	public int getInclusion() {
		return inclusion;
	}

	
	
}
