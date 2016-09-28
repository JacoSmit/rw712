import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	private static int K;
		
	public static void processCase(BufferedReader in, PrintStream out) throws NumberFormatException, IOException {
		
		K = Integer.parseInt(in.readLine());
		
		Cell [][] grid = new Cell[K][K];
		ArrayList<HashMap<Integer, Integer>> rows = new ArrayList<HashMap<Integer, Integer>>();
		ArrayList<HashMap<Integer, Integer>> columns = new ArrayList<HashMap<Integer, Integer>>();
		
		Graph g = new Graph(K*K);
		
		read(in, grid, rows, columns, g);
		
		if (K > 1) {
			startingTechniques(grid, g, rows, columns);
			cornerTechniques(grid, g, rows, columns);
			
			boolean changed = false;
			do {
				changed = false;
				changed = changed | startingTechniques(grid, g, rows, columns);
				changed = changed | basicTechniques(grid, g, rows, columns);
				changed = changed | cornerTechniques(grid, g, rows, columns);				
			} while (changed);
						
			if (countUnknowns(grid) > 0) {
				bruteForce(grid, g, rows, columns, 0);
			}
		}
		
		print(grid);
		
	}
	
	private static boolean bruteForce(Cell [][] grid, Graph g, ArrayList<HashMap<Integer, Integer>> rows, ArrayList<HashMap<Integer, Integer>> columns, int position) {
		
		int row = position / K;
		int col = position % K;
		
		if (row == K || col == K) {
			return true;
		}
		
		if (grid[row][col].isCrossed() || !grid[row][col].canBeCrossed()) {
			return bruteForce(grid, g, rows, columns, position+1);
		} else {
			/*
			 * Try painting white.
			 */
			grid[row][col].setUnCrossable();
			
			if (checkCondidtions(grid, rows, columns, row, col) && bruteForce(grid, g, rows, columns, position+1)) {
				return true;
			} else {
				grid[row][col].setCrossable();
				
				/*
				 * Try painting black
				 */
				if (!formsPartitionIfCrossed(grid, g, row, col)) {				
					
					grid[row][col].crossOut();
															
					if (row+1 < K) {
						g.removeEdge(row*K+col, (row+1)*K+col);
					}
					
					if (row-1 >= 0) {
						
						g.removeEdge(row*K+col, (row-1)*K+col);
					}
					
					if (col+1 < K) {
						
						g.removeEdge(row*K+col, row*K+col+1);
					}
					
					if (col-1 >= 0) {
						g.removeEdge(row*K+col, row*K+col-1);
					}
					
					
					if (checkCondidtions(grid, rows, columns, row, col) && bruteForce(grid, g, rows, columns, position+1)) {
						return true;
					} else {
						
						grid[row][col].unCrossOut(true);
						if (row+1 < K) {
							g.addEdge(row*K+col, (row+1)*K+col);
						}
						
						if (row-1 >= 0) {
							
							g.addEdge(row*K+col, (row-1)*K+col);
						}
						
						if (col+1 < K) {
							
							g.addEdge(row*K+col, row*K+col+1);
						}
						
						if (col-1 >= 0) {
							g.addEdge(row*K+col, row*K+col-1);
						}
						
						return false;
					}
				} else {
					return false;
				}			
			}
		}		
	}
	
	private static boolean checkCondidtions(Cell [][] grid, ArrayList<HashMap<Integer, Integer>> rows, ArrayList<HashMap<Integer, Integer>> columns, int row, int col) {
		/*
		 * Check if path exists can bes called manually before setting square to black
		 */
		
		return checkCrossedConditions(grid, row, col) && checkDuplicates(grid, rows, columns, row, col);
	}
	
	private static boolean checkDuplicates(Cell [][] grid, ArrayList<HashMap<Integer, Integer>> rows, ArrayList<HashMap<Integer, Integer>> columns, int row, int col) {
		
		if (grid[row][col].isCrossed()) {
			return true;
		}
		
		if (!grid[row][col].canBeCrossed() && rows.get(row).get(grid[row][col].value) > 1) {
			for (int k = 0; k < K; k++) {
				if (k == col) {
					continue;
				}
				
				if (grid[row][k].value == grid[row][col].value && !grid[row][k].isCrossed() && !grid[row][k].canBeCrossed()) {
					return false;
				}
			}
		}
		
		if (!grid[row][col].canBeCrossed() && columns.get(col).get(grid[row][col].value) > 1) {
			for (int k = 0; k < K; k++) {
				if (k == row) {
					continue;
				}
				
				if (grid[k][col].value == grid[row][col].value && !grid[k][col].isCrossed() && !grid[k][col].canBeCrossed()) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private static boolean checkCrossedConditions(Cell [][] grid, int i, int j) {
					
		if (grid[i][j].isCrossed()) {
			if (i+1 < K) {
				if (grid[i+1][j].isCrossed()) {
					return false;
				}
			}
			
			if (i-1 >= 0) {
				if (grid[i-1][j].isCrossed()) {
					return false;
				}
			}

			if (j+1 < K) {
				if (grid[i][j+1].isCrossed()) {
					return false;
				}
			}

			if (j-1 >= 0) {
				if (grid[i][j-1].isCrossed()) {
					return false;
				}
			}			
		}
		
		return true;		
	}
	
	private static int countUnknowns(Cell[][] grid) {
		int count = 0;
		
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < K; j++) {
				if (!grid[i][j].isCrossed() && grid[i][j].canBeCrossed()) {
					return 1;
				}
			}
		}
		return count;
	}	
	
	private static boolean cornerTechniques(Cell [][] grid, Graph g, ArrayList<HashMap<Integer, Integer>> rows, ArrayList<HashMap<Integer, Integer>> columns) {
		boolean changed = false;
		
		changed = changed | cornerTechnique3(grid, g);
		changed = changed | cornerTechnique1(grid, g);
		changed = changed | cornerTechnique2(grid, g);
		changed = changed | cornerTechnique4(grid, g);
		changed = changed | cornerTechnique5(grid);
		
		return changed;
	}
	
	private static boolean cornerTechnique5(Cell [][] grid) {
		boolean changed = false;
		
		/*
		 * --------------------------------------
		 * Top Row
		 * --------------------------------------
		 */
		/*
		 * Corner 1 (Top left)
		 */
		if (grid[1][0].canBeCrossed() && grid[0][0].value == grid[0][1].value) {
			grid[1][0].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 2 (Top right)
		 */
		if (grid[1][K-1].canBeCrossed() && grid[0][K-1].value == grid[0][K-2].value) {
			grid[1][K-1].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 3 (Bottom left)
		 */
		if (grid[K-2][0].canBeCrossed() && grid[K-1][0].value == grid[K-1][1].value) {
			grid[K-2][0].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 4 (Bottom right)
		 */
		if (grid[K-2][K-1].canBeCrossed() && grid[K-1][K-1].value == grid[K-1][K-2].value) {
			grid[K-2][K-1].setUnCrossable();
			changed = true;
		}
		
		/*
		 * --------------------------------------
		 * Bottom Row
		 * --------------------------------------
		 */
		/*
		 * Corner 1 (Top left)
		 */
		if (grid[0][1].canBeCrossed() && grid[1][0].value == grid[1][1].value) {
			grid[0][1].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 2 (Top right)
		 */
		if (grid[0][K-2].canBeCrossed() && grid[1][K-1].value == grid[1][K-2].value) {
			grid[0][K-2].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 3 (Bottom left)
		 */
		if (grid[K-1][1].canBeCrossed() && grid[K-2][0].value == grid[K-2][1].value) {
			grid[K-1][1].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 4 (Bottom right)
		 */
		if (grid[K-1][K-2].canBeCrossed() && grid[K-2][K-1].value == grid[K-2][K-2].value) {
			grid[K-1][K-2].setUnCrossable();
			changed = true;
		}
		
		/*
		 * --------------------------------------
		 * Left Column
		 * --------------------------------------
		 */
		/*
		 * Corner 1 (Top left)
		 */
		if (grid[0][1].canBeCrossed() && grid[0][0].value == grid[1][0].value) {
			grid[0][1].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 2 (Top right)
		 */
		if (grid[0][K-2].canBeCrossed() && grid[0][K-1].value == grid[1][K-1].value) {
			grid[0][K-2].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 3 (Bottom left)
		 */
		if (grid[K-1][1].canBeCrossed() && grid[K-1][0].value == grid[K-2][0].value) {
			grid[K-1][1].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 4 (Bottom right)
		 */
		if (grid[K-1][K-2].canBeCrossed() && grid[K-1][K-1].value == grid[K-2][K-1].value) {
			grid[K-1][K-2].setUnCrossable();
			changed = true;
		}
		
		/*
		 * --------------------------------------
		 * Right Column
		 * --------------------------------------
		 */
		/*
		 * Corner 1 (Top left)
		 */
		if (grid[1][0].canBeCrossed() && grid[0][1].value == grid[1][1].value) {
			grid[1][0].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 2 (Top right)
		 */
		if (grid[1][K-1].canBeCrossed() && grid[0][K-2].value == grid[1][K-2].value) {
			grid[1][K-1].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 3 (Bottom left)
		 */
		if (grid[K-2][0].canBeCrossed() && grid[K-1][1].value == grid[K-2][1].value) {
			grid[K-2][0].setUnCrossable();
			changed = true;
		}
		
		/*
		 * Corner 4 (Bottom right)
		 */
		if (grid[K-2][K-1].canBeCrossed() && grid[K-1][K-2].value == grid[K-2][K-2].value) {
			grid[K-2][K-1].setUnCrossable();
			changed = true;
		}
				
		return changed;					
	}
	
	private static boolean cornerTechnique4(Cell [][] grid, Graph g) {
		/*
		 * 1 2
		 * 2 2
		 * 
		 * =
		 * 
		 * 1 2*
		 * 2* (2) 0*
		 * 0   0*
		 */
		boolean changed = false;
		
		/*
		 * Corner 1 (Top left)
		 */
		if (!grid[1][1].isCrossed() && grid[0][1].value == grid[1][1].value && grid[1][1].value == grid[1][0].value) {
			crossOut(grid, g, 1, 1);
			changed = true;
		}
		
		/*
		 * Corner 2 (Top right)
		 */
		if (!grid[1][K-2].isCrossed() && grid[0][K-2].value == grid[1][K-2].value && grid[1][K-2].value == grid[1][K-1].value) {
			crossOut(grid, g, 1, K-2);
			changed = true;
		}
		
		/*
		 * Corner 3 (Bottom left)
		 */
		if (!grid[K-2][1].isCrossed() && grid[K-1][1].value == grid[K-2][1].value && grid[K-2][1].value == grid[K-2][0].value) {
			crossOut(grid, g, K-2, 1);	
			changed = true;
		}
		
		/*
		 * Corner 4 (Bottom right)
		 */
		if (!grid[K-2][K-2].isCrossed() && grid[K-1][K-2].value == grid[K-2][K-2].value && grid[K-2][K-2].value == grid[K-2][K-1].value) {
			crossOut(grid, g, K-2, K-2);
			changed = true;
		}
		return changed;
		
	}
	
	private static boolean cornerTechnique3(Cell [][] grid, Graph g) {
		boolean changed = false;
		/*
		 * Corner 1 (Top left)
		 */
		if (!grid[0][0].isCrossed() && !grid[1][1].isCrossed() && grid[0][0].value == grid[1][0].value && grid[0][1].value == grid[1][1].value && grid[0][0].value == grid[0][1].value) {
			crossOut(grid, g, 0, 0);
			crossOut(grid, g, 1, 1);
			if (K > 2) {
				grid[0][2].setUnCrossable();
			}
			changed = true;
		}
		
		/*
		 * Corner 2 (Top right)
		 */
		if (!grid[0][K-1].isCrossed() && !grid[1][K-2].isCrossed() && grid[0][K-1].value == grid[1][K-1].value && grid[0][K-2].value == grid[1][K-2].value && grid[0][K-1].value == grid[0][K-2].value) {
			crossOut(grid, g, 0, K-1);
			crossOut(grid, g, 1, K-2);
			if (K-3 > 0) {
				grid[0][K-3].setUnCrossable();
			}
			changed = true;
		}
		
		/*
		 * Corner 3 (Bottom left)
		 */
		if (!grid[K-1][0].isCrossed() && !grid[K-2][1].isCrossed() && grid[K-1][0].value == grid[K-2][0].value && grid[K-1][1].value == grid[K-2][1].value && grid[K-1][0].value == grid[K-1][1].value) {
			crossOut(grid, g, K-1, 0);
			crossOut(grid, g, K-2, 1);
			
			if (K > 2) {
				grid[K-1][2].setUnCrossable();
			}
			changed = true;
		}
		
		/*
		 * Corner 4 (Bottom right)
		 */
		if (!grid[K-1][K-1].isCrossed() && !grid[K-2][K-2].isCrossed() && grid[K-1][K-1].value == grid[K-2][K-1].value && grid[K-1][K-2].value == grid[K-2][K-2].value && grid[K-1][K-1].value == grid[K-1][K-2].value) {
			crossOut(grid, g, K-1, K-1);
			crossOut(grid, g, K-2, K-2);
			
			if (K-3 > 0) {
				grid[K-1][K-3].setUnCrossable();
			}
			changed = true;
		}
		return changed;
	}
	
	private static boolean cornerTechnique2(Cell [][] grid, Graph g) {
		boolean changed = false;
		/*
		 * --------------------------------------------------
		 * Vertical
		 * -------------------------------------------------
		 */
		
		/*
		 * Corner 1 (Top left)
		 */
		if (!grid[0][0].isCrossed() && !grid[1][1].isCrossed() && grid[0][0].value == grid[1][0].value && grid[0][1].value == grid[1][1].value) {
			crossOut(grid, g, 0, 0);
			crossOut(grid, g, 1, 1);
			changed = true;
		}
		
		/*
		 * Corner 2 (Top right)
		 */
		if (!grid[0][K-1].isCrossed() && !grid[1][K-2].isCrossed() && grid[0][K-1].value == grid[1][K-1].value && grid[0][K-2].value == grid[1][K-2].value) {
			crossOut(grid, g, 0, K-1);
			crossOut(grid, g, 1, K-2);
			changed = true;
		}
		
		/*
		 * Corner 3 (Bottom left)
		 */
		if (!grid[K-1][0].isCrossed() && !grid[K-2][1].isCrossed() && grid[K-1][0].value == grid[K-2][0].value && grid[K-1][1].value == grid[K-2][1].value) {
			crossOut(grid, g, K-1, 0);
			crossOut(grid, g, K-2, 1);
			changed = true;
		}
		
		/*
		 * Corner 4 (Bottom right)
		 */
		if (!grid[K-1][K-1].isCrossed() && !grid[K-2][K-2].isCrossed() && grid[K-1][K-1].value == grid[K-2][K-1].value && grid[K-1][K-2].value == grid[K-2][K-2].value) {
			crossOut(grid, g, K-1, K-1);
			crossOut(grid, g, K-2, K-2);
			changed = true;
		}
		
		/*
		 * --------------------------------------------------
		 * Horizontal
		 * -------------------------------------------------
		 */
		
		/*
		 * Corner 1 (Top left)
		 */
		if (!grid[0][0].isCrossed() && !grid[1][1].isCrossed() && grid[0][0].value == grid[0][1].value && grid[1][0].value == grid[1][1].value) {
			crossOut(grid, g, 0, 0);
			crossOut(grid, g, 1, 1);
			changed = true;
		}
		
		/*
		 * Corner 2 (Top right)
		 */
		if (!grid[0][K-1].isCrossed() && !grid[1][K-2].isCrossed() && grid[0][K-1].value == grid[0][K-2].value && grid[1][K-1].value == grid[1][K-2].value) {
			crossOut(grid, g, 0, K-1);
			crossOut(grid, g, 1, K-2);
			changed = true;
		}
		
		/*
		 * Corner 3 (Bottom left)
		 */
		if (!grid[K-1][0].isCrossed() && !grid[K-2][1].isCrossed() && grid[K-1][0].value == grid[K-1][1].value && grid[K-2][0].value == grid[K-2][1].value) {
			crossOut(grid, g, K-1, 0);
			crossOut(grid, g, K-2, 1);
			changed = true;
		}
		
		/*
		 * Corner 4 (Bottom right)
		 */
		if (!grid[K-1][K-1].isCrossed() && !grid[K-2][K-2].isCrossed() && grid[K-1][K-1].value == grid[K-1][K-2].value && grid[K-2][K-1].value == grid[K-2][K-2].value) {
			crossOut(grid, g, K-1, K-1);
			crossOut(grid, g, K-2, K-2);
			changed = true;
		}
		return changed;
	}
	
	private static boolean cornerTechnique1(Cell [][] grid, Graph g) {
		boolean changed = false;
		/*
		 * Corner 1 (Top left)
		 */
		if (!grid[0][0].isCrossed() && grid[0][0].value == grid[0][1].value && grid[0][0].value == grid[1][0].value) {
			crossOut(grid, g, 0, 0);
			changed = true;
		}
		
		/*
		 * Corner 2 (Top right)
		 */
		if (!grid[0][K-1].isCrossed() && grid[0][K-1].value == grid[0][K-2].value && grid[0][K-1].value == grid[1][K-1].value) {
			crossOut(grid, g, 0, K-1);
			changed = true;
		}
		
		/*
		 * Corner 3 (Bottom left)
		 */
		if (!grid[K-1][0].isCrossed() && grid[K-1][0].value == grid[K-1][1].value && grid[K-1][0].value == grid[K-2][0].value) {
			crossOut(grid, g, K-1, 0);	
			changed = true;
		}
		
		/*
		 * Corner 4 (Bottom right)
		 */
		if (!grid[K-1][K-1].isCrossed() && grid[K-1][K-1].value == grid[K-1][K-2].value && grid[K-1][K-1].value == grid[K-2][K-1].value) {
			crossOut(grid, g, K-1, K-1);
			changed = true;
		}
		return changed;
	}
	
	private static boolean basicTechniques(Cell [][] grid, Graph g, ArrayList<HashMap<Integer, Integer>> rows, ArrayList<HashMap<Integer, Integer>> columns) {
		boolean changed = false;
		
		changed = changed | shadedSquaresInRowAndColumn(grid, g, rows, columns);
		changed = changed | unshadingToAvoidPartitions(grid, g);
		
		return changed;
	}
	
	private static boolean unshadingToAvoidPartitions(Cell[][] grid, Graph g) {
		boolean changed = false;
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < K; j++) {
				if (!grid[i][j].isCrossed()) {								
					if (grid[i][j].canBeCrossed() && formsPartitionIfCrossed(grid, g, i, j) ) {
						grid[i][j].setUnCrossable();
						changed = true;
					}
				}
			}
		}
		return changed;
	}
	
	private static boolean formsPartitionIfCrossed(Cell [][] grid, Graph g, int iToBe, int jToBe) {
		boolean prevCanBeCrossed = grid[iToBe][jToBe].canBeCrossed();
		grid[iToBe][jToBe].crossOut();
				
		if (iToBe+1 < K) {
			g.removeEdge(iToBe*K+jToBe, (iToBe+1)*K+jToBe);
		}
		
		if (iToBe-1 >= 0) {
			g.removeEdge(iToBe*K+jToBe, (iToBe-1)*K+jToBe);
		}
		
		if (jToBe+1 < K) {
			g.removeEdge(iToBe*K+jToBe, iToBe*K+jToBe+1);
		}
		
		if (jToBe-1 >= 0) {
			g.removeEdge(iToBe*K+jToBe, iToBe*K+jToBe-1);
		}
	
		CC cc = new CC(g, grid);
		
		grid[iToBe][jToBe].unCrossOut(prevCanBeCrossed);
		
		if (iToBe+1 < K) {
			g.addEdge(iToBe*K+jToBe, (iToBe+1)*K+jToBe);
		}
		
		if (iToBe-1 >= 0) {
			g.addEdge(iToBe*K+jToBe, (iToBe-1)*K+jToBe);
		}
		
		if (jToBe+1 < K) {
			g.addEdge(iToBe*K+jToBe, iToBe*K+jToBe+1);
		}
		
		if (jToBe-1 >= 0) {
			g.addEdge(iToBe*K+jToBe, iToBe*K+jToBe-1);
		}
		
		
		if (cc.count() > 1) {
			return true;
		}
		
		return false;
	}
	
	private static boolean shadedSquaresInRowAndColumn(Cell [][] grid, Graph g, ArrayList<HashMap<Integer, Integer>> rows, ArrayList<HashMap<Integer, Integer>> columns) {
		boolean changed = false;
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < K; j++) {
				if (!grid[i][j].canBeCrossed() && !grid[i][j].isCrossed() && rows.get(i).get(grid[i][j].value) > 1) {
					
					for (int k = 0; k < K; k++) {
						if (k == j) {
							continue;
						}
						
						if (grid[i][j].value == grid[i][k].value && !grid[i][k].isCrossed()) {
							crossOut(grid, g, i, k);
							changed = true;
						}
					}
					
				}
				
				if (!grid[i][j].canBeCrossed() && !grid[i][j].isCrossed() && columns.get(j).get(grid[i][j].value) > 1) {
					for (int k = 0; k < K; k++) {
						if (k == i) {
							continue;
						}
						
						if (grid[i][j].value == grid[k][j].value && !grid[k][j].isCrossed()) {
							crossOut(grid, g, k, j);
							changed = true;
						}
					}
				}
			}
		}
		
		return changed;
	}
	
	
	private static boolean startingTechniques(Cell [][] grid, Graph g, ArrayList<HashMap<Integer, Integer>> rows, ArrayList<HashMap<Integer, Integer>> columns) {
		boolean changed = false;
		changed = changed | adjacentTriplets(grid, g);
		changed = changed | squareBetweenPair(grid);
		changed = changed | pairInduction(grid, g, rows, columns);
		return changed;
	}
	
	private static boolean pairInduction(Cell[][] grid, Graph g, ArrayList<HashMap<Integer, Integer>> rows, ArrayList<HashMap<Integer, Integer>> columns) {
		boolean changed = false;
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < K; j++) {
				
				if (j > 0 && j < K-1) {
							
					int rowCount = rows.get(i).get(grid[i][j].value);				
					
					if (rowCount >= 3) {
						if (grid[i][j].value == grid[i][j-1].value || grid[i][j].value == grid[i][j+1].value) {
							
							for (int k = 0; k < K; k++) {
																
								if (grid[i][j].value == grid[i][j-1].value 
										&& (k == j || k == j-1)) {
									continue;
								}
								
								if (grid[i][j].value == grid[i][j+1].value 
										&& (k == j || k == j+1)) {
									continue;
								}
								
								if (grid[i][k].value == grid[i][j].value) {
									
									if (grid[i][k].canBeCrossed() && grid[i][j].canBeCrossed()) {
										//grid[i][k].crossOut();
										crossOut(grid, g, i, k);
										changed = true;
									}
									break;
								}
							}						
						} 
					}
				}
				
				if (i > 0 && i < K-1) {
					int columnCount = columns.get(j).get(grid[i][j].value);
										
					if (columnCount >= 3) {
						if (grid[i][j].value == grid[i-1][j].value || grid[i][j].value == grid[i+1][j].value) {
														
							for (int k = 0; k < K; k++) {
								
								if (grid[i][j].value == grid[i-1][j].value 
										&& (k == i || k == i-1)) {
									continue;
								}
								
								if (grid[i][j].value == grid[i+1][j].value 
										&& (k == i || k == i+1)) {
									continue;
								}
								
								if (grid[i][j].value == grid[k][j].value) {									
									if (grid[i][j].canBeCrossed() && grid[k][j].canBeCrossed()) {
										crossOut(grid, g, k, j);
										changed = true;
									}
									break;
								}
							}
						} 
					}
				}
			}
		}
		return changed;
	}
	
	private static boolean squareBetweenPair(Cell[][] grid) {
		boolean changed = false;
		
		//Start at 1,1 since can't be pair if at 0,0
		for (int i = 1; i < K-1; i++) {
			for (int j = 1; j < K-1; j++) {
				//Check up and down
				if (grid[i][j].canBeCrossed() && grid[i-1][j].value == grid[i+1][j].value) {
					grid[i][j].setUnCrossable();
					changed = true;
				}
				
				//Check left and right
				if (grid[i][j].canBeCrossed() && grid[i][j+1].value == grid[i][j-1].value) {
					grid[i][j].setUnCrossable();
					changed = true;
				}
			}
		}
		return changed;
		
	}

	private static boolean adjacentTriplets(Cell[][] grid, Graph g) {
		boolean changed = false;
		//Start at 1,1 since can't be triple if at 0,0
		for (int i = 1; i < K-1; i++) {
			for (int j = 1; j < K-1; j++) {
				//Check up and down
				if (grid[i][j].canBeCrossed() && grid[i][j].value == grid[i-1][j].value 
						&& grid[i][j].value == grid[i+1][j].value) {
					
					crossOut(grid, g, i-1, j);
					crossOut(grid, g, i+1, j);
					changed = true;
				}
				
				//Check left and right
				if (grid[i][j].canBeCrossed() && grid[i][j].value == grid[i][j-1].value 
						&& grid[i][j].value == grid[i][j+1].value) {
					
					crossOut(grid, g, i, j-1);
					crossOut(grid, g, i, j+1);
					changed = true;
				}
			}
		}
		return changed;
	}
	
	private static void crossOut(Cell[][] grid, Graph g, int i, int j) {
		grid[i][j].crossOut();
		
		if (i+1 < K) {
			grid[i+1][j].setUnCrossable();
			g.removeEdge(i*K+j, (i+1)*K+j);
		}
		
		if (i-1 >= 0) {
			grid[i-1][j].setUnCrossable();
			g.removeEdge(i*K+j, (i-1)*K+j);
		}
		
		if (j+1 < K) {
			grid[i][j+1].setUnCrossable();
			g.removeEdge(i*K+j, i*K+j+1);
		}
		
		if (j-1 >= 0) {
			grid[i][j-1].setUnCrossable();
			g.removeEdge(i*K+j, i*K+j-1);
		}
		
		
	}
	
	private static void read(BufferedReader in, Cell [][] grid, ArrayList<HashMap<Integer, Integer>> rows, ArrayList<HashMap<Integer, Integer>> columns, Graph g) throws IOException {
		String line = "";
		
		for (int i = 0; i < K; i++) {
			columns.add(new HashMap<Integer, Integer>());
		}
		
		for (int i = 0; i < K; i++) {
			
			line = in.readLine();
			
			String data[] = line.split(" ");
			
			HashMap <Integer, Integer> elementCount = new HashMap<Integer, Integer>();
			
			for (int j = 0; j < K; j++) {
				int value = Integer.parseInt(data[j]);
				grid[i][j] = new Cell(i, j, value);
				
				if (elementCount.containsKey(value)) {
					elementCount.put(value, elementCount.get(value)+1);
				} else {
					elementCount.put(value, 1);
				}
				
				if (columns.get(j).containsKey(value)) {
					columns.get(j).put(value, columns.get(j).get(value)+1);
				} else {
					columns.get(j).put(value, 1);
				}
				
				if (j-1 >= 0) {
					g.addEdge(i*K+j, i*K+j-1);
				}
				
				if (j+1 < K) {
					g.addEdge(i*K+j, i*K+j+1);
				}
				
				if (i-1 >= 0) {
					g.addEdge(i*K+j, (i-1)*K+j);
				}
				
				if (i+1 < K) {
					g.addEdge(i*K+j, (i+1)*K+j);
				}
				
			}
			
			rows.add(elementCount);
		}
	}
	
	public static void print(Cell [][] grid) {
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < K; j++) {
				if (grid[i][j].isCrossed()) {
					System.out.print("(" + grid[i][j].value + ")");
				} else if (!grid[i][j].canBeCrossed()) {
					System.out.print(grid[i][j].value);
				} else {
					System.out.print(grid[i][j].value);
				}
				
				if (j != K-1) {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	
	public static void process(BufferedReader in, PrintStream out) throws NumberFormatException, IOException {
        int N = Integer.parseInt(in.readLine());
        
        for (int i = 1; i <= N; i++) {
            out.println("" + i + ":");
            processCase(in, out);
        }
    }
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		//final long startTime = System.currentTimeMillis();
		
		process(new BufferedReader(new InputStreamReader(System.in)), System.out);
		//final long endTime = System.currentTimeMillis();

		//System.out.println("Total execution time: " + (endTime - startTime) );
	}

}
