import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class Main {
	
	private static int[] r;
	private static int[] c;
	private static int[] u;
	private static int[] d;
	
	private static int[][] grid;
	private static int R;
	private static int C;
	
	private static final boolean DEBUG = false;
	
	public static void processCase(BufferedReader in, PrintStream out) throws IOException {
		String line = in.readLine();
		
		String data[] = line.split(" ");
		R = Integer.parseInt(data[0]);
		C = Integer.parseInt(data[1]);
		
		r = new int[R];
		c = new int[C];
		u = new int[R+C-1];
		d = new int[R+C-1];
		
		grid = new int[R][C];
		int [] zeros = new int[4];
		
		readData(in, zeros);
		
		
		
		if (algorithm(zeros)) {
			print(grid);
		} else {
			
			HashSet<Point> openSpaces = new HashSet<Point>();
						
			for (int i = 0; i < R; i++) {
				for (int j = 0; j < C; j++) {
					if (grid[i][j] == 0) {
						openSpaces.add(new Point(j, i));
					}
				}
			}
			System.out.println(openSpaces.size());			
			System.out.println(backtrack(zeros, openSpaces));
			System.out.println(openSpaces.size());
			print(grid);
		}
	}
	
	private static boolean algorithm(int [] zeros) {
		boolean changed = false;
		while(zeros[0] != R && zeros[1] != C && zeros[2] != R+C-1 && zeros[3] != R+C-1) {
			
			changed = false;
			/*
			 * Rows
			 */
			for (int i = 0; i < R; i++) {
				if (zeros[0] == R && zeros[1] == C && zeros[2] == R+C-1 && zeros[3] == R+C-1) {
					break;
				}
				
				if (r[i] == 0) {
					continue;
				}
							
				HashSet<Integer> columnOpenSpaces = new HashSet<Integer>();
				int totalOpenRow = addRow(columnOpenSpaces, i);
				
				if (totalOpenRow == r[i]) {
					r[i] = 0;
					zeros[0]++;
										
					for (int j: columnOpenSpaces) {
						int dIndex = C-1-j + R-1-(R-1-i);
						c[j] -= 1;
						grid[i][j] = 1;
						
						if (c[j] == 0) {
							zeros[1]++;
							clearColumn(j);
						}
						
						if (u[i+j] != 0) {
							u[i+j] -= 1;
							if (u[i+j] == 0) {
								zeros[2]++;
								clearUp(i+j);
							}
						}					
	
						if (d[dIndex] != 0) {
							d[dIndex] -= 1;
							if (d[dIndex] == 0) {
								zeros[3]++;
								clearDown(dIndex);
							}
						}					
					}
					
					clearRow(i);
					changed = true;
				}			
			}
			
			if (zeros[0] == R && zeros[1] == C && zeros[2] == R+C-1 && zeros[3] == R+C-1) {
				break;
			}
			
			/*
			 * Columns
			 */
			for (int j = 0; j < C; j++) {
				if (zeros[0] == R && zeros[1] == C && zeros[2] == R+C-1 && zeros[3] == R+C-1) {
					break;
				}
				
				if (c[j] == 0) {
					continue;
				}
				
				HashSet<Integer> rowOpenSpaces = new HashSet<Integer>();
				int totalOpenCol = addCol(rowOpenSpaces, j);
				
				if (totalOpenCol == c[j]) {
					c[j] = 0;
					zeros[1]++;
					
					for (int i: rowOpenSpaces) {
						int dIndex = C-1-j + R-1-(R-1-i);
						r[i] -= 1;
						grid[i][j] = 1;
						
						if (r[i] == 0) {
							zeros[0]++;
							clearRow(i);
						}
	
						if (u[i+j] != 0) {
							u[i+j] -= 1;
							if (u[i+j] == 0) {
								zeros[2]++;
								clearUp(i+j);
							}
						}
						
						if (d[dIndex] != 0) {
							d[dIndex] -= 1;
							if (d[dIndex] == 0) {
								zeros[3]++;
								clearDown(dIndex);
							}
						}						
					}
					clearColumn(j);
					changed = true;
				}			
			}
			
			if (zeros[0] == R && zeros[1] == C && zeros[2] == R+C-1 && zeros[3] == R+C-1) {
				break;
			}
					/*
			 * Up and down
			 */
			for (int k = 0; k < R+C-1; k++) {
				
				if (zeros[0] == R && zeros[1] == C && zeros[2] == R+C-1 && zeros[3] == R+C-1) {
					break;
				}
				
				if (d[k] == 0 && u[k] == 0) {
					continue;
				}				
				
				/*
				 * Up
				 */
				if (u[k] != 0) {			
					HashSet<Point> upOpenSpaces = new HashSet<Point>();
					
					int totalOpenUp = addUp(upOpenSpaces, k);
					
					if (totalOpenUp == u[k]) {
						u[k] = 0;
						zeros[2]++;
						for (Point p: upOpenSpaces) {
							int dIndex = C-1-p.x + R-1-(R-1-p.y);
							r[p.y] -= 1;
							grid[p.y][p.x] = 1;
							
							if (r[p.y] == 0) {
								zeros[0]++;
								clearRow(p.y);
							}
							
							c[p.x] -= 1;
							if (c[p.x] == 0) {
								zeros[1]++;
								clearColumn(p.x);
							}
							
							if (d[dIndex] != 0) {
								d[dIndex] -= 1;
								if (d[dIndex] == 0) {
									zeros[3]++;
									clearDown(dIndex);
								}
							}						
						}
						clearUp(k);
						changed = true;
					}
				}
				
				/*
				 * End up
				 * Down
				 */
				
				if (d[k] != 0) {
					HashSet<Point> downOpenSpaces = new HashSet<Point>();
					
					int totalOpenDown = addDown(downOpenSpaces, k);
					
													
					if (totalOpenDown == d[k]) {
						d[k] = 0;
						zeros[3]++;
						for (Point p: downOpenSpaces) {
							r[p.y] -= 1;
							grid[p.y][p.x] = 1;
							
							if (r[p.y] == 0) {
								zeros[0]++;
								clearRow(p.y);
							}
							
							c[p.x] -= 1;
							if (c[p.x] == 0) {
								zeros[1]++;
								clearColumn(p.x);
							}
		
							if (u[p.y+p.x] != 0) {
								u[p.y+p.x] -= 1;
								if (u[p.y+p.x] == 0) {
									zeros[2]++;
									clearUp(p.x+p.y);
								}
							}						
						}
						clearDown(k);
						changed = true;
					}
				}
			}
			
			if (!changed) {
				break;
			}			
		}
		
		return zeros[0] == R && zeros[1] == C && zeros[2] == R+C-1 && zeros[3] == R+C-1;
	}
	
	
	private static boolean backtrack(int [] zeros, HashSet<Point> openSpaces) {
		if (zeros[0] == R && zeros[1] == C && zeros[2] == R+C-1 && zeros[3] == R+C-1) {
			return true;
		}
		
		Iterator<Point> i = openSpaces.iterator();
		
		HashSet<Point> removed = new HashSet<Point>();
		
		HashSet<Point> copy = new HashSet<Point>();
		copy.addAll(openSpaces);
		
		while(i.hasNext()) {
		
			Point p = i.next();
			i.remove();
			copy.remove(p);
			int [][] tempGrid = copyGrid();
					
			boolean rowZero = false;
			boolean columnZero = false;
			boolean upZero = false;
			boolean downZero = false;
			
			if (checkRow(p.y, p.x) && checkColumn(p.x, p.y) && checkUp(p) && checkDown(p)) {
				
				grid[p.y][p.x] = 1;
				
				int dIndex = C-1-p.x + R-1-(R-1-p.y);
				
				r[p.y] -= 1;
						
				if (r[p.y] == 0) {
					zeros[0]++;
					clearRow(p.y, copy, removed);
					rowZero = true;
				}
				
				c[p.x] -= 1;
				if (c[p.x] == 0) {
					zeros[1]++;
					clearColumn(p.x, copy, removed);
					columnZero = true;
				}
				
				u[p.x+p.y] -= 1;
				if (u[p.x+p.y] == 0) {
					zeros[2]++;
					clearUp(p.x+p.y, copy, removed);
					upZero = true;
				}				
				
				d[dIndex] -= 1;
				if (d[dIndex] == 0) {
					zeros[3]++;
					clearDown(dIndex, copy, removed);
					downZero = true;
				}
				
				int [][] tempGrid2 = copyGrid();
				int [] tempZeros = copyArr(zeros);
				int [] tempR = copyArr(r);
				int [] tempC = copyArr(c);
				int [] tempU = copyArr(u);
				int [] tempD = copyArr(d);
				
				if (algorithm(zeros)) {
					return true;
				} else {
					
					grid = tempGrid2;
					zeros = tempZeros;
					r = tempR;
					c = tempC;
					u = tempU;
					d = tempD;
					
					if(backtrack(zeros, copy)) {
						return true;
					} else {
						r[p.y] += 1;
						c[p.x] += 1;
						u[p.x+p.y] += 1;
						d[dIndex] += 1;
						
						if (rowZero) {
							zeros[0]--;
						}
						
						if (columnZero) {
							zeros[1]--;
						}
						
						if (upZero) {
							zeros[2]--;
						}
						
						if (downZero) {
							zeros[3]--;
						}
						grid = tempGrid;
					}
				}
			}		
		
			
			removed.add(p);
		}
		
		openSpaces.addAll(removed);
		
		return false;
	}
	
	private static int [][] copyGrid() {
		int [][] tempGrid = new int[R][C];
		
		for (int i = 0; i < R; i++) {
			for (int j = 0; j < C; j++) {
				tempGrid[i][j] = grid[i][j];
			}
		}
		
		return tempGrid;
	}
	
	private static int[] copyArr(int arr[]) {
		int [] newArr = new int[arr.length];
		
		for (int i = 0; i < arr.length; i++) {
			newArr[i] = arr[i];
		}
		
		return newArr;
	}
	
	private static boolean checkRow(int t, int j) {
									
		
		int dIndex = C-1-j + R-1-(R-1-t);
		
		if (c[j] - 1 < 0) {
			return false;
		}				
		
		if (u[t+j] - 1 < 0) {
			return false;
		}
		
		if (d[dIndex] - 1 < 0) {
			return false;
		}				
		return true;			
	}
	
	private static boolean checkColumn(int t, int i) {
		
		int dIndex = C-1-t + R-1-(R-1-i);
		
		if (r[i] - 1 < 0) {
			return false;
		}		

		if (u[i+t] - 1 < 0) {
			return false;
		}
		
		if (d[dIndex] -1 < 0) {
			return false;				
		}						
		
		return true;
	}
	
	private static boolean checkUp(Point p) {
		
		int dIndex = C-1-p.x + R-1-(R-1-p.y);
		
		if (r[p.y] - 1 < 0) {
			return false;
		}
				
		if (c[p.x] - 1 < 0) {
			return false;
		}
					
		if (d[dIndex] - 1 < 0) {
			return false;
		}						
		
		return true;
	}
	
	private static boolean checkDown(Point p) {
		
		if (r[p.y] - 1 < 0) {
			return false;
		}
		
		if (c[p.x] - 1 < 0) {
			return false;
		}
		
		if (u[p.y+p.x] -1 <  0) {
			return false;
		}		
		return true;
	}
	
	private static int addRow(HashSet<Integer> columnOpenSpaces, int i) {
		int totalOpenRow = 0;
		for (int j = 0; j < C; j++) {
			if (c[j] == 0) {
				continue;
			}
			
			if (grid[i][j] == 0) {
				totalOpenRow++;
				columnOpenSpaces.add(j);
			}
		}
		
		return totalOpenRow;
	}
	
	private static int addCol(HashSet<Integer> rowOpenSpaces, int j) {
		int totalOpenCol = 0;
		for (int i = 0; i < R; i++) {
			if (r[i] == 0) {
				continue;
			}
			
			if (grid[i][j] == 0) {
				totalOpenCol++;
				rowOpenSpaces.add(i);
			}
		}
		return totalOpenCol;
	}
	
	private static int addUp(HashSet<Point> upOpenSpaces, int k) {
		int j = 0;
		int totalOpenUp = 0;
		if (k < C) {
			for (int i = k; i >= 0 && j < R; i--) {
				if (grid[j][i] == 0) {
					totalOpenUp++;
					upOpenSpaces.add(new Point(i,j));
				}
				j++;
			}
		} else {
			int kTemp = k+1;
			j = kTemp - C;
			for (int i = C-1; i >= 0 && j < R; i--) {
				if (grid[j][i] == 0) {
					totalOpenUp++;
					upOpenSpaces.add(new Point(i,j));
				}
				j++;
			}
		}
		return totalOpenUp; 
	}
	
	private static int addDown(HashSet<Point> downOpenSpaces, int k) {
		int j = 0;
		int totalOpenDown = 0;
		
		if (k < R) {
			for (int i = k; i >= 0 && j < C; i--) {
				if (grid[i][C-1-j] == 0) {
					totalOpenDown++;
					downOpenSpaces.add(new Point(C-1-j,i));
				}
				j++;
			}	
		} else {
			for (int i = R-1; i >= 0 && j < C - (k - (R-1)); i--) {
				if (grid[i][C-1-(k-(R-1))-j] == 0) {
					totalOpenDown++;
					downOpenSpaces.add(new Point(C-1-(k-(R-1))-j,i));
				}
				j++;
			}
		}
				
		return totalOpenDown;
		
	}
	
	private static void readData(BufferedReader in, int [] zeros) throws IOException {
		String line = in.readLine();
		String [] data = line.split(" ");
		
		for (int i = 0; i < r.length; i++) {
			r[i] = Integer.parseInt(data[i]);
			
			if (r[i] == 0) {
				clearRow(i);
				zeros[0]++;
			}
		}
		
		line = in.readLine();
		data = line.split(" ");
		
		for (int i = 0; i < c.length; i++) {
			c[i] = Integer.parseInt(data[i]);
			
			if (c[i] == 0) {
				clearColumn(i);
				zeros[1]++;
			}
		}
		
		line = in.readLine();
		data = line.split(" ");
		
		String lineDown = in.readLine();
		String downData[] = lineDown.split(" ");
		
		for (int i = 0; i < u.length; i++) {
			u[i] = Integer.parseInt(data[i]);
			d[i] = Integer.parseInt(downData[i]);
			
			if (u[i] == 0) {
				clearUp(i);
				zeros[2]++;
			}
			
			if (d[i] == 0) {
				clearDown(i);
				zeros[3]++;
			}
		}
		
	}
	
	private static void clearRow(int row, HashSet<Point> openSpaces, HashSet<Point> removed) {
		for (int i = 0; i < grid[0].length; i++) {
			if (grid[row][i] == 0) { 
				grid[row][i] = 2;
							
				if (openSpaces != null) {
					Point p =new Point(i,row);
					if (openSpaces.contains(p)){
						//openSpaces.remove(p);
						//removed.add(p);
					}
				}
			}
		}
	}
	
	private static void clearRow(int row) {
		clearRow(row, null, null);
	}
	
	private static void clearColumn(int col, HashSet<Point> openSpaces, HashSet<Point> removed) {
		
		for (int i = 0; i < grid.length; i++) {
			if (grid[i][col] == 0) { 
				grid[i][col] = 2;
				
				if (openSpaces != null) {
					Point p =new Point(col, i);
					if (openSpaces.contains(p)){
						//openSpaces.remove(p);
						//removed.add(p);
					}
				}
			}
		}
	}
	
	private static void clearColumn(int col) {
		clearColumn(col, null, null);
	}
	
	private static void clearUp(int upIndex, HashSet<Point> openSpaces, HashSet<Point> removed) {		
		int j = 0;
		
		if (upIndex < C) {
			for (int i = upIndex; i >= 0 && j < R; i--) {
				if (grid[j][i] == 0) {
					grid[j][i] = 2;
					
					if (openSpaces != null) {
						Point p =new Point(i,j);
						if (openSpaces.contains(p)){
							//openSpaces.remove(p);
							//removed.add(p);
						}
					}
				}
				j++;
			}
		} else {
			upIndex++;
			j = upIndex - C;
			for (int i = C-1; i >= 0 && j < R; i--) {
				if (grid[j][i] == 0) {
					grid[j][i] = 2;
					if (openSpaces != null) {
						Point p =new Point(i,j);
						if (openSpaces.contains(p)){
							//openSpaces.remove(p);
							//removed.add(p);
						}
					}
				}
				j++;
			}
		}
	}
	
	private static void clearUp(int upIndex) {
		clearUp(upIndex, null, null);
		
	}
	
	private static void clearDown(int downIndex, HashSet<Point> openSpaces, HashSet<Point> removed) {
			
		int j = 0;
		
		if (downIndex < R) {
			for (int i = downIndex; i >= 0 && j < C; i--) {
				if (grid[i][C-1-j] == 0) {
					grid[i][C-1-j] = 2;
					
					if (openSpaces != null) {
						Point p =new Point(C-1-j, i);
						if (openSpaces.contains(p)){
							//openSpaces.remove(p);
							//removed.add(p);
						}
					}
				}
				j++;
			}	
		} else {
			for (int i = R-1; i >= 0 && j < C - (downIndex - (R-1)); i--) {
				if (grid[i][C-1-(downIndex-(R-1))-j] == 0) {
					grid[i][C-1-(downIndex-(R-1))-j] = 2;
					
					if (openSpaces != null) {
						Point p =new Point(C-1-(downIndex-(R-1))-j, i);
						if (openSpaces.contains(p)){
							//openSpaces.remove(p);
							//removed.add(p);
						}
					}
				}
				j++;
			}
		}	
	}
	
	private static void clearDown(int downIndex) {
		clearDown(downIndex, null, null);
	}
	
	
	private static void print(int arr[][]) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] == 1) {
					System.out.print("#");
				} else {
					System.out.print("-");
				}
			}
			System.out.println();
		}
	}
	
	private static void printDebug(int arr[][]) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				System.out.print(arr[i][j]);
			
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void process(BufferedReader in, PrintStream out) throws NumberFormatException, IOException {
        int N = Integer.parseInt(in.readLine());
        
        for (int i = 1; i <= N; i++) {
            out.println("" + i + ":");
            processCase(in, out);
        }
    }
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		process(new BufferedReader(new InputStreamReader(System.in)), System.out);
	}
}
