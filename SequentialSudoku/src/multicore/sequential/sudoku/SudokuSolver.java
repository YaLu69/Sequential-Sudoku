package multicore.sequential.sudoku;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class SudokuSolver
{
    private static long solutionCounter = 0;
    
    private static BigInteger solutionSpace = BigInteger.ONE;
    
    /**
     * Ritorna il numero delle soluzioni valide del Sudoku
     * @return solutionCounter
     */
    public long getSolutionCounter() { return solutionCounter; }
    
    /**
     * Ritorna lo spazio delle soluzioni del Sudoku
     * @return solutionSpace
     */
    public BigInteger getSolutionSpace() { return solutionSpace; }
	
    /**
     * Calcola i candidati possibili per una specifica cella del Sudoku
     * @param sudoku
     * @param row
     * @param col
     * @return set di Integer che rappresentano i possibili candidati
     */
    public static Set<Integer> getCandidates(int[][] sudoku, int row, int col)
    {
    	Set<Integer> candidates = new HashSet<>();
    	for(int i = 1; i <= 9; i++)
    		if (isValid(sudoku, row, col, i)) candidates.add(i);
		return candidates;
    }
    
    /**
     * Trova lo spazio delle soluzioni di un Sudoku
     * @param sudoku
     */
    private static void findSpaceSolution(int[][] sudoku)
    {
    	for (int i = 0; i < 9; i++)
    		for (int j = 0; j < 9; j++)
    			if(sudoku[i][j] == 0)
    				solutionSpace = solutionSpace.multiply(BigInteger.valueOf(getCandidates(sudoku, i, j).size()));
    }
    
    /**
     * Crea un Sudoku formato matrice di interi 9x9 a partire dalla lettura di un file
     * @param filePath stringa rappresentante il percorso del file
     * @return il Sudoku associato al file dato in input
     */
	public static int[][] sudokuReader(String filePath)
	{
		int[][] sudoku = new int[9][9];
		try (InputStream in = Files.newInputStream(Paths.get(new File(filePath).toURI()));
				BufferedReader reader =
						new BufferedReader(new InputStreamReader(in))) {
			String line = null;
			int row = 0;
			while ((line = reader.readLine()) != null)
			{
				for (int col = 0; col < line.length(); col++)
					sudoku[row][col] = line.charAt(col) != '.' ?
							Integer.parseInt(""+line.charAt(col)) : 0;
				row++;
			}
		} catch (IOException e) {
			System.err.println(e);
		}
		return sudoku;
	}
      
    /**
     * Verifico la validità del candidato v in una specifica cella della Sudoku
     * @param sudoku
     * @param row
     * @param col 
     * @param v valore del candidato in esame
     * @return true se il candidato è valido, false altrimenti
     */
    private static boolean isValid(int[][] sudoku, int row, int col, int v)
    {        
        // Verifico che il candidato v sia valido lungo la riga e la colonna
        for(int i = 0; i < 9; i++)
        {
            if(sudoku[row][i] == v) return false;
            if(sudoku[i][col] == v) return false;
        }
        // Verifico che il candidato v sia valido all'interno del proprio quadrante
        int rowSubregion = (row / 3)*3;
        int colSubregion = (col / 3)*3;
        //int rowSubregion = row - row % 3;
        //int colSubregion = col - col % 3;
        for(int w = 0; w < 3; w++)
            for(int k = 0; k < 3; k++)
            	if(sudoku[rowSubregion + k][colSubregion + w] == v) return false;
        return true;
    }
    
    /**
	 * Risolvere ricorsivamente un Sudoku
	 * @param sudoku 
	 * @param index
	 */
    private static void findSolution(int[][] sudoku, int index)
    {
        if(index == 81) solutionCounter++;
        else
        {
            int row = index / 9;
            int col = index % 9;
            // Salto le celle già riempite e vado avanti
            if(sudoku[row][col] != 0) findSolution(sudoku, index+1);
            else
            {
                // Provo tutti i possibili valori con cui riempire quella cella
                for(int i = 1; i <= 9; i++)
                {
                    if(isValid(sudoku, row, col, i))
                    {
                    	sudoku[row][col] = i;
                        findSolution(sudoku,index+1);
                        sudoku[row][col] = 0;
                    }   
                }
            }
        }
    }
    
    /**
     * Trova lo spazio di soluzioni e il numero di soluzioni del Sudoku in input
     * @param sudoku
     */
    public void solve(int[][] sudoku)
    {
    	findSpaceSolution(sudoku);
    	long startTime = System.currentTimeMillis();
    	findSolution(sudoku,0);
		System.out.println(System.currentTimeMillis() - startTime + " ms - solve() execution time");
    }

}