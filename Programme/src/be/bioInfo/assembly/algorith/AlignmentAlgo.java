package be.bioInfo.assembly.algorith;

import be.bioInfo.assembly.model.Fragment;

public class AlignmentAlgo
{
	private static final int G = -2;
	private static final int P = 1;
	private int maxValueColumn = Integer.MIN_VALUE, maxIndexColumn = -1, maxValueRow = Integer.MIN_VALUE,  maxIndexRow = -1, maxValue, maxIndex;
	private boolean maxValueInRow = false;
	
	private void execute(Fragment f1, Fragment f2, int matrix[][])
	{
		//System.out.println(f1.getCode()+" "+f2.getCode());
		//ALGO
		for(int i = 0; i < f1.getCode().length()+1; i++)
		{
			matrix[i][0] = 0;
		}
		
		for(int j = 0; j < f2.getCode().length()+1; j++)
		{
			matrix[0][j] = 0;
		}
		
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

	private void computeMaxValue(Fragment f1, Fragment f2, int[][] matrix)
	{
		//CALCUL DE LA VALEUR MAXIMALE
		
		maxValueColumn(f1, f2, matrix);
		
		maxValueRow(f1, f2, matrix);
		
		maxValue();
		
		//AFFICHAGE VALEUR MAXIMALE
		//System.out.println(maxValue+" "+maxIndex);
	}

	private void maxValue() {
		maxValueInRow = false;
	
		if(maxValueRow > maxValueColumn)
		{
			//Valeur max dans la dernière ligne
			maxValue = maxValueRow;
			maxIndex = maxIndexRow;
			maxValueInRow = true;
		}
		else
		{
			//Valeur max dans la dernière colonne
			maxValue = maxValueColumn;
			maxIndex = maxIndexColumn;
		}
	}

	private void maxValueRow(Fragment f1, Fragment f2, int[][] matrix) {
		maxValueRow = Integer.MIN_VALUE;
		maxIndexRow = -1;
		
		//Regarde dans la dernière ligne
		for(int j = 0; j < f2.getCode().length()+1; j++)
		{
			if(matrix[f1.getCode().length()][j] > maxValueRow)
			{
				maxValueRow = matrix[f1.getCode().length()][j];
				maxIndexRow = j;
			}
		}
	}

	private void maxValueColumn(Fragment f1, Fragment f2, int[][] matrix) {
		maxValueColumn = Integer.MIN_VALUE;
		maxIndexColumn = -1;
		
		//Regarde dans la dernière colonne
		for(int i = 0; i < f1.getCode().length()+1; i++)
		{
			if(matrix[i][f2.getCode().length()] > maxValueColumn)
			{
				maxValueColumn = matrix[i][f2.getCode().length()];
				maxIndexColumn = i;
			}
		}
	}
	
	private int score(char c1, char c2)
	{
		if(c1 == c2)
			return P;
		else
			return -P;
	}

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
	
	public int computeAlignmentMax(Fragment f1, Fragment f2)
	{
		int matrix [][] = new int[f1.getCode().length()+1][f2.getCode().length()+1];
		
		execute(f1,f2, matrix);
		
		computeMaxValue(f1, f2, matrix);
		
		return computeAlignment(f1, f2, matrix);
	}
	
	private int computeAlignment(Fragment f1, Fragment f2, int matrix[][])
	{
		
		int i = 0;
		int j = 0;
		
		if(maxValueInRow == false)
		{
			j = f2.getCode().length();
			i = maxIndex;
		}
		else
		{
			i = f1.getCode().length();
			j = maxIndex;

		}

		return alignmentMax(i, j, f1, f2, matrix);
		
	}
	
	private int alignmentMax(int i, int j, Fragment f1, Fragment f2, int matrix[][])
	{
		int cptAlignment = 0;
		
		//System.out.println(f1.getCode()+" "+f2.getCode());
		
		while(i != 0 && j != 0)
		{
			int p = score(f1.getCode().charAt(i-1),f2.getCode().charAt(j-1));
			
			//System.out.println(f1.getCode().charAt(i-1)+" "+f2.getCode().charAt(j-1));
			
			if(matrix[i-1][j-1]+p == matrix[i][j])
			{
				i = i-1;
				j = j-1;
			}
			else
			{
				if(matrix[i][j-1]+G == matrix[i][j])
				{
					i = i;
					j = j-1;
				}
				else
				{
					//matrix[i-1][j]+G == matrix[i][j]
					i = i-1;
					j = j;
				}
			}
			
			
			cptAlignment++;
		}

		//System.out.println(cptAlignment);
		return cptAlignment;
		
	}
	
	
	
}
