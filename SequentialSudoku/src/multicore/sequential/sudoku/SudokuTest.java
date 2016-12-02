package multicore.sequential.sudoku;

public class SudokuTest
{
	public static void main(String[] args)
	{
		String filePath = "test2_a.txt";
		SudokuSolver solver = new SudokuSolver();
		solver.solve(SudokuSolver.sudokuReader(filePath));
		System.out.println("Sudoku file name: "+filePath);
		System.out.println("Solution counter: " + solver.getSolutionCounter());
		System.out.println("Space Solution: " + solver.getSolutionSpace());
	}
}
