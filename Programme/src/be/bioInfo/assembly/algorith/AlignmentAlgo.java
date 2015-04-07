package be.bioInfo.assembly.algorith;

import be.bioInfo.assembly.model.Fragment;

public class AlignmentAlgo
{
	private static final int G = -2;
	private static final int P = 1;
	private int maxValueColumn = Integer.MIN_VALUE, maxIndexColumn = -1, maxValueRow = Integer.MIN_VALUE,  maxIndexRow = -1, maxValue;
	
	/**
	 * Exécution de l'algorithme semi-global
	 * @param f1 Un fragment
	 * @param f2 Un second fragment
	 * @param matrix La matrice de l'alignement semi-global
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
		for(int i = 0; i < f1.getCode().length()+1; i++)
		{
			for(int j = 0; j < f2.getCode().length()+1; j++)
			{
				System.out.print(matrix[i][j]+" ");
			}
			System.out.print("\n");
		}
	}

	/**
	 * Calcul de P
	 * @param c1 le caractère de f1 à la i-1ème place
	 * @param c2 le caractère de f2 à la j-1ème place
	 * @return la valeur de P
	 */
	private int score(char c1, char c2)
	{
		if(c1 == c2)
			return P;
		else
			return -P;
	}

	/**
	 * Calcule le maximum entre le voisin du dessus+G , le voisin en diagonal+P et le voisin de gauche+G
	 * @param a la valeur du voisin du dessus+G
	 * @param b la valeur du voisin en diagonal+P
	 * @param c la valeur du voisin de gauche+G
	 * @return le maximum
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
	 * Calcul de l'indice et de la valeur de la plus grande valeur de la dernière ligne de la matrice
	 * @param f1 un fragment
	 * @param f2 un second fragment
	 * @param matrix la matrice de l'alignement semi-global
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
	 * Calcul de l'indice et de la valeur de la plus grande valeur de la dernière colonne de la matrice
	 * @param f1 un fragment
	 * @param f2 un second fragment
	 * @param matrix la matrice de l'alignement semi-global
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
	 * Calcul de la valeur maximale de la dernière ligne et colonne
	 * @param f1 un fragment
	 * @param f2 un second fragment
	 * @param matrix la matrice de l'alignement semi-global
	 */
	private void computeMaxValue(Fragment f1, Fragment f2, int[][] matrix)
	{
		//CALCUL DE LA VALEUR MAXIMALE
		
		maxValueColumn(f1, f2, matrix);
		
		maxValueRow(f1, f2, matrix);

	}

	/**
	 * Calcul de la valeur maximale de la dernière ligne et colonne et reconstruction de l'alignement
	 * @param f1 un fragment
	 * @param f2 un second fragment
	 */
	public void computeAlignmentMax(Fragment f1, Fragment f2)
	{
		int matrix [][] = new int[f1.getCode().length()+1][f2.getCode().length()+1];
		
		execute(f1,f2, matrix);
		
		computeMaxValue(f1, f2, matrix);
		
		alignment(f1, f2, matrix);

	}
	
	/**
	 * Calcul l'alignement des fragments
	 * @param f1 un fragment
	 * @param f2 un second fragment
	 * @param matrix la matrice de l'alignement semi-global
	 */
	private void alignment(Fragment f1, Fragment f2, int matrix[][])
	{
		
		int i = 0;
		int j = 0;
		
		//Calcul de l'alignement de f1->f2
		i = f1.getCode().length();
		j = maxIndexRow;
		
		alignmentConstructor(i, j, f1, f2, matrix);
	
		//Calcul de l'alignement de f2->f1
		j = f2.getCode().length();
		i = maxIndexColumn;
		
		alignmentConstructor(i, j, f1, f2, matrix);

	}
	
	/**
	 * Construction de l'alignement
	 * @param i l'indice de la ligne de la matrice
	 * @param j l'indice de la colonne de la matrice
	 * @param f1 un fragment
	 * @param f2 un second fragment
	 * @param matrix la matrice de l'alignement semi-global
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
	 * Construit correctement l'alignement
	 * @param i l'indice de la ligne de la matrice
	 * @param j l'indice de la colonne de la matrice
	 * @param savei l'indice de la ligne de la matrice sauvegardée
	 * @param savej l'indice de la colonne de la matrice sauvegardée
	 * @param f1 un fragment
	 * @param f2 un second fragment
	 * @param alignmentF1 alignement de F1
	 * @param alignmentF2 alignement de F2
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
		
		/*System.out.println(reconstructorF1);
		System.out.println(reconstructorF2);*/
	}

	public int getMaxValueColumn() {
		return maxValueColumn;
	}

	public int getMaxValueRow() {
		return maxValueRow;
	}
	
	
	
}
